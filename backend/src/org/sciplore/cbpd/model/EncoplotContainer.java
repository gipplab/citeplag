package org.sciplore.cbpd.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.sciplore.preamble.License;

/**
* Container class for encoplot text matches.
*/

@License (author="Mario Lipinski")
@XmlRootElement

public class EncoplotContainer {
	List<Pattern> pattern = new ArrayList<Pattern>();
	List<TextpatternMember> textpatternMemberDoc1 = new ArrayList<TextpatternMember>();
	List<TextpatternMember> textpatternMemberDoc2 = new ArrayList<TextpatternMember>();
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
	/**
	 * Returns the textpatternMember.
	 * 
	 * @return the textpatternMember
	 */
	@XmlElement(name="textpattern_doc1")
	public List<TextpatternMember> getTextpatternMemberDoc1() {
		return textpatternMemberDoc1;
	}
	/**
	 * Sets the textpatternMember.
	 * 
	 * @param textpatternMemberDoc1 the textpatternMember for document 1
	 */
	public void setTextpatternMember(List<TextpatternMember> textpatternMemberDoc1) {
		this.textpatternMemberDoc1 = textpatternMemberDoc1;
	}
	
	
	/**
	 * Returns the textpatternMember.
	 * 
	 * @return the textpatternMember
	 */
	@XmlElement(name="textpattern_doc2")
	public List<TextpatternMember> getTextpatternMemberDoc2() {
		return textpatternMemberDoc2;
	}
	/**
	 * Sets the textpatternMember.
	 * 
	 * @param textpatternMemberDoc2 the textpatternMember for document 2
	 */
	public void setTextpatternMemberDoc2(List<TextpatternMember> textpatternMemberDoc2) {
		this.textpatternMemberDoc2 = textpatternMemberDoc2;
	}
	
	
}
