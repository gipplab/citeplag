package org.sciplore.cbpd.alg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.sciplore.cbpd.main.DetectionHelper;
import org.sciplore.cbpd.model.*;
import org.sciplore.preamble.License;

/**
* Computes Greedy Citation Tiles (GCT) for two documents.
*/

@License (author="Norman Meuschke")
public class GCT {

	private Document doc1, doc2;

	private int minimumTileLength = 1;

	public GCT(Document d1, Document d2) {
		this.doc1 = d1;
		this.doc2 = d2;
	}

	public GCT(Document d1, Document d2, int min) {
		this.doc1 = d1;
		this.doc2 = d2;
		this.minimumTileLength = min;
	}


	public List<Pattern> getGctPattern(int procedure) {

		ArrayList<Citation> citsDoc1 = new ArrayList<Citation> (doc1.getCitations());
		ArrayList<Citation> citsDoc2 = new ArrayList<Citation> (doc2.getCitations());
		HashMap<Integer,String> sharedRefs = new DetectionHelper().getSharedRefDocumentIds(doc1, doc2);

		// Boolean[] storing citation markings
		// -> citations are marked when they have been assigned to a larger tile
		boolean[] markedCitsDoc1 = new boolean[citsDoc1.size()];
		boolean[] markedCitsDoc2 = new boolean[citsDoc2.size()];

		// Each match is represented as an Integer[2].
		// -> [0] starting index of match in citation sequence of doc. 1
		// -> [1] starting index of match in citation sequence of doc. 2
		// -> [2] length of match
		ArrayList<Integer[]> maximalMatches;

		// All matches of a certain length n are stored in one list.
		// All lists of matches are organized in a map using the match length as key.
		HashMap<Integer, ArrayList<Integer[]>> matchingSectionsLengthN = new HashMap<Integer, ArrayList<Integer[]>>();

		// Longest matches are grouped in tiles; all tiles are stored in a list.
		ArrayList<int[]> citationTiles = new ArrayList<int[]>();

		// Identification of matches
		int maxMatch;

		do {
			maxMatch = minimumTileLength;

			for (int iterDoc2 = 0; iterDoc2 < citsDoc2.size(); iterDoc2++) {

				for (int iterDoc1 = 0; iterDoc1 < citsDoc1.size(); iterDoc1++) {
					int matchLength = 0;

					switch(procedure){
					case 22:{
						//Include all directly adjacent shared citations when one matching citation has been found
						while ( 
								((matchLength + iterDoc1 < citsDoc1.size())
										&& (matchLength + iterDoc2 < citsDoc2.size())
										&& (!markedCitsDoc2[iterDoc2 + matchLength])
										&& (!markedCitsDoc1[iterDoc1 + matchLength])
										&& (citsDoc1.get(iterDoc1 + matchLength).matches(citsDoc2.get(iterDoc2 + matchLength)))
										) ||
										((matchLength>0)
												&& (sharedRefs.containsKey(citsDoc1.get(iterDoc1 + matchLength).getReference().getRefDocument().getDocumentId()))
												&& (sharedRefs.containsKey(citsDoc2.get(iterDoc2 + matchLength).getReference().getRefDocument().getDocumentId()))
												)
								){	
							matchLength++;
						}
						break;
					}
					default:{
						//Include direct matches only
						while ((matchLength + iterDoc1 < citsDoc1.size())
								&& (matchLength + iterDoc2 < citsDoc2.size())
								&& (!markedCitsDoc2[iterDoc2 + matchLength])
								&& (!markedCitsDoc1[iterDoc1 + matchLength])
								&& (citsDoc1.get(iterDoc1 + matchLength).matches(citsDoc2.get(iterDoc2 + matchLength))))
						{
							matchLength++;
						}
						break;
					}
					}

					// Match found
					if (matchLength >= maxMatch) {

						if (!matchingSectionsLengthN.containsKey(matchLength)) {
							ArrayList<Integer[]> matchToBeAdded = new ArrayList<Integer[]>();
							matchToBeAdded.add(new Integer [] {iterDoc1,iterDoc2});
							matchingSectionsLengthN.put(matchLength, matchToBeAdded);
						}
						else {
							ArrayList<Integer[]> curMatch = matchingSectionsLengthN.get(matchLength);
							curMatch.add(new Integer[] { iterDoc1, iterDoc2 });
						}
					}

					if (matchLength > maxMatch)
						maxMatch = matchLength;
				}
			}

			// Marking affected citations
			if (matchingSectionsLengthN.containsKey(maxMatch)) {
				maximalMatches = matchingSectionsLengthN.get(maxMatch);

				for (Integer[] match : maximalMatches) {
					boolean marked = false;

					for (int iMarker = 0; iMarker < maxMatch; iMarker++) {

						if (markedCitsDoc1[match[0] + iMarker] || markedCitsDoc2[match[1] + iMarker]) {
							marked = true;
							break;
						}
					}

					// Forming tiles based on identified match and prior markings
					if (!marked) {

						for (int iMarker = 0; iMarker < maxMatch; iMarker++) {
							markedCitsDoc1[match[0] + iMarker] = true;
							markedCitsDoc2[match[1] + iMarker] = true;
						}

						int[] newTile = new int[] { match[0], match[1], maxMatch };
						citationTiles.add(newTile);
						newTile = null;
					}
				}
			}
		} while (maxMatch > minimumTileLength);

		/*Convert tiles to Patterns and CitationpatternMembers*/
		ArrayList<Pattern> result = new ArrayList<Pattern>();
		Pattern patToAdd;
		ArrayList<Citation> citsInTile;
		Integer[] citGaps;
		int cnt;

		/*Iterate tiles*/
		for(int[] curTile : citationTiles){
			patToAdd = new Pattern(doc1, doc2, String.valueOf(procedure), curTile[2]);
			result.add(patToAdd);

			/*Add CitationpatternMembers for doc1*/	
			citsInTile = new ArrayList<Citation>();

			for(int iterDoc1 = curTile[0]; iterDoc1<curTile[0]+curTile[2]; iterDoc1++){
				citsInTile.add(citsDoc1.get(iterDoc1));
			}

			citGaps = new DetectionHelper().getCitationPatternGaps(citsInTile);
			cnt = 1;

			for (Citation c1 : citsInTile) {
				doc1.addCitationpatternMember(new CitationpatternMember(patToAdd, cnt, doc1, citGaps[cnt-1], c1));
				cnt++;
			}


			/*Add CitationpatternMembers for doc2*/	
			citsInTile = new ArrayList<Citation>();

			for(int iterDoc2 = curTile[1]; iterDoc2<curTile[1]+curTile[2]; iterDoc2++){
				citsInTile.add(citsDoc2.get(iterDoc2));
			}

			citGaps = new DetectionHelper().getCitationPatternGaps(citsInTile);
			cnt=1;

			for (Citation c2 : citsInTile) {
				doc2.addCitationpatternMember(new CitationpatternMember(patToAdd, cnt, doc2, citGaps[cnt-1], c2));
				cnt++;
			}
		}

		//print(markedCitsDoc1, markedCitsDoc2, result);
		return result;
	}
	
	public void print(boolean[] markedCitsDoc1, boolean[] markedCitsDoc2, ArrayList<Pattern> result){
		//Debugging: output of citation sequences, marked citations and citation tiles
		// Citation sequences
				System.err.print("Citations Doc 1:\n| ");
				for(Citation c: doc1.getCitations()){
					System.err.print(c.getDocReferenceId()+" | ");
				}
		
				System.err.print("\n\nCitations Doc 2:\n| ");
				for(Citation c: doc2.getCitations()){
					System.err.print(c.getDocReferenceId()+" | ");
				}
				System.err.print("\n\n");
		// Marked citations
				System.err.println("Marked citations Doc1:"); 
				for (int i=0; i < markedCitsDoc1.length; i++) {
					System.err.print("["+i+":"+markedCitsDoc1[i]+"]"); 
				}
				System.err.println("\n\nMarked citations Doc2:"); 
				for (int i=0; i < markedCitsDoc2.length; i++) {
					System.err.print("["+i+":"+markedCitsDoc2[i]+"]"); 
				}
		// Patterns	
		System.err.print("\n\nPatterns:");
		for(Pattern p : result){
			System.err.print("\nPattern score= "+p.getPatternScore()+"\n");
			System.err.print("Doc1: <");
			for (CitationpatternMember m : doc1.getCitationpatternMember()){
				if(m.getPatternId()==p.getPatternId()){
					System.err.print(m.getCount()+": "+m.getCitation().getReference().getDocReferenceId()+" | ");
				}
			}
			System.err.println(">");
			System.err.print("Doc2: <");
			for (CitationpatternMember m : doc2.getCitationpatternMember()){
				if(m.getPatternId()==p.getPatternId()){
					System.err.print(m.getCount()+": "+m.getCitation().getReference().getDocReferenceId()+" | ");
				}
			}
			System.err.println(">");	
		}
	}
}
