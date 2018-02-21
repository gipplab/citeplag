package org.sciplore.cbpd.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.sciplore.preamble.License;
/**
* Container class for citation pattern information.
*/

@License (author="Mario Lipinski")
@XmlRootElement(name="CitationpatternMember")

public class CitationpatternMember {
	int patternMemberId;
	Pattern pattern;
	int count;
	Document document;
	int gap;
	Citation citation;
	
	public CitationpatternMember() {
		this.patternMemberId = this.hashCode();
	}
	
	public CitationpatternMember(Pattern pattern, int count, Document document, int gap, Citation citation) {
		this();
		this.pattern = pattern;
		this.count = count;
		this.document = document;
		this.gap = gap;
		this.citation = citation;
	}
	
	/**
	 * Returns the patternMemberId.
	 * 
	 * @return the patternMemberId
	 */
	@XmlElement(name="pattern_member_id")
	public int getPatternMemberId() {
		return patternMemberId;
	}
	/**
	 * Sets the patternMemberId.
	 * 
	 * @param patternMemberId the patternMemberId
	 */
	public void setPatternMemberId(int patternMemberId) {
		this.patternMemberId = patternMemberId;
	}
	/**
	 * Returns the pattern.
	 * 
	 * @return the pattern
	 */
	@XmlTransient
	public Pattern getPattern() {
		return pattern;
	}
	/**
	 * Sets the pattern.
	 * 
	 * @param pattern the pattern
	 */
	public void setPattern(Pattern pattern) {
		this.pattern = pattern;
	}
	
	@XmlElement(name="pattern_id")
	public int getPatternId() {
		return pattern.getPatternId();
	}
	
	/**
	 * Returns the count.
	 * 
	 * @return the count
	 */
	public int getCount() {
		return count;
	}
	/**
	 * Sets the count.
	 * 
	 * @param count the count
	 */
	public void setCount(int count) {
		this.count = count;
	}
	/**
	 * Returns the document.
	 * 
	 * @return the document
	 */
	@XmlTransient
	public Document getDocument() {
		return document;
	}
	/**
	 * Sets the document.
	 * 
	 * @param document the document
	 */
	public void setDocument(Document document) {
		this.document = document;
	}
	
	@XmlElement(name="document_id")
	public int getDocumentId() {
		return document.getDocumentId();
	}
	
	/**
	 * Returns the gap.
	 * 
	 * @return the gap
	 */
	public int getGap() {
		return gap;
	}
	/**
	 * Sets the gap.
	 * 
	 * @param gap the gap
	 */
	public void setGap(int gap) {
		this.gap = gap;
	}
	/**
	 * Returns the citation.
	 * 
	 * @return the citation
	 */
	@XmlTransient
	public Citation getCitation() {
		return citation;
	}
	/**
	 * Sets the citation.
	 * 
	 * @param citation the citation
	 */
	public void setCitation(Citation citation) {
		this.citation = citation;
	}
	
	@XmlElement(name="db_citation_id")
	public int getCitationId() {
		return citation.getDbCitationId();
	}
	
}
