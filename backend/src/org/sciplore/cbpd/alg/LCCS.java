package org.sciplore.cbpd.alg;

import static java.lang.Math.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.sciplore.cbpd.main.DetectionHelper;
import org.sciplore.cbpd.model.Citation;
import org.sciplore.cbpd.model.CitationpatternMember;
import org.sciplore.cbpd.model.Document;
import org.sciplore.cbpd.model.Pattern;
import org.sciplore.preamble.License;

/**
* Computes the Longest Common Citation Sequence (LCCS) of two documents.<br> 
* Code was derived from LCS algorithm described in the <a href="http://en.wikipedia.org/wiki/Longest_common_subsequence_problem">Wikipedia</a>.<br> 
* Original author: jhess
*/

@License (author="Norman Meuschke")

public class LCCS {
	private int[][] c;
	private Document doc1, doc2;
	private ArrayList<DiffEntry<Citation>> diff;
	private ArrayList<ArrayList<Citation>> backtrackBoth;
	private ArrayList<Citation> backtrack1;
	private ArrayList<Citation> backtrack2;

	public LCCS(Document d1, Document d2) {
		this.doc1 = d1;
		this.doc2 = d2;	
	}

	public List<Pattern> getLccsPattern() {
		/*Compute the LCCS and get the citations involved in both documents*/
		ArrayList<ArrayList<Citation>> bothLccs = new ArrayList<ArrayList<Citation>> (this.getLccs());
		//		System.err.println(bothLccs.get(0));
		//		System.err.println(bothLccs.get(1));

		/*No LCCS detected*/
		if(bothLccs.get(0).size()==0)
			return new ArrayList<Pattern>();

		/*LCCS>0 detected*/
		else{
			Pattern pat = new Pattern(doc1, doc2, "1", bothLccs.get(0).size());
			Integer[] citGaps;
			int cnt;

			/*Add CitationpatternMembers to document 1*/
			citGaps = new DetectionHelper().getCitationPatternGaps(bothLccs.get(0));  
			cnt=1;

			for (Citation c1 : bothLccs.get(0)) {
				doc1.addCitationpatternMember(new CitationpatternMember(pat, cnt, doc1, citGaps[cnt-1], c1));
				cnt++;
			}

			/*Add CitationpatternMembers to document 2*/
			citGaps = new DetectionHelper().getCitationPatternGaps(bothLccs.get(1));
			cnt = 1;

			for (Citation c2 : bothLccs.get(1)) { 
				doc2.addCitationpatternMember(new CitationpatternMember(pat, cnt, doc2, citGaps[cnt-1], c2));
				cnt++;
			}

			/*Create and return pattern*/	
			ArrayList<Pattern> result = new ArrayList<Pattern>();
			result.add(pat);
			return result;
		}
	}

	public ArrayList<ArrayList<Citation>> getLccs() {
		findLccs();
		this.backtrackBoth = new ArrayList<ArrayList<Citation>>();
		this.backtrack1 = new ArrayList<Citation>();
		this.backtrack2 = new ArrayList<Citation>();
		backtrack(getLengthOfSeq1(), getLengthOfSeq2());
		backtrackBoth.add(backtrack1);
		backtrackBoth.add(backtrack2);

		return this.backtrackBoth;
	}

	public List<DiffEntry<Citation>> getLccsDiff() {
		findLccs();

		if (this.diff == null) {
			this.diff = new ArrayList<DiffEntry<Citation>>();
			diff(getLengthOfSeq1(), getLengthOfSeq2());
		}
		return this.diff;
	}

	public int getLccsLength() {
		findLccs();

		return c[getLengthOfSeq1()][getLengthOfSeq2()];
	}

	public int getMinEditDistance() {
		findLccs();
		return getLengthOfSeq1() + getLengthOfSeq2() - 2 * abs(getLccsLength());
	}

	public Document getDoc1(){
		return this.doc1;
	}

	public Document getDoc2(){
		return this.doc2;
	}

	@Override
	public String toString() {
		findLccs();

		StringBuffer buf = new StringBuffer();
		buf.append("  ");
		for (int j = 1; j <= getLengthOfSeq2(); j++) {
			buf.append(valueOfC2Internal(j));
		}
		buf.append("\n");
		buf.append(" ");
		for (int j = 0; j < c[0].length; j++) {
			buf.append(Integer.toString(c[0][j]));
		}
		buf.append("\n");
		for (int i = 1; i < c.length; i++) {
			buf.append(getValueOfC1Internal(i));
			for (int j = 0; j < c[i].length; j++) {
				buf.append(Integer.toString(c[i][j]));
			}
			buf.append("\n");
		}
		return buf.toString();
	}

	private void backtrack(int i, int j) {
		findLccs();

		if (i == 0 || j == 0) {
			return;
		} else if (isEqual(i, j)) {
			backtrack(i - 1, j - 1);
			backtrack1.add(getValueOfC1Internal(i));
			backtrack2.add(valueOfC2Internal(j));
		} else {
			if (c[i][j - 1] > c[i - 1][j]) {
				backtrack(i, j - 1);
			} else {
				backtrack(i - 1, j);
			}
		}
	}

	private void diff(int i, int j) {
		findLccs();

		while (!(i == 0 && j == 0)) {
			if (i > 0 && j > 0 && isEqual(i, j)) {
				this.diff.add(new DiffEntry<Citation>(DiffType.NONE,
						getValueOfC1Internal(i)));
				i--;
				j--;

			} else {
				if (j > 0 && (i == 0 || c[i][j - 1] >= c[i - 1][j])) {
					this.diff.add(new DiffEntry<Citation>(DiffType.ADD,
							valueOfC2Internal(j)));
					j--;

				} else if (i > 0 && (j == 0 || c[i][j - 1] < c[i - 1][j])) {

					this.diff.add(new DiffEntry<Citation>(DiffType.REMOVE,
							getValueOfC1Internal(i)));
					i--;
				}
			}
		}

		Collections.reverse(this.diff);
	}

	private boolean equals(Citation x1, Citation y1) {
		if(null == x1 && null == y1)
			return false;
		if (x1.getReference().getRefDocument().getDocumentId()==y1.getReference().getRefDocument().getDocumentId())
			return true;
		else
			return false;
	}

	private void findLccs() {
		if (c != null) {
			return;
		}
		c = new int[getLengthOfSeq1() + 1][];
		for (int i = 0; i < c.length; i++) {
			c[i] = new int[getLengthOfSeq2() + 1];
		}

		for (int i = 1; i < c.length; i++) {
			for (int j = 1; j < c[i].length; j++) {
				if (isEqual(i, j)) {
					c[i][j] = c[i - 1][j - 1] + 1;
				} else {
					c[i][j] = max(c[i][j - 1], c[i - 1][j]);
				}
			}
		}
	}

	private boolean isEqual(int i, int j) {
		return equals(getValueOfC1Internal(i), valueOfC2Internal(j));
	}

	private int getLengthOfSeq1() {
		return doc1.getCitations().size();
	}

	private int getLengthOfSeq2() {
		return doc2.getCitations().size();
	}


	private Citation getValueOfC1(int index) {
		return doc1.getCitations().get(index);
	}

	private Citation getValueOfC1Internal(int i) {
		return getValueOfC1(i - 1);
	}

	private Citation getValueOfC2(int index) {
		return doc2.getCitations().get(index);
	}

	private Citation valueOfC2Internal(int j) {
		return getValueOfC2(j - 1);
	}

	/**
	 * 
	 * Utility class for LCCS computation.
	 *
	 * @param <VALUE>
	 */
	public static class DiffEntry<VALUE> {

		private DiffType type;

		private VALUE value;

		public DiffEntry(DiffType type, VALUE value) {
			super();
			this.type = type;
			this.value = value;
		}

		public DiffType getType() {
			return type;
		}

		public VALUE getValue() {
			return value;
		}

		public void setType(DiffType type) {
			this.type = type;
		}

		public void setValue(VALUE value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return type.toString() + value.toString();
		}

	}

	public static enum DiffType {

		ADD("+", "add"),
		REMOVE("-", "remove"), 
		NONE(" ", "none");

		private String val;

		private String name;

		DiffType(String val, String name) {
			this.val = val;
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public String getVal() {
			return val;
		}


		@Override
		public String toString() {
			return val;
		}
	}
}
