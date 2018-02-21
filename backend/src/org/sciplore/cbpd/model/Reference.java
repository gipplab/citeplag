package org.sciplore.cbpd.model;

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import org.sciplore.preamble.License;

/**
* Container class for reference information.
*/

@License (author="Mario Lipinski")

public class Reference {
	int dbReferenceId;
	Document contDocument;
	String docReferenceId;
	Document refDocument;
	List<Citation> citations = new LinkedList<Citation>();
	
	public Reference() {
		dbReferenceId = this.hashCode();
	}
	
	public Reference(Document cont, String docRefId, Document ref) {
		this();
		this.contDocument = cont;
		this.docReferenceId = docRefId;
		this.refDocument = ref;
	}

	/**
	 * Returns the dbReferenceId.
	 * 
	 * @return the dbReferenceId
	 */
	@XmlAttribute(name="db_reference_id")
	public int getDbReferenceId() {
		return dbReferenceId;
	}

	/**
	 * Sets the dbReferenceId.
	 * 
	 * @param dbReferenceId the dbReferenceId
	 */
	public void setDbReferenceId(int dbReferenceId) {
		this.dbReferenceId = dbReferenceId;
	}

	/**
	 * Returns the contDocument.
	 * 
	 * @return the contDocument
	 */
	@XmlTransient
	public Document getContDocument() {
		return contDocument;
	}

	/**
	 * Sets the contDocument.
	 * 
	 * @param contDocument the contDocument
	 */
	public void setContDocument(Document contDocument) {
		this.contDocument = contDocument;
	}

	/**
	 * Returns the docReferenceId.
	 * 
	 * @return the docReferenceId
	 */
	@XmlAttribute(name="doc_reference_id")
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
	 * Returns the refDocument.
	 * 
	 * @return the refDocument
	 */
	@XmlElement(name="Document")
	public Document getRefDocument() {
		return refDocument;
	}

	/**
	 * Sets the refDocument.
	 * 
	 * @param refDocument the refDocument
	 */
	public void setRefDocument(Document refDocument) {
		this.refDocument = refDocument;
	}

	/**
	 * Returns the citations.
	 * 
	 * @return the citations
	 */
	@XmlTransient
	public List<Citation> getCitations() {
		return citations;
	}

	/**
	 * Sets the citations.
	 * 
	 * @param citations the citations
	 */
	public void setCitations(List<Citation> citations) {
		this.citations = citations;
	}
	
	
	/**
	 * Sets the citations.
	 * 
	 * @param c the citation
	 * 
	 */
	public void addCitation(Citation c) {
		this.citations.add(c);
	}
	
	public void addCitation(int character) {
		addCitation(new Citation(this.getContDocument(), this.getDocReferenceId(), this, character));
	}
	
}
