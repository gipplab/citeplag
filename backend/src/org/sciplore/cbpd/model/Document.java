package org.sciplore.cbpd.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.persistence.oxm.annotations.XmlPath;
import org.sciplore.preamble.License;

/**
* Container class for document metadata.
*/

@License (author="Mario Lipinski")
@XmlRootElement

public class Document {
	int documentId;
	List<Author> authors = new LinkedList<Author>();
	List<Citation> citations = new LinkedList<Citation>();
	List<DocumentData> data = new LinkedList<DocumentData>();
	DocumentText text = new DocumentText();
	DocumentText plain = new DocumentText();
	List<Pattern> pattern = new LinkedList<Pattern>();
	List<Reference> outReferences = new LinkedList<Reference>();
	List<Reference> inReferences = new LinkedList<Reference>();
	List<TextpatternMember> textpatternMember = new ArrayList<TextpatternMember>();
	List<CitationpatternMember> citationpatternMember = new ArrayList<CitationpatternMember>();
	
	public Document() {
		documentId = this.hashCode();
	}
	
	public boolean equals(Document d) {
		String title1 = "";
		String title2 = "";
		
		for (DocumentData data : this.getData()) {
			if (data.getType().equals("title")) {
				title1 = data.getValue();
			}
		}
		
		for (DocumentData data : d.getData()) {
			if (data.getType().equals("title")) {
				title2 = data.getValue();
			}
		}
		
		if (StringUtils.getLevenshteinDistance(title1, title2) < 20) {
			return true;
		} else {
			return false;
		}
	}
	
	public void addData(DocumentData data) {
		this.data.add(data);
	}
	
	public void addData(String type, String value) {
		DocumentData dData = new DocumentData(this, type, value);
		addData(dData);
	}

	/**
	 * Returns the documentId.
	 * 
	 * @return the documentId
	 */
	@XmlElement(name="document_id")
	public int getDocumentId() {
		return documentId;
	}

	/**
	 * Sets the documentId.
	 * 
	 * @param documentId the documentId
	 */
	public void setDocumentId(int documentId) {
		this.documentId = documentId;
	}


	/**
	 * Returns the authors.
	 * 
	 * @return the authors
	 */
	@XmlElementRef
	public List<Author> getAuthors() {
		return authors;
	}

	/**
	 * Sets the authors.
	 * 
	 * @param authors the authors
	 */
	public void setAuthors(List<Author> authors) {
		this.authors = authors;
	}

	public void addAuthor(String name) {
		this.authors.add(new Author(this, name));
	}

	public void addAuthor(String firstname, String lastname) {
		this.authors.add(new Author(this, firstname, lastname));
	}

	/**
	 * Returns the citations.
	 * 
	 * @return the citations
	 */
	@XmlElementWrapper(name="Citation")
	@XmlElement(name="Citation")
	public List<Citation> getCitations() {
		Map<Double, Citation> citations = new TreeMap<Double, Citation>();
		int k=0;
		for (Reference r : outReferences) {
			for (Citation c : r.getCitations()) {
				citations.put(c.getCharacter()+(k++/1000.), c);
			}
		}
		int i = 0;
		for (Citation c : citations.values()) {
			c.setCount(++i);
		}
		return new ArrayList<Citation>(citations.values());
	}

	/**
	 * Sets the citations.
	 * 
	 * @param citations the citations
	 */
	public void setCitations(List<Citation> citations) {
		this.citations = citations;
	}
	
	public void addCitation(String refId, Reference ref, int character) {
		this.citations.add(new Citation(this, refId, ref, character));
	}

	/**
	 * Returns the data.
	 * 
	 * @return the data
	 */
	public List<DocumentData> getData() {
		return data;
	}

	/**
	 * Sets the data.
	 * 
	 * @param data the data
	 */
	public void setData(List<DocumentData> data) {
		this.data = data;
	}

	/**
	 * Returns the text.
	 * 
	 * @return the text
	 */
	@XmlPath("Text/Text")
	public DocumentText getText() {
		return text;
	}

	/**
	 * Sets the text.
	 * 
	 * @param text the text
	 */
	public void setText(DocumentText text) {
		this.text = text;
	}

	public void setText(String text) {
		this.text = new DocumentText(this, text);
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
	
	
	public void addPattern(Pattern p) {
		this.pattern.add(p);
	}
	

	/**
	 * Returns the outReferences.
	 * 
	 * @return the outReferences
	 */
	@XmlElement(name="Reference")
	public List<Reference> getOutReferences() {
		return outReferences;
	}

	/**
	 * Sets the outReferences.
	 * 
	 * @param outReferences the outReferences
	 */
	public void setOutReferences(List<Reference> outReferences) {
		this.outReferences = outReferences;
	}

	/**
	 * Sets the outReferences.
	 * 
	 * 
	 */
	public void addOutReference(Document ref, String id) {
		this.outReferences.add(new Reference(this, id, ref));
	}

	/**
	 * Sets the outReferences.
	 * 
	 * 
	 */
	public void addOutReference(Reference ref) {
		this.outReferences.add(ref);
	}

	/**
	 * Returns the inReferences.
	 * 
	 * @return the inReferences
	 */
	public List<Reference> getInReferences() {
		return inReferences;
	}

	/**
	 * Sets the inReferences.
	 * 
	 * @param inReferences the inReferences
	 */
	public void setInReferences(List<Reference> inReferences) {
		this.inReferences = inReferences;
	}

	/**
	 * Returns the textpatternMember.
	 * 
	 * @return the textpatternMember
	 */
	@XmlElementWrapper(name="TextpatternMember")
	@XmlElement(name="TextpatternMember")
	public List<TextpatternMember> getTextpatternMember() {
		return textpatternMember;
	}

	/**
	 * Sets the textpatternMember.
	 * 
	 * @param textpatternMember the textpatternMember
	 */
	public void setTextpatternMember(List<TextpatternMember> textpatternMember) {
		this.textpatternMember = textpatternMember;
	}
	
	public void addTextpatternMember(TextpatternMember m) {
		this.textpatternMember.add(m);
	}	

	/**
	 * Returns the citationpatternMember.
	 * 
	 * @return the citationpatternMember
	 */
	@XmlElementWrapper(name="CitationpatternMember")
	@XmlElementRef
	public List<CitationpatternMember> getCitationpatternMember() {
		return citationpatternMember;
	}

	/**
	 * Sets the citationpatternMember.
	 * 
	 * @param citationpatternMember the citationpatternMember
	 */
	public void setCitationpatternMember(
			List<CitationpatternMember> citationpatternMember) {
		this.citationpatternMember = citationpatternMember;
	}
	
	public void addCitationpatternMember(CitationpatternMember m) {
		this.citationpatternMember.add(m);
	}
	
	public String toString(){
		return new String("DocId: "+this.documentId);
	}

	/**
	 * Returns the plain.
	 * 
	 * @return the plain
	 */
	@XmlTransient
	public DocumentText getPlain() {
		return plain;
	}

	/**
	 * Sets the plain.
	 * 
	 * @param plain the plain
	 */
	public void setPlain(DocumentText plain) {
		this.plain = plain;
	}
	
	public void setPlain(String text) {
		this.plain = new DocumentText(this, text);
	}

}