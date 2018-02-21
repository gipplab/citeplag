package org.sciplore.cbpd.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import org.sciplore.preamble.License;

/**
* Container class for patterns.
*/

@License (author="Mario Lipinski")

public class Pattern {
	int patternId;
	Document document1;
	Document document2;
	String procedure;
	int patternScore;
	
	public Pattern() {
		patternId = this.hashCode();
	}

	public Pattern(Document document1, Document document2, String procedure, int patternScore) {
		this();
		this.document1 = document1;
		this.document2 = document2;
		this.procedure = procedure;
		this.patternScore = patternScore;
	}

	/**
	 * Returns the patternId.
	 * 
	 * @return the patternId
	 */
	@XmlElement(name="pattern_id")
	public int getPatternId() {
		return patternId;
	}

	/**
	 * Sets the patternId.
	 * 
	 * @param patternId the patternId
	 */
	public void setPatternId(int patternId) {
		this.patternId = patternId;
	}

	/**
	 * Returns the document1.
	 * 
	 * @return the document1
	 */
	@XmlTransient
	public Document getDocument1() {
		return document1;
	}

	/**
	 * Sets the document1.
	 * 
	 * @param document1 the document1
	 */
	public void setDocument1(Document document1) {
		this.document1 = document1;
	}
	
	@XmlElement(name="document_id1")
	public int getDocument1Id() {
		return document1.getDocumentId();
	}

	/**
	 * Returns the document2.
	 * 
	 * @return the document2
	 */
	@XmlTransient
	public Document getDocument2() {
		return document2;
	}

	/**
	 * Sets the document2.
	 * 
	 * @param document2 the document2
	 */
	public void setDocument2(Document document2) {
		this.document2 = document2;
	}

	@XmlElement(name="document_id2")
	public int getDocument2Id() {
		return document2.getDocumentId();
	}

	/**
	 * Returns the procedure.
	 * 
	 * @return the procedure
	 */
	public String getProcedure() {
		return procedure;
	}

	/**
	 * Sets the procedure.
	 * 
	 * @param procedure the procedure
	 */
	public void setProcedure(String procedure) {
		this.procedure = procedure;
	}

	/**
	 * Returns the patternScore.
	 * 
	 * @return the patternScore
	 */
	@XmlElement(name="pattern_score")
	public int getPatternScore() {
		return patternScore;
	}

	/**
	 * Sets the patternScore.
	 * 
	 * @param patternScore the patternScore
	 */
	public void setPatternScore(int patternScore) {
		this.patternScore = patternScore;
	}
}
