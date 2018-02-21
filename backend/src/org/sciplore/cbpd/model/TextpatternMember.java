package org.sciplore.cbpd.model;

import javax.xml.bind.annotation.XmlElement;
import org.sciplore.preamble.License;

/**
* Container class for text pattern member.
*/

@License (author="Mario Lipinski")

public class TextpatternMember {
	int patternMemberId;
	int patternId;
	Pattern pattern;
	Document document;
	int startCharacter;
	int endCharacter;
	
	public TextpatternMember() {
		patternMemberId = this.hashCode();
	}

	/**
	 * Returns the patternMemberId.
	 * 
	 * @return the patternMemberId
	 */
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

	/**
	 * Returns the document.
	 * 
	 * @return the document
	 */
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

	/**
	 * Returns the startCharacter.
	 * 
	 * @return the startCharacter
	 */
	@XmlElement(name="start_character")
	public int getStartCharacter() {
		return startCharacter;
	}

	/**
	 * Sets the startCharacter.
	 * 
	 * @param startCharacter the startCharacter
	 */
	public void setStartCharacter(int startCharacter) {
		this.startCharacter = startCharacter;
	}

	/**
	 * Returns the endCharacter.
	 * 
	 * @return the endCharacter
	 */
	@XmlElement(name="end_character")
	public int getEndCharacter() {
		return endCharacter;
	}

	/**
	 * Sets the endCharacter.
	 * 
	 * @param endCharacter the endCharacter
	 */
	public void setEndCharacter(int endCharacter) {
		this.endCharacter = endCharacter;
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
}
