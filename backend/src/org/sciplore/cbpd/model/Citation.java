package org.sciplore.cbpd.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.sciplore.preamble.License;
/**
* Container class for citation information.
*/

@License (author="Mario Lipinski")
@XmlRootElement(name="Citation")

public class Citation implements Comparable<Object> {
	int dbCitationId;
	Document document;
	String docReferenceId;
	Reference reference;
	int count;
	int character;
	int word;
	int sentence;
	int paragraph;
	int section;

	public Citation() {
		dbCitationId = this.hashCode();
	}

	public Citation(Document doc, String refId, Reference ref, int character) {
		this();
		this.document = doc;
		this.docReferenceId = refId;
		this.reference = ref;
		this.character = character;
	}

	/**
	 * Returns the dbCitationId.
	 * 
	 * @return the dbCitationId
	 */
	@XmlElement(name="db_citation_id")
	public int getDbCitationId() {
		return dbCitationId;
	}

	/**
	 * Sets the dbCitationId.
	 * 
	 * @param dbCitationId the dbCitationId
	 */
	public void setDbCitationId(int dbCitationId) {
		this.dbCitationId = dbCitationId;
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
	 * Returns the docReferenceId.
	 * 
	 * @return the docReferenceId
	 */
	@XmlTransient
	public String getDocReferenceId() {
		return docReferenceId;
	}

	/**
	 * Sets the docReferenceId.
	 * 
	 * @param docReferenceId the docReferenceId
	 */
	public void setDocReferenceId(String docReferenceId) {
		this.docReferenceId = docReferenceId;
	}

	/**
	 * Returns the reference.
	 * 
	 * @return the reference
	 */
	@XmlTransient
	public Reference getReference() {
		return reference;
	}

	/**
	 * Sets the reference.
	 * 
	 * @param reference the reference
	 */
	public void setReference(Reference reference) {
		this.reference = reference;
	}

	@XmlElement(name="db_reference_id")
	public int getReferenceId() {
		return reference.getDbReferenceId();
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
	 * Returns the character.
	 * 
	 * @return the character
	 */
	public int getCharacter() {
		return character;
	}

	/**
	 * Sets the character.
	 * 
	 * @param character the character
	 */
	public void setCharacter(int character) {
		this.character = character;
	}

	/**
	 * Returns the word.
	 * 
	 * @return the word
	 */
	public int getWord() {
		return word;
	}

	/**
	 * Sets the word.
	 * 
	 * @param word the word
	 */
	public void setWord(int word) {
		this.word = word;
	}

	/**
	 * Returns the sentence.
	 * 
	 * @return the sentence
	 */
	public int getSentence() {
		return sentence;
	}

	/**
	 * Sets the sentence.
	 * 
	 * @param sentence the sentence
	 */
	public void setSentence(int sentence) {
		this.sentence = sentence;
	}

	/**
	 * Returns the paragraph.
	 * 
	 * @return the paragraph
	 */
	public int getParagraph() {
		return paragraph;
	}

	/**
	 * Sets the paragraph.
	 * 
	 * @param paragraph the paragraph
	 */
	public void setParagraph(int paragraph) {
		this.paragraph = paragraph;
	}

	/**
	 * Returns the section.
	 * 
	 * @return the section
	 */
	public int getSection() {
		return section;
	}

	/**
	 * Sets the section.
	 * 
	 * @param section the section
	 */
	public void setSection(int section) {
		this.section = section;
	}

	public String toString(){
		return this.reference.getDocReferenceId();
	}

	public boolean matches (Citation c){
		if (this.reference.getRefDocument().getDocumentId() == c.getReference().getRefDocument().getDocumentId())
			return true;
		else return false;
	}
	 
	@Override
	public int compareTo(Object comp) {
		if (comp.getClass().equals(this.getClass())) {
			if (this.getCount() > (((Citation) comp).getCount()))
				return 1;
			else if (this.getCount() == (((Citation) comp).getCount()))
				return 0;
			else
				return -1;
		} else
			return 0;
	}
}
