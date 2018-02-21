package org.sciplore.cbpd.model;

import javax.xml.bind.annotation.XmlTransient;

import org.sciplore.preamble.License;


/**
* Container class for document data.
*/

@License (author="Mario Lipinski")

public class DocumentData {
	Document document;
	String type;
	String value;
	
	public DocumentData() {
		
	}
	
	public DocumentData(Document doc, String type, String value) {
		this.document = doc;
		this.type = type;
		this.value = value;
	}
	
	public String toString() {
		return type + ": " + value;
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
	 * Returns the type.
	 * 
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets the type.
	 * 
	 * @param type the type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Returns the value.
	 * 
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Sets the value.
	 * 
	 * @param value the value
	 */
	public void setValue(String value) {
		this.value = value;
	}
}
