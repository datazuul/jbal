package org.jopac2.jbal.subject;

import java.util.Vector;

import org.jopac2.jbal.abstractStructure.Field;

/*
686 OTHER CLASS NUMBERS
Field Definition

This field contains class numbers from classification systems which are not internationally used but which are widely understood, published schemes.

Occurrence

Optional. Repeatable.

Indicators

Indicator 1: blank (not defined)
Indicator 2: blank (not defined)

Subfields

$a Class number
The class number taken from the classification scheme. Repeatable.

$b Book Number
The book number applied by the assigning agency. Repeatable.

$c Classification Subdivision
A subdivision of the class number taken from the classification scheme. Repeatable.

$2 System Code
A code for the classification scheme used in formulating the number. For a list of system codes, see Appendix G. Not repeatable.

Examples

EX 1
686 ##$aW1$bRE359$2usnlm
A U.S. National Library of Medicine class number.

EX 2
686 ##$a281.9$bC81A$2usnal
A U.S. National Agricultural Library class number. 
*/

public class OtherClassNumbers implements SubjectInterface {
	private Vector<Field> fields=new Vector<Field>();
	private char indicator1,indicator2;
	
/**
 * Indicator 1: blank (not defined)
 * Indicator 2: blank (not defined)
 */
	public OtherClassNumbers() {
		indicator1=' ';
		indicator2=' ';
	}
	
/**
 * Use null values if not applicable
 * 	
 * $a Class number
 * The class number taken from the classification scheme. Repeatable.
 * 
 * $b Book Number
 * The book number applied by the assigning agency. Repeatable.
 * 
 * $c Classification Subdivision
 * A subdivision of the class number taken from the classification scheme. Repeatable.
 * 
 * $2 System Code
 * A code for the classification scheme used in formulating the number. For a list of system codes, see Appendix G. Not repeatable.
 *  
 */
	public void setSubjectData(String classNumber, String bookNumber, String classificationSubdivision, String systemCode) {
		setField("a",classNumber);
		setField("b",bookNumber);
		setField("c",classificationSubdivision);
		setField("2",systemCode);
	}
	
	private void setField(String n, String c) {
		if(n!=null && c!=null && c.length()>0) fields.addElement(new Field(n,c));
	}

	public Vector<Field> getData() {
		return fields;
	}

	public char getIndicator1() {
		return indicator1;
	}

	public char getIndicator2() {
		return indicator2;
	}

	public String getTagIdentifier() {
		return "686";
	}

}
