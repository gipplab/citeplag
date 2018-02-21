package org.sciplore.cbpd.model;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlValue;

import org.sciplore.preamble.License;
/**
* Container class for author information.
*/

@License (author="Mario Lipinski")
@XmlRootElement(name="Author")

public class Author {
	int id;
	Document document;
	String lastname;
	String firstname;
	
	public Author() {
		this.id = this.hashCode();
	}
	
	public Author(Document doc, String name) {
		this();
		this.document = doc;
		String[] names = name.split("\\s");
		for (int i=0; i < names.length-1; i++) {
			if(i == 0) {
				firstname = "";
			} else {
				firstname += " ";
			}
			firstname += names[i];
		}
		lastname = names[names.length-1];
	}
	
	public Author(Document doc, String firstname, String lastname) {
		this();
		this.document = doc;
		this.firstname = firstname;
		this.lastname = lastname;
	}

	/**
	 * Returns the id.
	 * 
	 * @return the id
	 */
	@XmlTransient
	public int getId() {
		return id;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id the id
	 */
	public void setId(int id) {
		this.id = id;
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
	 * Returns the lastname.
	 * 
	 * @return the lastname
	 */
	@XmlTransient
	public String getLastname() {
		return lastname;
	}

	/**
	 * Sets the lastname.
	 * 
	 * @param lastname the lastname
	 */
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	/**
	 * Returns the firstname.
	 * 
	 * @return the firstname
	 */
	@XmlTransient
	public String getFirstname() {
		return firstname;
	}

	/**
	 * Sets the firstname.
	 * 
	 * @param firstname the firstname
	 */
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	
	@XmlValue
	public String getName() {
		StringBuilder sb = new StringBuilder();
		if (firstname != null && !firstname.isEmpty()) {
			sb.append(firstname);
		}
		if (sb.length() > 0 && lastname != null && !lastname.isEmpty()) {
			sb.append(" ");
		}
		if (lastname != null && !lastname.isEmpty()) {
			sb.append(lastname);
		}
		return sb.toString();
	}
}
