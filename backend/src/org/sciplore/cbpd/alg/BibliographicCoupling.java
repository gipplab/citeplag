package org.sciplore.cbpd.alg;

import java.util.ArrayList;
import java.util.List;

import org.sciplore.cbpd.model.Citation;
import org.sciplore.cbpd.model.CitationpatternMember;
import org.sciplore.cbpd.model.Document;
import org.sciplore.cbpd.model.Pattern;
import org.sciplore.cbpd.model.Reference;
import org.sciplore.preamble.*;

/**
* Computes the Bibliographic Coupling strength (number of matching references) between two documents.
*/

@License (author="Mario Lipinski")
public class BibliographicCoupling {
	List<Pattern> patterns = new ArrayList<Pattern>();
	Document doc1;
	Document doc2;
	
	public BibliographicCoupling(Document doc1, Document doc2) {
		this.doc1 = doc1;
		this.doc2 = doc2;
	}
	
	public List<Pattern> process() {
		int strength = 0;
		for (Reference r1 : doc1.getOutReferences()) {
			for (Reference r2 : doc2.getOutReferences()) {
				if (r1.getRefDocument() == r2.getRefDocument()) {
					strength++;
					
					Pattern p = new Pattern(doc1, doc2, "71", 0);
					
					int score = 0;
					for (Citation c : r1.getCitations()) {
						score++;
						CitationpatternMember m = new CitationpatternMember(p, strength, c.getDocument(), 0, c);
						r1.getContDocument().addCitationpatternMember(m);
					}
					
					for (Citation c : r2.getCitations()) {
						score++;
						CitationpatternMember m = new CitationpatternMember(p, strength, c.getDocument(), 0, c);
						r2.getContDocument().addCitationpatternMember(m);
					}
					
					p.setPatternScore(score);
					
					patterns.add(p);
				}
			}
		}
		patterns.add(new Pattern(doc1, doc2, "70", strength));
		return patterns;
	}
}
