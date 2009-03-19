package org.jopac2.jbal.subject;

import java.util.Vector;

import org.jopac2.jbal.abstractStructure.Field;

/*
605 TITLE USED AS SUBJECT

Field definition

This field contains a title which is one of the subjects of the item being recorded. This title may be the title of a work in any form of medium, e.g. stage plays, radio programmes, etc.

Occurrence

Optional. Repeatable.

Indicators

Indicator 1: blank (not defined)
Indicator 2: blank (not defined)

Subfields

$a Entry Element
The short title or title proper. Not repeatable.

$h Number of Section or Part
The number of a part when the item to which the title or uniform title refers is only a part of the work named in subfield $a. Repeatable for a subdivided part.

$i Name of Section or Part
The name of a part when the item to which the title or uniform title refers is only a part of the work named in subfield $a (EX 3). Repeatable for a subdivided part. (EX 3)

$j Form Subdivision
The description of this subfield can be found above the description of $x.

$k Date of Publication
The date of publication of the item as subject when it is necessary to add it to the uniform title to distinguish the item. Not repeatable.

$l Form Subheading
A standard phrase added to a heading to further specify the uniform title. Not repeatable.

$m Language (when part of heading)
The language of the item when required as part of the heading because it differs from that usually associated with the work named in the heading or when the work does not have a main language. If the work is in more than one language, both languages should be entered in a single $m. Not repeatable.

$n Miscellaneous Information
Any information not provided for in any other subfield. This includes a general material designation added to a title (EX 4, 5). Repeatable.

$q Version (or Date of Version)
An identification of the version of the work represented by the item; this may be the original date of the version. Not repeatable. (EX 6)

$r Medium of Performance (for Music)
The instrumentation, etc., of the item. Repeatable.

$s Numeric Designation (for Music)
A number assigned by the composer or others to distinguish works. The number may be the serial, opus or thematic index number or date used as a number. Repeatable.

$u Key (for Music)
The musical key used as part of the uniform title. Not repeatable.

$w Arranged Statement (for Music)
The statement that a musical work is an arrangement. Not repeatable.

$j Form Subdivision
A term added to the subject heading to further specify the kind(s) or genre(s) of material (EX 3, 6, 7). Agencies not using this subdivision should use $x instead. Repeatable.

$x Topical Subdivision
A term added to the title to specify the aspect that the subject heading represents (EX 2, 3, 6, 7). Repeatable.

$y Geographical Subdivision
A term added to a title to specify a place in relation to it that the subject heading represents. Repeatable.

$z Chronological Subdivision
A term added to a title to specify the period in time in relation to it that the subject heading represents. Repeatable.

$2 System Code
An identification in coded form of the system from which the subject heading is derived. It is recommended that subfield $2 always be present in each occurrence of the field. For a list of system codes, see Appendix G. Not repeatable.

$3 Authority Record Number
The control number for the authority record for the heading. This subfield is for use with UNIMARC/Authorities. Not repeatable.

Notes on Field Contents

This field will normally contain a uniform title, since most other titles assigned as subjects will be treated using 604 NAME AND TITLE USED AS SUBJECT. Any anonymous work which is the subject of the item being recorded will be entered here. Further information and examples on the contents of subfields for uniform titles are found at field 500. A title proper which is to be entered in this field should be entered in subfield $a including in that subfield any other title information as necessary.

Related Fields

604 NAME AND TITLE USED AS SUBJECT
When the subject is an author/title, field 604 is used.

Examples

EX 1
605 ##$aNSBThe NSEreporter$21c
The Library of Congress subject heading assigned to the record for Concerned about the planet : 'The reporter' magazine and American liberation, 1949-1968 by Martin K Doudna.

EX 2
605 ##$aBible$xAbstracting and indexing$21c
A subject heading assigned to a book about abstracting and indexing the Bible. For an item which is an index see EX 7.

EX 3
605 ##$aBible$iN.T.$iJohn XIIIXVII$jCommentaries$21c
A subject heading assigned to Love revealed : meditations on chapters 13-17 of the Gospel by John by George Bowen.

EX 4
605 ##$aNSBThe NSEArchers$n(Radio program)$21c
A subject heading assigned to a book entitled Forever Ambridge : thirty years of the Archers, dealing with the history of a serialized radio programme.

EX 5
605 ##$aEmpire strikes back$n(Motion picture)$21c
A subject heading assigned to the record of Once upon a galaxy : a journal of the making of 'The Empire Strikes back". The cataloguing agency omits leading articles (c.f. EX 4).

EX 6
605 ##$aAngloAmerican cataloguing rules$q2nd ed.$jCongresses$21c
The subject heading refers specifically to a particular edition of the work. The record in which the subject heading occurs is for Seminar on AACR 2 : proceedings of a seminar organised by the Cataloguing and Indexing Group of the Library Association. A topical subdivision is added.

EX 7
605 ##$aVariety$jIndexes$21c 
 */

public class TitleSubject implements SubjectInterface {
	private Vector<Field> fields=new Vector<Field>();
	private char indicator1,indicator2;
	
/**
 * No indicators
 */
	public TitleSubject() {
		indicator1=' ';
		indicator2=' ';
	}
	
/**
 * Use null values if not applicable
 * 	
 $a Entry Element
The short title or title proper. Not repeatable.

$h Number of Section or Part
The number of a part when the item to which the title or uniform title refers is only a part of the work named in subfield $a. Repeatable for a subdivided part.

$i Name of Section or Part
The name of a part when the item to which the title or uniform title refers is only a part of the work named in subfield $a (EX 3). Repeatable for a subdivided part. (EX 3)

$j Form Subdivision
The description of this subfield can be found above the description of $x.

$k Date of Publication
The date of publication of the item as subject when it is necessary to add it to the uniform title to distinguish the item. Not repeatable.

$l Form Subheading
A standard phrase added to a heading to further specify the uniform title. Not repeatable.

$m Language (when part of heading)
The language of the item when required as part of the heading because it differs from that usually associated with the work named in the heading or when the work does not have a main language. If the work is in more than one language, both languages should be entered in a single $m. Not repeatable.

$n Miscellaneous Information
Any information not provided for in any other subfield. This includes a general material designation added to a title (EX 4, 5). Repeatable.

$q Version (or Date of Version)
An identification of the version of the work represented by the item; this may be the original date of the version. Not repeatable. (EX 6)

$r Medium of Performance (for Music)
The instrumentation, etc., of the item. Repeatable.

$s Numeric Designation (for Music)
A number assigned by the composer or others to distinguish works. The number may be the serial, opus or thematic index number or date used as a number. Repeatable.

$u Key (for Music)
The musical key used as part of the uniform title. Not repeatable.

$w Arranged Statement (for Music)
The statement that a musical work is an arrangement. Not repeatable.

$j Form Subdivision
A term added to the subject heading to further specify the kind(s) or genre(s) of material (EX 3, 6, 7). Agencies not using this subdivision should use $x instead. Repeatable.

$x Topical Subdivision
A term added to the title to specify the aspect that the subject heading represents (EX 2, 3, 6, 7). Repeatable.

$y Geographical Subdivision
A term added to a title to specify a place in relation to it that the subject heading represents. Repeatable.

$z Chronological Subdivision
A term added to a title to specify the period in time in relation to it that the subject heading represents. Repeatable.

$2 System Code
An identification in coded form of the system from which the subject heading is derived. It is recommended that subfield $2 always be present in each occurrence of the field. For a list of system codes, see Appendix G. Not repeatable.

$3 Authority Record Number
The control number for the authority record for the heading. This subfield is for use with UNIMARC/Authorities. Not repeatable. 
 * 
 */
	public void setSubjectData(String entryElement,
			String numberOfSectionOrPart, String nameOfSectionOrPart, 
			String affiliationOrAddress, String formSubdivision, 
			String dateOfPublication, String formSubheading,
			String language, String miscellaneousInformation, 
			String versionOrDateOfVersion, 
			String mediumOfPerformance_Music, String numericDesignation_Music,
			String key_Music, String arrangedStatement_Music,
			String formSubdivision1, 
			String topicalSubdivision, String geographicalSubdivision, String chronologicalSubdivision, 
			String systemCode, String authorityRecordNumber) {
		setField("a",entryElement);
		setField("h",numberOfSectionOrPart);
		setField("i",nameOfSectionOrPart);
		setField("j",formSubdivision);
		setField("k",dateOfPublication);
		setField("l",formSubheading);
		setField("m",language);
		setField("n",miscellaneousInformation);
		setField("q",versionOrDateOfVersion);
		setField("r",mediumOfPerformance_Music);
		setField("s",numericDesignation_Music);
		setField("u",key_Music);
		setField("w",arrangedStatement_Music);
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
		return "605";
	}

}
