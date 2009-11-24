package org.jopac2.jbal.subject;

import java.util.Vector;

import org.jopac2.jbal.abstractStructure.Field;
import org.jopac2.jbal.abstractStructure.Tag;

/**
600 PERSONAL NAME USED AS SUBJECT

Field Definition

This field contains the name of a person who is one of the subjects of the item, in access point form, with the optional addition of extra subject information.

Occurrence

Optional. Repeatable.

Indicators

Indicator 1: blank (not defined)
Indicator 2: Form of Name Indicator
This indicator specifies whether the name is entered under the first occurring name (forename) or a name in direct order or whether it is entered under a surname, family name, patronymic or equivalent, usually with inversion.

0 Name entered under forename or in direct order (EX 3, 4)
1 Name entered under surname (family name, patronymic etc.) (EX 1, 2, 5)

Subfields

$a Entry Element
The portion of the name used as the entry element in the heading; that part of the name by which the name is entered in ordered lists. This subfield must be present if the field is present. Not repeatable.

$b Part of Name Other than Entry Element
The remainder of the name, used when the entry element is a surname or family name (EX 1, 2, 5). It contains forenames and other given names. The form of name indicator should be set to 1 when this subfield is used. Not repeatable.

$c Additions to Name Other than Dates
Any additions to names (other than dates) which do not form an integral part of the name itself including titles, epithets or indications of office. Repeatable for second and subsequent occurrences of such additions.

$d Roman Numerals
Roman numerals associated with names of certain popes, royalty and ecclesiastics. If an epithet (or a further forename) is associated with the numeration, this too should be included (EX 4). The form of name indicator should be set to 0 when this subfield is used. Not repeatable.

$f Dates
The dates attached to personal names together with abbreviations or other indications of the nature of the dates. Any indications of the type of date (e.g., flourished, born, died) should also be entered in the subfield in full or abbreviated form (EX 5). All the dates for the person named in the field should be entered in $f. Not repeatable.

$g Expansion of Initials of Forename
The full form of forenames when initials are recorded in subfield $b as the preferred form and when both initials and the full form are required. Not repeatable.

$j Form Subdivision
The description of this subfield can be found above the description of $x.

$p Affiliation/address
This subfield contains the institutional affiliation of the individual at the time the work was prepared. Not repeatable.

$t Title
Not used. For author/title subject headings, use field 604 NAME AND TITLE USED AS SUBJECT.

$j Form Subdivision
A term added to the subject heading to further specify the kind(s) or genre(s) of material (EX 2). Agencies not using this subdivision should use $x instead. Repeatable.

$x Topical Subdivision
A term added to a subject heading to further specify the topic the subject heading represents (EX 2, 3, 5). Repeatable.

$y Geographical Subdivision
A term added to a subject heading to specify a place in relation to a person which the subject heading represents (EX 3, 5). Repeatable.

$z Chronological Subdivision
A term added to a subject heading to specify the period in time in relation to a person whom the subject heading represents. Repeatable.

$2 System Code
An identification in coded form of the system or thesaurus from which the subject heading is derived. It is recommended that subfield $2 always be present in each occurrence of the field. For a list of system codes, see Appendix G. Not repeatable.

$3 Authority Record Number
The control number for the authority record for the heading. This subfield is for use with UNIMARC/Authorities. Not repeatable.

Notes on Field Contents

This field is intended for recording headings for personal names used as subjects. These headings are structured in the same form as the headings for persons responsible for the content of an item. Subfields $a, $b, $c, $d and $f follow the same form as in field 700 and further explanation of the scope and content of these subfields can be found there.

Unlike field 700, this field can contain more than the name of the person and additions to the name. Terms may be added to a subject heading to further specify it with respect to form, topic, place or time. These and the order of all the subfields follow the rules of the subject heading system or thesaurus used by the agency preparing the record.

Related Fields

601 CORPORATE BODY NAME USED AS SUBJECT
When a corporate body rather than a person is the subject, field 601 is used.

602 FAMILY NAME USED AS SUBJECT
When a family rather than a person is the subject, field 602 is used.

604 NAME AND TITLE USED AS SUBJECT
When the subject is an author/title, field 604 is used.

Examples

EX 1
600 #1$aBurroughs$bEdgar Rice$21c

EX 2
600 #1$aShakespeare$bWilliams$f564-1616$jQuotations$21c

EX 3
600 #0$aJesus Christ$xNativity$21c
600 #0$aJesus Christ$xTrial$21c
The record for Son of God : birth and trial of Jesus containing two subject headings.

EX 4
600 #0$aGustavus$dII Adolphus,$cKing of Sweden$21c

EX 5
600 #1$aEinstein$bAlbert$f1879-1955$xHomes and haunts$yGermany$yBerlin$2lc 
*/

public class PersonalNameSubject implements SubjectInterface {
	private Vector<Field> fields=new Vector<Field>();
	private char indicator1,indicator2;
	
	public void setData(Tag tag) {
		this.indicator1=tag.getModifier1();
		this.indicator2=tag.getModifier2();
		this.fields=tag.getFields();
	}
	
	public SubjectInterface clone() {
		SubjectInterface c=new PersonalNameSubject(indicator2=='1');
		for(int i=0;fields!=null && i<fields.size();i++)
			c.setField(fields.elementAt(i)); // clone?
		return c;
	}
	
	public void setField(Field field) {
		fields.addElement(field);
	}
	
/**
 * Indicator 1: blank (not defined)
 * Indicator 2: Form of Name Indicator
 * This indicator specifies whether the name is entered under the first occurring name (forename) or a name in direct order or whether it is entered under a surname, family name, patronymic or equivalent, usually with inversion.
 *
 * 0 Name entered under forename or in direct order (EX 3, 4)
 * 1 Name entered under surname (family name, patronymic etc.) (EX 1, 2, 5)
 */
	public PersonalNameSubject(boolean nameEnteredUnderSurname) {
		indicator1=' ';
		if(nameEnteredUnderSurname) indicator2='1';
		else indicator2='0';
	}
	
/**
 * Use null values if not applicable
 * 	
 * $a Entry Element
 * The portion of the name used as the entry element in the heading; that part of the name by which the name is entered in ordered lists. This subfield must be present if the field is present. Not repeatable.
 * 
 * $b Part of Name Other than Entry Element
 * The remainder of the name, used when the entry element is a surname or family name (EX 1, 2, 5). It contains forenames and other given names. The form of name indicator should be set to 1 when this subfield is used. Not repeatable.
 * 
 * $c Additions to Name Other than Dates
 * Any additions to names (other than dates) which do not form an integral part of the name itself including titles, epithets or indications of office. Repeatable for second and subsequent occurrences of such additions.
 * 
 * $d Roman Numerals
 * Roman numerals associated with names of certain popes, royalty and ecclesiastics. If an epithet (or a further forename) is associated with the numeration, this too should be included (EX 4). The form of name indicator should be set to 0 when this subfield is used. Not repeatable.
 * 
 * $f Dates
 * The dates attached to personal names together with abbreviations or other indications of the nature of the dates. Any indications of the type of date (e.g., flourished, born, died) should also be entered in the subfield in full or abbreviated form (EX 5). All the dates for the person named in the field should be entered in $f. Not repeatable.
 * 
 * $g Expansion of Initials of Forename
 * The full form of forenames when initials are recorded in subfield $b as the preferred form and when both initials and the full form are required. Not repeatable.
 * 
 * $j Form Subdivision
 * The description of this subfield can be found above the description of $x.
 * 
 * $p Affiliation/address
 * This subfield contains the institutional affiliation of the individual at the time the work was prepared. Not repeatable.
 * 
 * $t Title
 * Not used. For author/title subject headings, use field 604 NAME AND TITLE USED AS SUBJECT.
 * 
 * $j Form Subdivision
 * A term added to the subject heading to further specify the kind(s) or genre(s) of material (EX 2). Agencies not using this subdivision should use $x instead. Repeatable.
 * 
 * $x Topical Subdivision
 * A term added to a subject heading to further specify the topic the subject heading represents (EX 2, 3, 5). Repeatable.
 * 
 * $y Geographical Subdivision
 * A term added to a subject heading to specify a place in relation to a person which the subject heading represents (EX 3, 5). Repeatable.
 * 
 * $z Chronological Subdivision
 * A term added to a subject heading to specify the period in time in relation to a person whom the subject heading represents. Repeatable.
 * 
 * $2 System Code
 * An identification in coded form of the system or thesaurus from which the subject heading is derived. It is recommended that subfield $2 always be present in each occurrence of the field. For a list of system codes, see Appendix G. Not repeatable.
 * 
 * $3 Authority Record Number
 * The control number for the authority record for the heading. This subfield is for use with UNIMARC/Authorities. Not repeatable.
 * 
 */
	public void setSubjectData(String entryElement, String partOfNameOtherThanEntryElement, 
			String additionsToNameOtherThanDates, String romanNumerals,
			String dates, String expansionOfInitialsOfForename, String formSubdivision, 
			String affiliationOrAddress, String formSubdivision1, 
			String topicalSubdivision, String geographicalSubdivision, String chronologicalSubdivision, 
			String systemCode, String authorityRecordNumber) {
		setField("a",entryElement);
		setField("b",partOfNameOtherThanEntryElement);
		setField("c",additionsToNameOtherThanDates);
		setField("d",romanNumerals);
		setField("f",dates);
		setField("g",expansionOfInitialsOfForename);
		setField("j",formSubdivision);
		setField("p",affiliationOrAddress);
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
		return "600";
	}

}
