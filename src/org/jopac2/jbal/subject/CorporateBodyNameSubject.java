package org.jopac2.jbal.subject;

import java.util.Vector;

import org.jopac2.jbal.abstractStructure.Field;

/*
601 CORPORATE BODY NAME USED AS SUBJECT

Field Definition

This field contains the name of a corporate body which is one of the subjects of the item, in access point form, with the optional addition of extra subject information.

Occurrence

Optional. Repeatable.

Indicators

Indicator 1: Meeting Indicator

The first indicator specifies whether the corporate body is a meeting or not. Meetings include conferences, symposia, etc. If the name of the meeting is a subdivision of the name of a corporate body, then the name is regarded as that of a corporate body (EX 10).

0 Corporate name
1 Meeting

If the source format does not distinguish meeting names from other corporate names, the indicator position should contain the fill character.

Indicator 2: Form of Name Indicator

The second indicator denotes the form of the corporate name as follows:

0 Name in inverted order
An inverted form may be used when the first word of a corporate name or meeting begins with an initial or forename relating to a personal name.

1 Name entered under place or jurisdiction
Used for corporate names relating to governments or other agencies of jurisdiction that are entered under the name of the place. According to certain cataloguing codes other kinds of institutions which are associated with a place are also entered under that place, e.g., universities, learned societies, art galleries (EX 9).

2 Name entered under name in direct order
Used for all other kinds of corporate names. (EX 1-8, 10).

Subfields

$a Entry Element
The portion of the name used as the entry element in the heading; that part of the name by which the name is entered in ordered lists; i.e. the part of the name up to the first filing boundary. This subfield is not repeatable but must be present if the field is present.

$b Subdivision (or name if entered under place)
The name of a lower level in a hierarchy when the name includes a hierarchy; or the name of the corporate body when it is entered under place (EX 9). This subfield excludes additions to the name added by the cataloguer to distinguish it from other institutions of the same name (see $c, $g, $h). Repeatable if there is more than one lower level in the hierarchy.

$c Addition to Name or Qualifier
Any addition to the name of the corporate body added by the cataloguer, other than number, place and date of conference. Repeatable. (EX 4, 5, 7, 8)

$d Number of Meeting and/or Number of Part of a Meeting
The number of a meeting when the meeting belongs to a numbered series. Not repeatable. (EX 10)

$e Location of Meeting
The place where a meeting was held when it is required as part of the heading. Not repeatable. (EX 10)

$f Date of Meeting
The date of a meeting when it is required as part of the heading. Not repeatable. (EX 10)

$g Inverted Element
Any part of the name of the corporate body which is removed from the beginning of the name in order to enter the body under a word which is more likely to be sought. Not repeatable.

$h Part of Name other than Entry Element and Inverted Element
In a heading with an inverted element, the part of the name following the inversion. Not repeatable.

$j Form Subdivision
The description of this subfield can be found above the description of $x.

$t Title
Not used. For author/title subject headings, use field 604 NAME AND TITLE USED AS SUBJECT.

$j Form Subdivision
A term added to the subject heading to further specify the kind(s) or genre(s) of material (EX 2, 3). Agencies not using this subdivision should use $x instead. Repeatable.

$x Topical Subdivision
A term added to a subject heading to further specify the topic the subject heading represents (EX 2, 3, 5, 6, 8). Repeatable (EX 2)

$y Geographical Subdivision
A term added to a subject heading to specify a place in relation to a corporate body which the subject heading represents. Repeatable. (EX 6)

$z Chronological Subdivision
A term added to a subject heading to specify the period in time in relation to a corporate body which the subject heading represents. Repeatable (EX 9).

$2 System Code
An identification in coded form of the system from which the subject heading is derived. It is recommended that subfield $2 always be present in each occurrence of the field. For a list of system codes, see Appendix G. Not repeatable.

$3 Authority Record Number
The control number for the authority record for the heading. This subfield is for use with UNIMARC/Authorities. Not repeatable.

Notes on Field Contents

This field is intended for recording headings for corporate names used as subjects. These headings are structured in the same form as corporate body name headings for bodies responsible for the contents of an item. Subfields $a, $b, $c, $d, $e, $f, $g and $h follow the same form as in field 710, and further explanation of the scope and content of these subfields can be found there.

Unlike field 710, this field can contain more than the name of the corporate body and additions to the name. Terms may be added to a subject heading to further specify it with respect to form, topic, place or time.

Political jurisdictions subdivided by names of subordinate bodies are entered in this field (EX 9). If the name of a political jurisdiction appears alone or is subdivided only by subject terms, it is entered in field 607.

Related Fields

600 PERSONAL NAME USED AS SUBJECT
When a person rather than a corporate body is the subject, field 600 is used.

602 FAMILY NAME USED AS SUBJECT
When a family rather than a corporate body is the subject, field 602 is used.

604 NAME AND TITLE USED AS SUBJECT
When the subject is an author/title, field 604 is used.

607 GEOGRAPHICAL NAME USED AS SUBJECT
Political jurisdictions represented by geographical names are entered in field 607 if they appear alone or subdivided only by subject terms.

Examples

EX 1
601 02$aHardy Heating Co Ltd$21c
A subject heading assigned to the record for Hardy Developments Ltd : test and cases in management accounting.

EX 2
601 02$aChurch of England.$xClergy.$jBiography$21c
A subject heading assigned to the record for Charles Lowder and the ritualistic movement (punctuation is retained in the example).

EX 3
601 02$aStrategic Arms Limitation Talks$xJuvenile literature$21c
A subject heading assigned to the record for a book for children entitled The nuclear arms race.

EX 4
601 02$aBeagle Expeditions$c1831-1836$21c
A subject heading assigned to the record for The adventures of Charles Darwin : a story of the Beagle voyage.

EX 5
601 02$aEgba$cAfrican tribe$xHistory$21c
A subject heading assigned to the record for Lugard and the Abeokuta uprising : the demise of Egba independence.

EX 6
601 02$aCatholic Church$yScotland$xGovernment$2 1c
A subject heading assigned to the record for Scotia pontificia: papal letters to Scotland before the Pontificate of Innocent III.

EX 7
601 02$aSpray$cShip$21c
A subject heading assigned to the record for In the wake of the Spray (the qualifier 'Ship' has been added since the name Spray is not distinctive).

EX 8
601 02$aTemplars$cOrder of chivalry$xHistory$21c
A subject heading assigned to the record for The Knights Templar.

EX 9
601 01$aGreat Britain$bManpower Services Commission$z1981-1985$21c
A subject heading assigned to the record for MSC corporate plan 1981, 1982, 1983, 1984, 1985.

EX 10
601 02$aUnited Nations$bConference on the Law of the Sea$d3rd$f1973-1975$eNew York, etc.$21c
A subject heading assigned to the record for documents on the third UN Conference on the Law of the Sea. 
*/

public class CorporateBodyNameSubject implements SubjectInterface {
	private Vector<Field> fields=new Vector<Field>();
	private char indicator1,indicator2;
	
/**
 * Indicator 1: Meeting Indicator
 * The first indicator specifies whether the corporate body is a meeting or not. Meetings include conferences, symposia, etc. If the name of the meeting is a subdivision of the name of a corporate body, then the name is regarded as that of a corporate body (EX 10).
 * 0 Corporate name
 * 1 Meeting
 * If the source format does not distinguish meeting names from other corporate names, the indicator position should contain the fill character.

 * Indicator 2: Form of Name Indicator
 * The second indicator denotes the form of the corporate name as follows:
 * 0 Name in inverted order
 *   An inverted form may be used when the first word of a corporate name or meeting begins with an initial or forename relating to a personal name.
 * 1 Name entered under place or jurisdiction
 *   Used for corporate names relating to governments or other agencies of jurisdiction that are entered under the name of the place. According to certain cataloguing codes other kinds of institutions which are associated with a place are also entered under that place, e.g., universities, learned societies, art galleries (EX 9).
 * 2 Name entered under name in direct order
 *   Used for all other kinds of corporate names. (EX 1-8, 10).
 */
	public CorporateBodyNameSubject(char meetingIndicator, char formOfNameIndicator) {
		indicator1=meetingIndicator;
		indicator2=formOfNameIndicator;
	}
	
/**
 * Use null values if not applicable
 * 	
 * $a Entry Element
 * The portion of the name used as the entry element in the heading; that part of the name by which the name is entered in ordered lists; i.e. the part of the name up to the first filing boundary. This subfield is not repeatable but must be present if the field is present.
 * 
 * $b Subdivision (or name if entered under place)
 * The name of a lower level in a hierarchy when the name includes a hierarchy; or the name of the corporate body when it is entered under place (EX 9). This subfield excludes additions to the name added by the cataloguer to distinguish it from other institutions of the same name (see $c, $g, $h). Repeatable if there is more than one lower level in the hierarchy.
 * 
 * $c Addition to Name or Qualifier
 * Any addition to the name of the corporate body added by the cataloguer, other than number, place and date of conference. Repeatable. (EX 4, 5, 7, 8)
 * 
 * $d Number of Meeting and/or Number of Part of a Meeting
 * The number of a meeting when the meeting belongs to a numbered series. Not repeatable. (EX 10)
 * 
 * $e Location of Meeting
 * The place where a meeting was held when it is required as part of the heading. Not repeatable. (EX 10)
 * 
 * $f Date of Meeting
 * The date of a meeting when it is required as part of the heading. Not repeatable. (EX 10)
 * 
 * $g Inverted Element
 * Any part of the name of the corporate body which is removed from the beginning of the name in order to enter the body under a word which is more likely to be sought. Not repeatable.
 * 
 * $h Part of Name other than Entry Element and Inverted Element
 * In a heading with an inverted element, the part of the name following the inversion. Not repeatable.
 * 
 * $j Form Subdivision
 * The description of this subfield can be found above the description of $x.
 * 
 * $t Title
 * Not used. For author/title subject headings, use field 604 NAME AND TITLE USED AS SUBJECT.
 * 
 * $j Form Subdivision
 * A term added to the subject heading to further specify the kind(s) or genre(s) of material (EX 2, 3). Agencies not using this subdivision should use $x instead. Repeatable.
 * 
 * $x Topical Subdivision
 * A term added to a subject heading to further specify the topic the subject heading represents (EX 2, 3, 5, 6, 8). Repeatable (EX 2)
 * 
 * $y Geographical Subdivision
 * A term added to a subject heading to specify a place in relation to a corporate body which the subject heading represents. Repeatable. (EX 6)
 * 
 * $z Chronological Subdivision
 * A term added to a subject heading to specify the period in time in relation to a corporate body which the subject heading represents. Repeatable (EX 9).
 * 
 * $2 System Code
 * An identification in coded form of the system from which the subject heading is derived. It is recommended that subfield $2 always be present in each occurrence of the field. For a list of system codes, see Appendix G. Not repeatable.
 * 
 * $3 Authority Record Number
 * The control number for the authority record for the heading. This subfield is for use with UNIMARC/Authorities. Not repeatable.
 * 
 */
	public void setSubjectData(String entryElement, String Subdivision, 
			String additionsToNameOrQualifier, String numberOfMeetingAndOrNumberOfPartOfAMeeting,
			String locationOfMeeting, String dateOfMeeting, String invertedElement, 
			String partOfNameOtherThanEntryElementAndInvertedElement, String formSubdivision, 
			String affiliationOrAddress, String formSubdivision1, 
			String topicalSubdivision, String geographicalSubdivision, String chronologicalSubdivision, 
			String systemCode, String authorityRecordNumber) {
		setField("a",entryElement);
		setField("b",Subdivision);
		setField("c",additionsToNameOrQualifier);
		setField("d",numberOfMeetingAndOrNumberOfPartOfAMeeting);
		setField("e",locationOfMeeting);
		setField("f",dateOfMeeting);
		setField("g",invertedElement);
		setField("h",partOfNameOtherThanEntryElementAndInvertedElement);
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
		return "601";
	}

}
