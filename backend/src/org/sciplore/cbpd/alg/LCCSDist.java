package org.sciplore.cbpd.alg;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.sciplore.cbpd.main.DetectionHelper;
import org.sciplore.cbpd.model.Citation;
import org.sciplore.cbpd.model.CitationpatternMember;
import org.sciplore.cbpd.model.Document;
import org.sciplore.cbpd.model.Pattern;
import org.sciplore.preamble.License;

/**
* Computes the Longest Common Citation Sequence that contains only distinct citations of two documents.<br>
* Multiple matches of identical citations are removed, e.g., if the LCCS of two documents is 1 2 3 1 4 2 the 
* distinct LCCS is 1 2 3 4.
*/

@License (author="Norman Meuschke")
public class LCCSDist {
	private Document doc1, doc2;
	ArrayList<ArrayList<Citation>> bothLccsDist;

	public LCCSDist(Document d1, Document d2) {
		this.doc1 = d1;
		this.doc2 = d2;	
	}

	public List<Pattern> getLccsDistPattern() {

		this.bothLccsDist = getLccsDist();

		/*No LCCS detected*/
		if(bothLccsDist.get(0).size()==0)
			return new ArrayList<Pattern>();

		/*LCCS>0 detected*/
		else{
			Pattern pat = new Pattern(doc1, doc2, "11", bothLccsDist.get(0).size());
			Integer[] citGaps;
			int cnt;

			/*Add CitationpatternMembers to document 1*/
			citGaps = new DetectionHelper().getCitationPatternGaps(bothLccsDist.get(0));  
			cnt=1;

			for (Citation c1 : bothLccsDist.get(0)) {
				doc1.addCitationpatternMember(new CitationpatternMember(pat, cnt, doc1, citGaps[cnt-1], c1));
				cnt++;
			}

			/*Add CitationpatternMembers to document 2*/
			citGaps = new DetectionHelper().getCitationPatternGaps(bothLccsDist.get(1));
			cnt = 1;

			for (Citation c2 : bothLccsDist.get(1)) { 
				doc2.addCitationpatternMember(new CitationpatternMember(pat, cnt, doc2, citGaps[cnt-1], c2));
				cnt++;
			}

			/*Create and return pattern*/	
			ArrayList<Pattern> result = new ArrayList<Pattern>();
			result.add(pat);
			return result;
		}
	}


	public ArrayList<ArrayList<Citation>> getLccsDist() {
		/*Compute the LCCS and get the citations involved in both documents*/
		LCCS lccs = new LCCS (this.doc1, this.doc2);
		this.bothLccsDist = lccs.getLccs();
		//		System.err.println(bothLccs.get(0));
		//		System.err.println(bothLccs.get(1));

		if(bothLccsDist.get(0).size()==0){
			return new ArrayList<ArrayList<Citation>>();
		}
		else{
			removeMultipleCitations();
			return bothLccsDist;
		}
	}

	private void removeMultipleCitations(){
		HashSet<Integer> distCitsInLccs = new HashSet<Integer>();

		for (int i=0; i<bothLccsDist.get(0).size(); i++){
			if(!distCitsInLccs.add(bothLccsDist.get(0).get(i).getReference().getRefDocument().getDocumentId())){
				bothLccsDist.get(0).remove(i);
				bothLccsDist.get(1).remove(i);
			}
		}
	}

	public void print(){
		if(this.bothLccsDist!=null){
			System.err.println("LCCS dist. Doc1:");
			for (int i = 0; i<bothLccsDist.get(0).size(); i++){
				System.err.print(bothLccsDist.get(0).get(i).getDocReferenceId()+" | ");
			}

			System.err.println("\n\nLCCS dist. Doc2:");		
			for (int i = 0; i<bothLccsDist.get(1).size(); i++){
				System.err.print(bothLccsDist.get(1).get(i).getDocReferenceId()+" | ");
			}
		}
	}
}
