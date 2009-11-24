package org.jopac2.jbal.subject;

import java.util.Vector;

import org.jopac2.jbal.abstractStructure.Field;
import org.jopac2.jbal.abstractStructure.Tag;

/*
606 TOPICAL NAME USED AS SUBJECT

Field Definition

This field contains a common noun or noun phrase used as a subject heading.

Occurrence

Optional. Repeatable.

Indicators

Indicator 1: Level of the Subject Item

The first indicator is used to distinguish primary and secondary descriptors. A term is considered primary (value '1') if it covers the main focus or subject of the material. A term covering a less important aspect is considered secondary (value '2'). Value '0' is used when no decision has been made as to whether the term is primary or secondary.

0 No level specified
1 Primary term
2 Secondary term
# No information available

Indicator 2: blank (not defined)

Subfields

$a Entry Element
The term in the form prescribed by the system of subject headings used. Not repeatable.

$j Form Subdivision
A term added to the subject heading to further specify the kind(s) or genre(s) or material (EX 6, 7). Agencies not using this subdivision should use $x instead. Repeatable (EX 8).

$x Topical Subdivision
A term added to the topical heading to specify the aspect that the subject heading represents (EX 2, 3). Repeatable.

$y Geographical Subdivision
A term added to the topical heading to specify a place in relation to the topic that the subject heading represents (EX 2, 4). Repeatable.

$z Chronological Subdivision
A term added to the topical heading to specify the period of time in relation to the topic that the subject heading represents (EX 5). Repeatable.

$2 System Code
An identification in coded form of the system from which the subject heading is derived. It is recommended that subfield $2 always be present in each occurrence of the field. For a list of system codes, see Appendix G. Not repeatable.

$3 Authority Record Number
The control number for the authority record for the heading. This subfield is for use with UNIMARC/Authorities. Not repeatable.

Notes on Field Contents

This field contains data entered according to the provisions of the system of subject headings used.

Related Fields

607 GEOGRAPHICAL NAME USED AS SUBJECT
Field 607 is used instead of 606 when the subject heading is a geographical name.

Examples

EX 1
606 ##$aPulmonary artery$xCatheterization$xHandbooks, manuals, etc$21c
606 ##$aHemodynamic monitoring$xHandbooks, manuals, etc$21c
606 ##$aHeart Catheterization$xinstrumentation$xhandbooks$2mesh
606 ##$aHeart Catheterization$xinstrumentation$xnurses' instructions$2mesh
606 ##$aMonitoring, Physiologic$xhandbooks$2mesh
606 ##$aMonitoring, Physiologic$xnurses' instructions$2mesh

Both Library of Congress and Medical subject headings have been assigned to the record for Memory bank for hemodynamic monitoring : the pulmonary artery catheter. The record predates the use of $j for form subdivisions.

EX 2
606 0#$aScaffolding$xSafety measures$21c
606 0#$aConstruction equipment$yGreat Britain$21c
Subject headings assigned to the record for Safety in construction work, scaffolding by the Health and Safety Executive.

EX 3
606 0#$aRadioactivity$xSafety measures$21c

EX 4
606 0#$aTrees$yUnited States$21c

EX 5
606 0#$aArts, Modern$z20th century$21c

EX 6
606 1#$aBiology$xPeriodicals$21c
The item is about periodicals on biology.

EX 7
606 1#$aBiology$jPeriodicals$21c
The item is a periodical on biology.

EX 8
606 0#$aVocal music$jBibliography$jUnion lists$21c 
*/

public class TopicalNameSubject implements SubjectInterface {
	private Vector<Field> fields=new Vector<Field>();
	private char indicator1,indicator2;
	
	public void setData(Tag tag) {
		this.indicator1=tag.getModifier1();
		this.indicator2=tag.getModifier2();
		this.fields=tag.getFields();
	}
	
	public SubjectInterface clone() {
		SubjectInterface c=new TopicalNameSubject(indicator1);
		for(int i=0;fields!=null && i<fields.size();i++)
			c.setField(fields.elementAt(i)); // clone?
		return c;
	}
	
	public void setField(Field field) {
		fields.addElement(field);
	}
	
/**
 * Indicator 1: Level of the Subject Item
 * 
 * The first indicator is used to distinguish primary and secondary descriptors. A term is considered primary (value '1') if it covers the main focus or subject of the material. A term covering a less important aspect is considered secondary (value '2'). Value '0' is used when no decision has been made as to whether the term is primary or secondary.
 * 
 * 0 No level specified
 * 1 Primary term
 * 2 Secondary term
 * # No information available
 * 
 * Indicator 2: blank (not defined)
 */
	public TopicalNameSubject(char levelOfSubjectItem) {
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
		return "606";
	}

}
