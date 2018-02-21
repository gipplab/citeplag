package org.sciplore.cbpd.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.sciplore.preamble.License;

@License (author="Mario Lipinski")
@XmlRootElement(name="response")

public class ResponseContainer {
	Document doc1;
	Document doc2;
	List<Pattern> pattern = new ArrayList<Pattern>();
	/**
	 * Returns the doc1.
	 * 
	 * @return the doc1
	 */
	public Document getDoc1() {
		return doc1;
	}
	
	public void disambiguate() {
//		if (doc1.equals(doc2)) {
//			doc2 = doc1;
//		}
		
		for (Reference r1 : doc1.getOutReferences()) {
			for (Reference r2 : doc2.getOutReferences()) {
				if (r1.getRefDocument().equals(r2.getRefDocument())) {
					r2.setRefDocument(r1.getRefDocument());
				}
			}
		}
	}
	
	
	/**
	 * Sets the doc1.
	 * 
	 * @param doc1 the doc1
	 */
	public void setDoc1(Document doc1) {
		this.doc1 = doc1;
	}
	/**
	 * Returns the doc2.
	 * 
	 * @return the doc2
	 */
	public Document getDoc2() {
		return doc2;
	}
	/**
	 * Sets the doc2.
	 * 
	 * @param doc2 the doc2
	 */
	public void setDoc2(Document doc2) {
		this.doc2 = doc2;
	}

	/**
	 * Returns the pattern.
	 * 
	 * @return the pattern
	 */
	public List<Pattern> getPattern() {
		return pattern;
	}

	/**
	 * Sets the pattern.
	 * 
	 * @param pattern the pattern
	 */
	public void setPattern(List<Pattern> pattern) {
		this.pattern = pattern;
	}
	
	public void addPatterns(List<Pattern> patterns) {
		this.pattern.addAll(patterns);
	}
}
