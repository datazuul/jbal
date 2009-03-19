package org.jopac2.jbal.subject;

import java.util.Vector;

import org.jopac2.jbal.abstractStructure.Field;

/*
607 GEOGRAPHICAL NAME USED AS SUBJECT

Field Definition

This field contains a geographical name used as a subject heading.

Occurrence

Optional. Repeatable.

Indicators

Indicator 1: blank (not defined)
Indicator 2: blank (not defined)

Subfields

$a Entry Element
The geographical name in the form prescribed by the system of authority headings used. Not repeatable.

$j Form Subdivision
A term added to the subject heading to further specify the kind(s) or genre(s) of material (EX 5, 6). Agencies not using this subdivision should use $x instead. Repeatable.

$x Topical Subdivision
A term added to the geographical name to specify the aspect that the subject heading represents. Repeatable.

$y Geographical Subdivision
A term added to the geographical name to further specify a place that the subject heading represents (EX 5). Repeatable.

$z Chronological Subdivision
A term added to the geographical name to specify the period in time in relation to the name that the subject heading represents (EX 1, 2, 4). Repeatable.

$2 System Code
An identification in coded form of the system from which the subject heading is derived. It is recommended that subfield $2 always be present in each occurrence of the field. For a list of system codes, see Appendix G. Not repeatable.

$3 Authority Record Number
The control number for the authority record for the heading. This subfield is for use with UNIMARC/Authorities. Not repeatable.

Notes on Field Contents

This field will contain data entered according to the provisions of the system of subject headings used. Political jurisdictions represented by geographical names are entered in this field if they appear alone or subdivided only by subject subdivisions (EX 2, 4). Political jurisdictions subdivided by names of subordinate bodies are entered in field 601.

Related Fields

601 CORPORATE BODY NAME USED AS SUBJECT
Political jurisdictions subdivided by names of subordinate bodies used as subjects are entered in field 601.

660 GEOGRAPHIC AREA CODE
An indication of the region covered by the work is entered in coded form in field 660.

Examples

EX 1
607 ##$aEurope$xHistory$z476-1492$21c
607 ##$aEurope, Western$xHistory$21c
Geographical subject headings assigned to the record for Froissart's Chronicles.

EX 2
607 ##$aGreat Britain$xPolitics and government$z1660-1714$21c
A geographical subject heading assigned to the record for Macaulay's History of England.

EX 3
607 ##$aExmouth, Eng.$xSocial life and customs$21c
A geographical subject heading assigned to the record for Mrs Beer's house, by Patricia Beer.

EX 4
607 ##$aRome$xPolitics and government$z510-30 B.C.$21c
A geographical subject heading assigned to the record for A short guide to electioneering : Quintus Cicero's 'Commentariolum petitionis'.

EX 5
607 ##$aUnited States$xBoundaries$yCanada$jPeriodicals$21c

EX 6
607 ##$aEurope$jRoad maps$21c 
*/

public class GeographicalNameSubject implements SubjectInterface {
	private Vector<Field> fields=new Vector<Field>();
	private char indicator1,indicator2;
	
/**
 * Indicator 1: blank (not defined)
 * Indicator 2: blank (not defined)
 */
	public GeographicalNameSubject(char levelOfSubjectItem) {
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
			String systemCode, String authorityRecordNumber) {
		setField("a",entryElement);
		setField("j",formSubdivision);
		setField("x",topicalSubdivision);
		setField("y",geographicalSubdivision);
		setField("z",chronologicalSubdivision);
		setField("2",systemCode);
		setField("3",authorityRecordNumber);
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
		return "607";
	}

}
