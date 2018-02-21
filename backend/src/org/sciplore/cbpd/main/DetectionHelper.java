package org.sciplore.cbpd.main;

import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

import org.sciplore.cbpd.model.Citation;
import org.sciplore.cbpd.model.Document;
import org.sciplore.cbpd.model.Reference;
import org.sciplore.preamble.License;
/**
* Utility to determine the gaps consisting of of non-matching citations in citation patterns and the document_ids of matching citations.
*/

@License (author="Norman Meuschke")

public class DetectionHelper {
	
	public Integer[] getCitationPatternGaps(List <Citation> citsDoc){
		Integer[]result = new Integer[citsDoc.size()];
		Citation c1 = new Citation();
		Citation c2 = new Citation(); 

		for (ListIterator<Citation> citIter = citsDoc.listIterator(); citIter.hasNext();) {

			if(citIter.hasPrevious()){
				c2=citIter.next();
				result[citIter.previousIndex()]=c2.getCount()-c1.getCount();
				c1 = c2;
			}
			else{
				result[citIter.nextIndex()]=-1;
				c1=citIter.next();
			}
		}
		return result;
	}
	
	public HashMap<Integer,String> getSharedRefDocumentIds(Document doc1, Document doc2){
		HashMap<Integer,String> sharedRefs = new HashMap<Integer,String>();
		
		for(Reference r1: doc1.getOutReferences()){
			for(Reference r2: doc2.getOutReferences()){
				if (r1.getRefDocument().getDocumentId() == r2.getRefDocument().getDocumentId())
					sharedRefs.put(r2.getRefDocument().getDocumentId(), new String("["+r1.getDocReferenceId()+" = "+r2.getDocReferenceId()+"]"));
				}
			}
	return sharedRefs;	
	}
}
