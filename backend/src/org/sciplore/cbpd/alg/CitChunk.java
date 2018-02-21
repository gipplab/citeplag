package org.sciplore.cbpd.alg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;

import com.google.common.collect.LinkedListMultimap;

import org.sciplore.cbpd.main.DetectionHelper;
import org.sciplore.cbpd.model.Citation;
import org.sciplore.cbpd.model.CitationpatternMember;
import org.sciplore.cbpd.model.Document;
import org.sciplore.cbpd.model.DocumentData;
import org.sciplore.cbpd.model.Pattern;
import org.sciplore.preamble.License;

/**
* Computes Citation Chunks for two documents.
*/

@License (author="Norman Meuschke")
public class CitChunk {
	private Document doc1, doc2;
	private int minCitOverlap = 1;
	private int characterWeight = 1;
	private int wordWeight = 6;
	private int sentenceWeight = 150;
	private int paragraphWeight = 870;
//	private int sectionWeight = 10000;
//	private int chapterWeight = 345000;
	private int maxCitDistance = 900;

	private ArrayList<LinkedListMultimap<Integer, Citation>> chunksDoc1, chunksDoc2;

	public CitChunk(Document d1, Document d2){
		this.doc1 = d1;
		this.doc2 = d2;
	}

	public CitChunk(Document d1, Document d2, int minOverlap, int cw, int ww, int sw, int pw, int secw, int chw, int mxd){
		this.doc1 = d1;
		this.doc2 = d2;
		this.minCitOverlap = minOverlap;
		this.characterWeight = cw;
		this.wordWeight = ww;
		this.sentenceWeight = sw;
		this.paragraphWeight = pw;
//		this.sectionWeight = secw;
//		this.chapterWeight = chw;
		this.maxCitDistance = mxd;
	}	

	public List<Pattern> getCitChunkPattern(int procedure){
		switch (procedure){

		case 30:{
			// One document chunked (adjacent citations only)
			this.chunksDoc1 = formChunks(this.doc1, 'a');
			this.chunksDoc2 = new ArrayList<LinkedListMultimap<Integer, Citation>>();
			// No merging
			// Sliding window comparison of chunks to unchunked document 2
			List<Pattern> result = compareChunks (this.chunksDoc1, this.chunksDoc2, 'o', 30);
//			print(result);
			return result;
		}

		case 31:{
			// One document chunked (adjacent citations only)
			this.chunksDoc1 = formChunks(this.doc1, 'a');
			this.chunksDoc2 = new ArrayList<LinkedListMultimap<Integer, Citation>>();
			// Single merging
			this.chunksDoc1 = mergeChunks(chunksDoc1, 's');
			// Sliding window comparison of chunks to unchunked document 2
			List<Pattern> result = compareChunks (this.chunksDoc1, this.chunksDoc2, 'o', 31);	
//			print(result);
			return result;
		}

		case 32:{
			// One document chunked (depending on predecessor)
			this.chunksDoc1 = formChunks(this.doc1, 'p');
			this.chunksDoc2 = new ArrayList<LinkedListMultimap<Integer, Citation>>();
			// No merging
			// Sliding window comparison of chunks to unchunked document 2
			List<Pattern> result = compareChunks (this.chunksDoc1, this.chunksDoc2, 'o', 32);
//			print(result);
			return result;
		}

		case 33:{
			// One document chunked (depending on predecessor)
			this.chunksDoc1 = formChunks(this.doc1, 'p');
			this.chunksDoc2 = new ArrayList<LinkedListMultimap<Integer, Citation>>();
			// Single merging
			this.chunksDoc1 = mergeChunks(chunksDoc1, 's');
			// Sliding window comparison of chunks to unchunked document 2
			List<Pattern> result = compareChunks (this.chunksDoc1, this.chunksDoc2, 'o', 33);	
//			print(result);
			return result;
		}

		case 34:{
			// One document chunked (depending on textual distance)
			this.chunksDoc1 = formChunks(this.doc1, 'w');
			this.chunksDoc2 = new ArrayList<LinkedListMultimap<Integer, Citation>>();
			// No merging
			// Sliding window comparison of chunks to unchunked document 2
			List<Pattern> result = compareChunks (this.chunksDoc1, this.chunksDoc2, 'o', 34);	
//			print(result);
			return result;
		}

		case 35:{
			// One document chunked (depending on textual distance)
			this.chunksDoc1 = formChunks(this.doc1, 'w');
			this.chunksDoc2 = new ArrayList<LinkedListMultimap<Integer, Citation>>();
			// Single merging
			this.chunksDoc1 = mergeChunks(chunksDoc1, 's');
			// Sliding window comparison of chunks to unchunked document 2
			List<Pattern> result = compareChunks (this.chunksDoc1, this.chunksDoc2, 'o', 35);	
//			print(result);
			return result;
		}

		case 40:{
			// Both documents chunked (adjacent citations only)
			this.chunksDoc1 = formChunks(this.doc1, 'a');
			this.chunksDoc2 = formChunks(this.doc2, 'a');
			// No merging
			// Chunk-to-Chunk comparison
			List<Pattern> result = compareChunks (this.chunksDoc1, this.chunksDoc2, 'b', 40);	
//			print(result);
			return result;
		}

		case 41:{
			// Both documents chunked (adjacent citations only)
			this.chunksDoc1 = formChunks(this.doc1, 'a');
			this.chunksDoc2 = formChunks(this.doc2, 'a');
			// Single merging
			this.chunksDoc1 = mergeChunks(chunksDoc1, 's');
			this.chunksDoc2 = mergeChunks(chunksDoc2, 's');
			// Chunk-to-Chunk comparison
			List<Pattern> result = compareChunks (this.chunksDoc1, this.chunksDoc2, 'b', 41);
//			print(result);
			return result;
		}

		case 42:{
			// Both documents chunked (depending on predecessor)
			this.chunksDoc1 = formChunks(this.doc1, 'p');
			this.chunksDoc2 = formChunks(this.doc2, 'p');
			// No merging
			// Chunk-to-Chunk comparison
			List<Pattern> result = compareChunks (this.chunksDoc1, this.chunksDoc2, 'b', 42);
//			print(result);
			return result;
		}

		case 43:{
			// Both documents chunked (depending on predecessor)
			this.chunksDoc1 = formChunks(this.doc1, 'p');
			this.chunksDoc2 = formChunks(this.doc2, 'p');
			// Single merging
			this.chunksDoc1 = mergeChunks(chunksDoc1, 's');
			this.chunksDoc2 = mergeChunks(chunksDoc2, 's');
			// Chunk-to-Chunk comparison
			List<Pattern> result = compareChunks (this.chunksDoc1, this.chunksDoc2, 'b', 43);
//			print(result);
			return result;
		}

		case 44:{
			// Both documents chunked (depending on textual distance)
			this.chunksDoc1 = formChunks(this.doc1, 'w');
			this.chunksDoc2 = formChunks(this.doc2, 'w');
			// No merging
			// Chunk-to-Chunk comparison
			List<Pattern> result = compareChunks (this.chunksDoc1, this.chunksDoc2, 'b', 44);		
//			print(result);
			return result;
		}

		case 45:{
			// Both documents chunked (depending on textual distance)
			this.chunksDoc1 = formChunks(this.doc1, 'w');
			this.chunksDoc2 = formChunks(this.doc2, 'w');
			// Single merging
			this.chunksDoc1 = mergeChunks(chunksDoc1, 's');
			this.chunksDoc2 = mergeChunks(chunksDoc2, 's');
			// Chunk-to-Chunk comparison
			List<Pattern> result = compareChunks (this.chunksDoc1, this.chunksDoc2, 'b', 45);
//			print(result);
			return result;
		}
		default: {
			return new ArrayList<Pattern>();
		}
		}
	}

	private ArrayList<LinkedListMultimap<Integer, Citation>> formChunks(Document doc, char chunkingStrategy) {

		ArrayList<LinkedListMultimap<Integer, Citation>> chunksInDoc = new ArrayList<LinkedListMultimap<Integer, Citation>>();
		LinkedListMultimap<Integer, Citation> curChunk = LinkedListMultimap.create();

		ArrayList<Citation> citSeq = new ArrayList<Citation> (doc.getCitations());
		HashMap<Integer,String> sharedRefs = new DetectionHelper().getSharedRefDocumentIds(this.doc1, this.doc2);

		ListIterator<Citation> citIter = citSeq.listIterator(); 
		Citation curCit, lastAdded = null;
		boolean chunkUnderConstr = false;

		// For all citations
		while(citIter.hasNext()) {
			curCit = citIter.next();	
			// If a shared citation is encountered
			if (sharedRefs.containsKey(curCit.getReference().getRefDocument().getDocumentId())){
				// If a chunk is already under construction
				if (chunkUnderConstr) {
					// Add the current citation if appropriate according to the chosen chunking strategy
					if (decisionToAdd(curChunk, curCit, lastAdded, chunkingStrategy)) 
					{	curChunk.put(curCit.getReference().getRefDocument().getDocumentId(),curCit);
					lastAdded = new Citation(curCit.getDocument(), curCit.getDocReferenceId(), curCit.getReference(), curCit.getCharacter());
					lastAdded.setCount(curCit.getCount());
					} 
					// If the current citation shouldn't be added, store the old chunk, create a new chunk, add the citation
					else {
						chunksInDoc.add(curChunk);
						curChunk = LinkedListMultimap.create();
						curChunk.put(curCit.getReference().getRefDocument().getDocumentId(),curCit);
						lastAdded = new Citation(curCit.getDocument(), curCit.getDocReferenceId(), curCit.getReference(), curCit.getCharacter());
						lastAdded.setCount(curCit.getCount());
					}
				} 
				// No chunk under construction already -> create a chunk, add the current citation
				else {
					curChunk = LinkedListMultimap.create();
					curChunk.put(curCit.getReference().getRefDocument().getDocumentId(),curCit);
					lastAdded = new Citation(curCit.getDocument(), curCit.getDocReferenceId(), curCit.getReference(), curCit.getCharacter());
					lastAdded.setCount(curCit.getCount());
					chunkUnderConstr = true;
				}
			}
		}

		// After iterating all citations, store chunks that haven't been stored yet
		if(curChunk.size()>0){
			chunksInDoc.add(curChunk);
			curChunk = null;
		}

		return chunksInDoc;
	}

	private boolean decisionToAdd(LinkedListMultimap<Integer, Citation> curChunk, 
			Citation curCit, Citation lastAddedCit, char chunkingStrategy) {
		/*
		 * Chunking strategies: 
		 * a - adjacent: only directly adjacent shared citations form a chunk
		 * 
		 * p - dependent on predecessor: distance of n non shared citations to the last
		 * preceding shared citation n<=1|| n<=s with s being the number of
		 * citations in the chunk currently under construction
		 * 
		 * w - weighted distance: textual distance expressed by a weightin scheme
		 */

		switch (chunkingStrategy) {
		// Directly adjacent citations only
		case 'a': {
			if ((lastAddedCit.getCount() + 1) == curCit.getCount())
				return true;
			else
				return false;
		}
		// Dependent on predecessor
		case 'p': {
			if (((lastAddedCit.getCount() + 2) >= curCit.getCount())
					|| (curChunk.values().size() +1 >= curCit.getCount() - lastAddedCit.getCount()))
				return true;
			else
				return false;
		}
		// Dependent on weighted distance
		case 'w': {
			if ((weightCitationDistance(lastAddedCit,curCit))<maxCitDistance)
				return true;
			else return false;
		}
		default:
			return false;
		}
	}

	private ArrayList<LinkedListMultimap<Integer,Citation>> mergeChunks(
			ArrayList<LinkedListMultimap<Integer,Citation>> chunksInDoc, char mergingStrategy) {

		/*
		 * Merging strategies: 
		 * n - none: no merge
		 * 
		 * s - single merging: adjacent chunks obtained from formChunks() are merged once at max
		 *                   
		 * c - continuous merging: if applicable, merging of previously merged chunks is allowed
		 */

		int chunkIter = 0; 
		while(chunkIter < chunksInDoc.size() - 1) {
			LinkedListMultimap<Integer, Citation> curChunk = chunksInDoc.get(chunkIter);
			ArrayList<Citation> citsInCurChunk = new ArrayList<Citation>(curChunk.values());
			ArrayList<Citation> citsInNextChunk = new ArrayList<Citation>(chunksInDoc.get(chunkIter+1).values());
			// Adjacent citations are compared to each other exactly once
			if ((citsInNextChunk.get(0).getCount() - 
					citsInCurChunk.get(citsInCurChunk.size()-1).getCount()) <= citsInCurChunk.size()+1) {
				// Chunks are merged, but chunkIter not increased 
				// -> merged chunk is compared to next chunk in line
				for (Citation curCit : citsInNextChunk) {
					curChunk.put(curCit.getReference().getRefDocument().getDocumentId(), curCit);
				}
				chunksInDoc.remove(chunkIter + 1);
				// In case of single merging, chunkIter is increased no matter if chunks were merged,
				// therefore the first chunk will not be considered for further merging
				if( mergingStrategy == 's') chunkIter++;
				// In case of continuous merging, the chunkIter is not increased if chunks could be merged,
				// therefore, the newly merged chunk is considered again for further merging 
				// with the following chunks
			}
			else {
				// If chunks could not be merged, consider the next chunk in single and continuous merging
				chunkIter++;
			} 
		}
		return chunksInDoc;
	}

	private int weightCitationDistance (Citation cit1, Citation cit2) {
		ArrayList<Integer> ret = new ArrayList<Integer>();
		ret.add((cit2.getCharacter()-cit1.getCharacter())*characterWeight);
		ret.add((cit2.getWord()-cit1.getWord())*wordWeight);
		ret.add((cit2.getSentence()-cit1.getSentence())*sentenceWeight);
		ret.add((cit2.getParagraph()-cit1.getParagraph())*paragraphWeight);
		Collections.sort(ret);
		return ret.get(0);
	}

	public List<Pattern> compareChunks(ArrayList<LinkedListMultimap<Integer, Citation>> chunksDoc1,
			ArrayList<LinkedListMultimap<Integer, Citation>> chunksDoc2, int comparisonStrategy, int proc){
		/*
		 * Comparison strategies:
		 * o - one: only one document is chunked,
		 * 			each chunk is "slid" over the 2nd, unchunked document
		 * 
		 * b - both: both documents are chunked and each chunk is compared 
		 *  		 to every chunk of the other document
		 */
		ArrayList<Pattern> result = new ArrayList<Pattern>();
		switch(comparisonStrategy) {
		case 'o':{

			// Iterate over all chunks of doc 1
			for(LinkedListMultimap<Integer, Citation> curChunk : chunksDoc1) {

				ArrayList<Citation> citSeqDoc2 = new ArrayList<Citation>();
				ArrayList<Citation> matchingCitsDoc2 = new ArrayList<Citation>();
				LinkedListMultimap<Integer, ArrayList<Citation>> matchesInCurChunk = LinkedListMultimap.create(curChunk.size());
				HashSet<Integer> citsInCurChunk = null;
				HashSet<Integer> citOverlap = null;

				int cont = 0;
				int matchLengthDoc1 = 0;
				int maxCitOverlap = minCitOverlap;


				// Initial alignment of current citation chunk in doc 1 with 
				// unchunked citation sequence of doc 2

				// Include first n citations of doc 2 (n = length of chunk)
				for (int i =0; i<Math.min(curChunk.size(),doc2.getCitations().size()); i++) {
					citSeqDoc2.add(doc2.getCitations().get(i));
					cont = i;
				}

				// Overlap calculation between chunk and first n citations of doc 2
				citsInCurChunk = new HashSet<Integer>(curChunk.keySet());
				citOverlap = new HashSet<Integer>();

				for(Citation c : citSeqDoc2){
					if(citsInCurChunk.contains(c.getReference().getRefDocument().getDocumentId())){
						matchingCitsDoc2.add(c);
						citOverlap.add(c.getReference().getRefDocument().getDocumentId());
					}
				}

				for(Integer i : citOverlap){
					matchLengthDoc1 +=curChunk.get(i).size(); 
				}


				// Match? -> provisional match of length l is stored and threshold for further matches increased
				if(matchingCitsDoc2.size()>=maxCitOverlap) {
					matchesInCurChunk.put(Math.min(matchLengthDoc1,matchingCitsDoc2.size()), matchingCitsDoc2); 
					maxCitOverlap = (Math.min(matchLengthDoc1,matchingCitsDoc2.size()));
				}


				// After initial alignment the current chunk of doc1 is "slid" 
				// over the unchunked citation sequence of doc 2 by means of a moving window
				for (int j = cont; j<doc2.getCitations().size()-1; j++) {
					matchingCitsDoc2 = new ArrayList<Citation>();

					// Previous citation to be removed from the start of the window
					citSeqDoc2.remove(0);

					// Next citation to be added to the end of the window
					citSeqDoc2.add(doc2.getCitations().get(j+1));

					// Overlap calculation between chunk and first n citations of doc 2
					citsInCurChunk = new HashSet<Integer>(curChunk.keySet());
					matchLengthDoc1 =0;
					citOverlap = new HashSet<Integer>();

					for(Citation c : citSeqDoc2){
						if(citsInCurChunk.contains(c.getReference().getRefDocument().getDocumentId())){
							matchingCitsDoc2.add(c);
							citOverlap.add(c.getReference().getRefDocument().getDocumentId());
						}
					}

					for(Integer i : citOverlap){
						matchLengthDoc1 +=curChunk.get(i).size(); 
					}


					// Match? -> provisional match of length l is stored and threshold for further matches increased
					if(matchingCitsDoc2.size()>=maxCitOverlap) {
						matchesInCurChunk.put(Math.min(matchLengthDoc1,matchingCitsDoc2.size()), matchingCitsDoc2); 
						maxCitOverlap = (Math.min(matchLengthDoc1,matchingCitsDoc2.size()));
					}

				}

				//If matches > minimum match length have been identified
				if(matchesInCurChunk.size()>0) {

					//Set for marking citations that are already part of an individually longest match
					HashSet <Integer> citsDoc2AlreadyMatched = new HashSet<Integer>();
					HashSet <Integer> matchingCitKeys = new HashSet<Integer>();


					//List of match lengths, sorted in desc. order 
					ArrayList<Integer> longestMatches = new ArrayList<Integer>(matchesInCurChunk.keys());
					Comparator<Integer> sortDesc = Collections.reverseOrder();
					Collections.sort(longestMatches,sortDesc);

					//Storing individual matches starting with longest ones
					for(Integer maxMatchLength:longestMatches){

						//List of citations being part of matches of length l
						List <ArrayList<Citation>> matchesLengthNDoc2 = matchesInCurChunk.get(maxMatchLength);

						//Check all citations in sequences representing matches of length l
						for(ArrayList<Citation> citsInMatchLengthNDoc2:matchesLengthNDoc2){
							matchingCitsDoc2 = new ArrayList<Citation>(); 
							matchingCitKeys = new HashSet<Integer>();

							for(Citation c : citsInMatchLengthNDoc2 ){	
								//Ignore citations that have been assigned to longer/equally long matches prior
								if(!citsDoc2AlreadyMatched.contains(c.getDbCitationId())){
									matchingCitsDoc2.add(c);
									matchingCitKeys.add(c.getReference().getRefDocument().getDocumentId());
								}
							}
							//If enough citations that haven't been matched before remain 
							//-> create and store new CitationPatternMatch
							if(matchingCitsDoc2.size()>=minCitOverlap){

								ArrayList<Citation> matchingCitsDoc1 = new ArrayList<Citation>();

								for (Integer i : matchingCitKeys){
									matchingCitsDoc1.addAll(curChunk.get(i));
								}

								Collections.sort(matchingCitsDoc1);

								/*Convert chunks to Patterns and CitationpatternMembers*/
								Pattern patToAdd = new Pattern(doc1, doc2, String.valueOf(proc), Math.min(matchingCitsDoc1.size(), matchingCitsDoc2.size()));
								result.add(patToAdd);							

								/*Add CitationpatternMembers for doc1*/		
								Integer[] citGaps = new DetectionHelper().getCitationPatternGaps(matchingCitsDoc1);
								int cnt = 1;

								for (Citation c1 : matchingCitsDoc1) {
									doc1.addCitationpatternMember(new CitationpatternMember(patToAdd, cnt, doc1, citGaps[cnt-1], c1));
									cnt++;
								}

								/*Add CitationpatternMembers for doc2*/	
								citGaps = new DetectionHelper().getCitationPatternGaps(matchingCitsDoc2);
								cnt=1;

								for (Citation c2 : matchingCitsDoc2) {
									doc2.addCitationpatternMember(new CitationpatternMember(patToAdd, cnt, doc2, citGaps[cnt-1], c2));
									cnt++;
								}

								for(Citation citMatched : matchingCitsDoc2){
									citsDoc2AlreadyMatched.add(citMatched.getDbCitationId());
								}
							}
						}//End iterating current list of matching citations length n in doc 2
					}//End iterating individually longest match
				}//End processing of matches in a chunk
			}//End iterating over all chunks in doc 1
			return result;
		}//End "chunk one doc only"

		//both documents are chunked
		case 'b':{ 
			// Determination of individual chunk overlaps
			// -> storage in Multimap for capturing multiple equally long matches for one chunk
			HashSet<Integer> citsInChunkDoc1;
			HashSet<Integer> citOverlap;
			ArrayList<Citation> matchingCitsInChunkDoc2;
			LinkedListMultimap<Integer, ArrayList<Citation>> bestMatchesForChunk=LinkedListMultimap.create();
			int maxCitOverlap; 
			int matchLengthDoc1;
			

			// Iteration over all chunks of doc 1
			for(LinkedListMultimap<Integer, Citation> curChunkDoc1 : chunksDoc1){
				bestMatchesForChunk = LinkedListMultimap.create(curChunkDoc1.size());
				maxCitOverlap = minCitOverlap;
				
				// Iteration over all chunks (in Multimap representation) of doc 2
				for(LinkedListMultimap<Integer, Citation> curChunkDoc2 : chunksDoc2){
					citsInChunkDoc1 = new HashSet<Integer>(curChunkDoc1.keys());
					citOverlap = new HashSet<Integer>();
					matchingCitsInChunkDoc2 = new ArrayList<Citation>();
					matchLengthDoc1 = 0;
 
					// Overlap calculation between the two chunks
					for(Citation c : curChunkDoc2.values()){
						if(citsInChunkDoc1.contains(c.getReference().getRefDocument().getDocumentId())){
							matchingCitsInChunkDoc2.add(c);
							citOverlap.add(c.getReference().getRefDocument().getDocumentId());
						}
					}
					citsInChunkDoc1.retainAll(citOverlap);

					for(Integer i : citsInChunkDoc1){
						matchLengthDoc1 +=curChunkDoc1.get(i).size(); 
					}

					// Match? -> provisional match of length l is stored and threshold for further matches increased
					if(matchingCitsInChunkDoc2.size()>=maxCitOverlap) {
						bestMatchesForChunk.put(Math.min(matchLengthDoc1, matchingCitsInChunkDoc2.size()), matchingCitsInChunkDoc2);
						maxCitOverlap = (Math.min(matchLengthDoc1, matchingCitsInChunkDoc2.size()));
					}
				}//End iteration chunks doc2

				//Matches> minimum match length have been identified?
				if(bestMatchesForChunk.size()>0) {
					HashSet<Integer> matchingCitKeys;
					ArrayList<Citation> citsInPatternDoc1; 

					//List of matching citation sequences (ArrayList<Citation>) with max. match length 
					ArrayList<ArrayList<Citation>> longestMatches = new ArrayList<ArrayList<Citation>>
					(bestMatchesForChunk.get(Collections.max(bestMatchesForChunk.keys())));

					//Storing longest matches
					for(ArrayList<Citation> curMaxMatchChunk2:longestMatches){
						matchingCitKeys = new HashSet<Integer>();
						citsInPatternDoc1 = new ArrayList<Citation>(); 

						//Obtain  cits. in chunk1 corresponding to matching cits. in chunk2
						for(Citation c : curMaxMatchChunk2){	
							matchingCitKeys.add(c.getReference().getRefDocument().getDocumentId());
						}

						for (Integer i : matchingCitKeys){
							citsInPatternDoc1.addAll(curChunkDoc1.get(i));
						}
						Collections.sort(citsInPatternDoc1);

						/*Convert chunks to Patterns and CitationpatternMembers*/
						Pattern patToAdd = new Pattern(doc1, doc2, String.valueOf(proc), Math.min(citsInPatternDoc1.size(), curMaxMatchChunk2.size()));
						result.add(patToAdd);							

						/*Add CitationpatternMembers for doc1*/		
						Integer[] citGaps = new DetectionHelper().getCitationPatternGaps(citsInPatternDoc1);
						int cnt = 1;

						for (Citation c1 : citsInPatternDoc1) {
							doc1.addCitationpatternMember(new CitationpatternMember(patToAdd, cnt, doc1, citGaps[cnt-1], c1));
							cnt++;
						}	

						/*Add CitationpatternMembers for doc2*/	
						citGaps = new DetectionHelper().getCitationPatternGaps(curMaxMatchChunk2);
						cnt=1;

						for (Citation c2 : curMaxMatchChunk2) {
							doc2.addCitationpatternMember(new CitationpatternMember(patToAdd, cnt, doc2, citGaps[cnt-1], c2));
							cnt++;
						}			
					}//End iterating maximal matching chunks in doc2
				}//End processing of matches
			}//End iterating over all chunks in doc 1

			return result;
		}
		default:{	return result;}
		}
	}

	public void print(List<Pattern> result){
// Titles
		System.err.print("Doc1: ");
		for (DocumentData data : this.doc1.getData()) {
			if (data.getType().equals("title")) {
				System.err.print(data.getValue());
			}
		}
		System.err.print("\nDoc2: ");
		for (DocumentData data : this.doc2.getData()) {
			if (data.getType().equals("title")) {
				System.err.print(data.getValue());
			}
		}
		
		// Shared references
		System.err.print("\n\nShared Refs (doc1 = doc2):\n| ");
		for (String s : new DetectionHelper().getSharedRefDocumentIds(doc1, doc2).values()){
			System.err.print(s+" | ");
		}

		// Citation sequences
		System.err.print("\n\nCitations Doc 1:\n| ");
		for(Citation c: doc1.getCitations()){
			System.err.print(c.getDocReferenceId()+" | ");
		}

		System.err.print("\n\nCitations Doc 2:\n| ");
		for(Citation c: doc2.getCitations()){
			System.err.print(c.getDocReferenceId()+" | ");
		}

		// Chunks formed
		System.err.println("\n\nChunks in Doc. 1:");
		for (LinkedListMultimap<Integer, Citation> ch : chunksDoc1) {
			System.err.print("< | ");
			for(Citation curCit : ch.values()) {
				System.err.print(curCit+" | ");
			}
			System.err.println(">");
		}

		System.err.println("\nChunks in Doc. 2:");
		for (LinkedListMultimap<Integer, Citation> ch : chunksDoc2) {
			System.err.print("< | ");
			for(Citation curCit : ch.values()) {
				System.err.print(curCit+" | ");
			}
			System.err.println(">");
		}
		// Patterns	
		System.err.print("\nPatterns:");
		for(Pattern p : result){
			System.err.print("\nPattern score= "+p.getPatternScore()+" ID= "+p.getPatternId()+"\n");
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