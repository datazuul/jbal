package org.jopac2.jbal.subject;

import java.util.Vector;

import org.jopac2.jbal.abstractStructure.Field;

/**
602 FAMILY NAME USED AS SUBJECT

Field Definition

This field contains the name of a family which is one of the subjects of the item, in access point form, with the optional addition of extra subject information.

Occurrence

Optional. Repeatable.

Indicators

Indicator 1: blank (not defined)
Indicator 2: blank (not defined)

Subfields

$a Entry Element
The name of the family in access point form. Not repeatable.

$f Dates
The dates of a family when they are required as part of the heading. Not repeatable (EX 2).

$j Form Subdivision
The description of this subfield can be found above the description of $x.

$t Title
Not used. For author/title subject headings, use field 604 NAME AND TITLE USED AS SUBJECT.

$j Form Subdivision
A term added to the subject heading to further specify the kind(s) or genre(s) of material (EX 2). Agencies not using this subdivision should use $x instead. Repeatable.

$x Topical Subdivision
A term added to a subject heading to further specify the topic the subject heading represents. Repeatable.

$y Geographical Subdivision
A term added to a subject heading to specify a place in relation to a family which the subject heading represents. Repeatable.

$z Chronological Subdivision
A term added to a subject heading to specify the period in time in relation to a family which the subject heading represents. Repeatable.

$2 System Code
An identification in coded form of the system from which the subject heading is derived. Not repeatable. It is recommended that subfield $2 always be present in each occurrence of the field.

$3 Authority Record Number
The control number for the authority record for the heading. This subfield is for use with UNIMARC/Authorities. Not repeatable.

Notes on Field Contents

This field is intended for recording headings for family names used as subjects. These headings are structured in the same form as name headings for a family responsible for the content of an item. Subfield $a includes the name of the family and any qualification such as '(Family)' or '(Clan)'. Such qualifications should retain their punctuation in the subfield.

This field can contain more than the name of the family in subfield $a. In addition, terms may be added to the subject heading to further specify it with respect to form, topic, place or time. These follow the rules of the subject heading system used.

Related Fields

600 PERSONAL NAME USED AS SUBJECT
When a person rather than a family name is the subject, field 600 is used.

601 CORPORATE BODY USED AS SUBJECT
When a corporate body rather than a family is the subject, field 601 is used.

604 NAME AND TITLE USED AS SUBJECT
When the subject is an author/title, field 604 is used.

Examples

EX 1
602 ##$aSwinnerton (Family)$jPeriodicals$21c
A subject heading assigned to a document entitled Swinnerton family history : heraldic and genealogical studies of the Swinnerton family.

EX 2
602 ##$aArchaemenid dynasty,$f559-330 B.C. 
*/

public class FamilyNameSubject implements SubjectInterface {
	private Vector<Field> fields=new Vector<Field>();
	private char indicator1,indicator2;
	
/**
 * No indicators
 */
	public FamilyNameSubject() {
		indicator1=' ';
		indicator2=' ';
	}
	
/**
 * Use null values if not applicable
 * 	
 * $a Entry Element
 * The name of the family in access point form. Not repeatable.
 * 
 * $f Dates
 * The dates of a family when they are required as part of the heading. Not repeatable (EX 2).
 * 
 * $j Form Subdivision
 * The description of this subfield can be found above the description of $x.
 * 
 * $t Title
 * Not used. For author/title subject headings, use field 604 NAME AND TITLE USED AS SUBJECT.
 * 
 * $j Form Subdivision
 * A term added to the subject heading to further specify the kind(s) or genre(s) of material (EX 2). Agencies not using this subdivision should use $x instead. Repeatable.
 * 
 * $x Topical Subdivision
 * A term added to a subject heading to further specify the topic the subject heading represents. Repeatable.
 * 
 * $y Geographical Subdivision
 * A term added to a subject heading to specify a place in relation to a family which the subject heading represents. Repeatable.
 * 
 * $z Chronological Subdivision
 * A term added to a subject heading to specify the period in time in relation to a family which the subject heading represents. Repeatable.
 * 
 * $2 System Code
 * An identification in coded form of the system from which the subject heading is derived. Not repeatable. It is recommended that subfield $2 always be present in each occurrence of the field.
 * 
 * $3 Authority Record Number
 * The control number for the authority record for the heading. This subfield is for use with UNIMARC/Authorities. Not repeatable.  * 
 */
	public void setSubjectData(String entryElement, 
			String dates, String formSubdivision, 
			String formSubdivision1, 
			String topicalSubdivision, String geographicalSubdivision, String chronologicalSubdivision, 
			String systemCode, String authorityRecordNumber) {
		setField("a",entryElement);
		setField("f",dates);
		setField("j",formSubdivision);
		setField("j",formSubdivision1);
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
		return "602";
	}

}
