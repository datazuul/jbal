package org.jopac2.jbal.subject;

import java.util.Vector;

import org.jopac2.jbal.abstractStructure.Field;

/*
610 UNCONTROLLED SUBJECT TERMS

Field Definition

This field is used to record subject terms that are not derived from controlled subject heading lists.

Occurrence

Optional. Repeatable.

Indicators

Indicator 1: Level of the Subject Term

The first indicator is used to distinguish primary and secondary descriptors. A term is considered primary (value '1') if it covers the main focus or subject of the material. A term covering a less important aspect is considered secondary (value '2'). Value '0' is used when no decision is made as to whether the term is primary or secondary.

0 No level specified
1 Primary term
2 Secondary term

Indicator 2: blank (undefined)

Subfields

$a Subject Term
Repeatable when more than one term is assigned for the item.

Notes on Field Contents

Terms belonging to structured subject thesauri should be coded in fields 600607 with the appropriate $2 System Code.

Related Fields

600-607 Subject Heading fields

Examples

EX 1
610 1#$afuel cells$amolten carbonate$apower

EX 2
610 1#$amicrographics$aCOM$adata capture$acomputer-assisted retrieval

EX 3
610 2#$aKing, Donald W.$aWilliams, James G.$aNetworks, Topology$aPublic corporation

EX 4
610 1#$aCorporation for Open Systems$aOSI 
*/

public class UncontrolledSubjectTerms implements SubjectInterface {
	private Vector<Field> fields=new Vector<Field>();
	private char indicator1,indicator2;
	
/**
 * Indicator 1: Level of the Subject Term
 * 
 * The first indicator is used to distinguish primary and secondary descriptors. A term is considered primary (value '1') if it covers the main focus or subject of the material. A term covering a less important aspect is considered secondary (value '2'). Value '0' is used when no decision is made as to whether the term is primary or secondary.
 * 
 * 0 No level specified
 * 1 Primary term
 * 2 Secondary term
 * 
 * Indicator 2: blank (undefined)
 */
	public UncontrolledSubjectTerms(char levelOfSubjectTerm) {
		indicator1=levelOfSubjectTerm;
		indicator2=' ';
	}
	
/**
 * Use null values if not applicable
 * 	
 * $a Subject Term
 * Repeatable when more than one term is assigned for the item.
 *  
 */
	public void setSubjectData(String subjectTerm) {
		setField("a",subjectTerm);
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
		return "610";
	}

}
