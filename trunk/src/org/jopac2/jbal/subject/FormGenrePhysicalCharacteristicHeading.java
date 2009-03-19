package org.jopac2.jbal.subject;

import java.util.Vector;

import org.jopac2.jbal.abstractStructure.Field;

/*
608 FORM, GENRE OR PHYSICAL CHARACTERISTICS HEADING

Field Definition

This field contains a term or terms indicating the form, genre and/or physical characteristics of the item being described.

Occurrence

Optional. Repeatable.

Indicators

Indicator 1: blank (not defined)
Indicator 2: blank (not defined)

Subfields

$a Entry Element
The term in the form prescribed by the system of form headings used. Not repeatable.

$j Form Subdivision
A term added to the subject heading to further specify the kind(s) or genre(s) of material (EX 6). Agencies not using this subdivision should use $x instead. Repeatable.

$x Topical Subdivision
A term added to the form heading to specify the aspect that the heading represents. Repeatable.

$y Geographical Subdivision
A term added to the form heading to specify a place in relation to the topic that the heading represents. Repeatable.

$z Chronological Subdivision
A term added to the form heading to specify the period of time in relation to the topic that the heading represents. Repeatable.

$2 System Code
An identification in coded form of the system from which the form heading is derived. It is recommended that subfield 2 always be present in each occurrence of the field. Not repeatable.

$3 Authority Record Number
The control number for the authority record for the heading. This subfield is intended to be used with the UNIMARC Authorities Format. Not repeatable.

$5 Institution to which the Field Applies
Name of institution to which field applies in coded form. Since there are no internationally accepted codes, the codes from USMARC Code List for Organizations, which includes codes for many non-U.S. library agencies, are recommended. Otherwise, the full name of the agency or a national code may be used. If the institution holds more than one copy the subfield should also contain the shelfmark after a colon. Not repeatable.

Notes on Field Contents

This field contains data entered according to the provisions of the system of form headings used.

Related Fields

606 TOPICAL NAME USED AS SUBJECT
Field 606 contains a topical name used as subject.

Examples

EX 1
608 ##$aEmblem books$yGermany$zl7th century$2rbgenr
A form heading constructed according to Genre terms : thesaurus for use in rare book and special collections cataloging.

EX 2
608 ##$aDictionaries$xFrench$zl8th century$2rbgenr
The item is a French dictionary published in 1770.

EX 3
608 ##$aBritish marble papers (Paper)$yGermany$zl7th century$2rbpap
The term indicating physical characteristics of the item constructed according to Paper terms : a thesaurus for use in rare book and special collections cataloging.

EX 4
608##$aVellum bindings (Binding)$yItaly$zl6th century$2rbbin

EX 5
608##$aArmorial bindings (Provenance)$2rbprov$5UkCU
The term indicating physical characteristics of the copy in Cambridge University Library.

EX 6
608 ##$aChildren's stories$jPictorial works$21c

EX7
608 ##$aDetective and mystery stories$2gsafd 
*/

public class FormGenrePhysicalCharacteristicHeading implements SubjectInterface {
	private Vector<Field> fields=new Vector<Field>();
	private char indicator1,indicator2;
	
/**
 * Indicator 1: blank (not defined)
 * Indicator 2: blank (not defined)
 */
	public FormGenrePhysicalCharacteristicHeading(char levelOfSubjectItem) {
		indicator1=levelOfSubjectItem;
		indicator2=' ';
	}
	
/**
 * Use null values if not applicable
 * 	
 * $a Entry Element
 * The term in the form prescribed by the system of subject headings used. Not repeatable.
 * 
 * $j Form Subdivision
 * A term added to the subject heading to further specify the kind(s) or genre(s) or material (EX 6, 7). Agencies not using this subdivision should use $x instead. Repeatable (EX 8).
 * 
 * $x Topical Subdivision
 * A term added to the topical heading to specify the aspect that the subject heading represents (EX 2, 3). Repeatable.
 * 
 * $y Geographical Subdivision
 * A term added to the topical heading to specify a place in relation to the topic that the subject heading represents (EX 2, 4). Repeatable.
 * 
 * $z Chronological Subdivision
 * A term added to the topical heading to specify the period of time in relation to the topic that the subject heading represents (EX 5). Repeatable.
 * 
 * $2 System Code
 * An identification in coded form of the system from which the subject heading is derived. It is recommended that subfield $2 always be present in each occurrence of the field. For a list of system codes, see Appendix G. Not repeatable.
 * 
 * $3 Authority Record Number
 * The control number for the authority record for the heading. This subfield is for use with UNIMARC/Authorities. Not repeatable.
 * 
 */
	public void setSubjectData(String entryElement, String formSubdivision, 
			String topicalSubdivision, String geographicalSubdivision, String chronologicalSubdivision, 
			String systemCode, String authorityRecordNumber, String institutionToWhichTheFieldApplies) {
		setField("a",entryElement);
		setField("j",formSubdivision);
		setField("x",topicalSubdivision);
		setField("y",geographicalSubdivision);
		setField("z",chronologicalSubdivision);
		setField("2",systemCode);
		setField("3",authorityRecordNumber);
		setField("5",institutionToWhichTheFieldApplies);
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
		return "608";
	}

}
