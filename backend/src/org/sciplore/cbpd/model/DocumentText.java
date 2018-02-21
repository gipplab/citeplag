package org.sciplore.cbpd.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.sciplore.preamble.License;

/**
* Container class for the main text of a document.
*/

@License (author="Mario Lipinski")
@XmlRootElement(name="Text")

public class DocumentText {
	Document document;
	String fulltext;
	
	public DocumentText() {
	}
	
	public DocumentText(String txt) {
		fulltext = txt;
	}

	public DocumentText(Document doc, String txt) {
		this.document = doc;
		fulltext = txt;
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

	/**
	 * Returns the document.
	 * 
	 * @return the document
	 */
	@XmlElement(name="document_id")
	public int getDocumentId() {
		if (document == null) {
			return 0;
		} else {
			return document.getDocumentId();
		}
	}

	/**
	 * Returns the fulltext.
	 * 
	 * @return the fulltext
	 */
	public String getFulltext() {
		return fulltext;
	}

	/**
	 * Sets the fulltext.
	 * 
	 * @param fulltext the fulltext
	 */
	public void setFulltext(String fulltext) {
		this.fulltext = fulltext;
	}
}
