package org.jopac2.jbal.subject;

import java.util.Vector;

import org.jopac2.jbal.abstractStructure.Field;
import org.jopac2.jbal.abstractStructure.Tag;

/**
615 SUBJECT CATEGORY (PROVISIONAL)
Field Definition

This field contains a higher level subject category in coded and/or textual form.

Occurrence

Optional. Repeatable.

Indicators

Indicator 1: blank (not defined)
Indicator 2: blank (not defined)

Subfields

$a Subject Category Entry Element Text
The term in the form prescribed by the system of subject categories used (EX 3, 4). Not repeatable.

$x Subject Category Subdivision Text
The term added to the subject category to specify a particular aspect of the subject category in the $a subfield. Repeatable.

$n Subject Category Code
A coded representation of a subject category (EX 1, 2, 4). Repeatable.

$m Subject Category Subdivision Code
A coded representation of a subject category subdivision (EX 2). Repeatable.

$2 System Code
An identification in coded form of the system from which the subject category is derived. For a list of system codes, see Appendix G. Not repeatable. It is recommended that subfield $2 always be present in each occurrence of the field.

$3 Authority Record Number
The control number for the authority record for the category. Not repeatable.

Notes on Field Contents

This field may contain data entered according to the provisions of the system of subject categories used. It may contain the category in textual form, coded form, or both forms.

Examples

EX 1
615 ##$nK800$2agris

EX 2
615 ##$nZ1$m.630$2mesh

EX 3
615 ##$aFuture$2liv

EX 4
615 ##$aComputer programming and software$n7372 
*/

public class SubjectCategory_Provisional implements SubjectInterface {
	private Vector<Field> fields=new Vector<Field>();
	private char indicator1,indicator2;
	
	public void setData(Tag tag) {
		this.indicator1=tag.getModifier1();
		this.indicator2=tag.getModifier2();
		this.fields=tag.getFields();
	}
	
	public SubjectInterface clone() {
		SubjectInterface c=new SubjectCategory_Provisional();
		for(int i=0;fields!=null && i<fields.size();i++)
			c.setField(fields.elementAt(i)); // clone?
		return c;
	}
	
	public void setField(Field field) {
		fields.addElement(field);
	}
/**
 * No indicators
 */
	public SubjectCategory_Provisional() {
		indicator1=' ';
		indicator2=' ';
	}
	
/**
 * Use null values if not applicable
 * 	
 * $a Subject Category Entry Element Text
 * The term in the form prescribed by the system of subject categories used (EX 3, 4). Not repeatable.
 * 
 * $x Subject Category Subdivision Text
 * The term added to the subject category to specify a particular aspect of the subject category in the $a subfield. Repeatable.
 * 
 * $n Subject Category Code
 * A coded representation of a subject category (EX 1, 2, 4). Repeatable.
 * 
 * $m Subject Category Subdivision Code
 * A coded representation of a subject category subdivision (EX 2). Repeatable.
 * 
 * $2 System Code
 * An identification in coded form of the system from which the subject category is derived. For a list of system codes, see Appendix G. Not repeatable. It is recommended that subfield $2 always be present in each occurrence of the field.
 * 
 * $3 Authority Record Number
 * The control number for the authority record for the category. Not repeatable.
 * 
 */
	public void setSubjectData(String subjectCategoryEntryElementText, 
			String subjectCategorySubdivisionText, String subjectCategoryCode, String subjectCategorySubdivisionCode, 
			String systemCode, String authorityRecordNumber) {
		setField("a",subjectCategoryEntryElementText);
		setField("x",subjectCategorySubdivisionText);
		setField("n",subjectCategoryCode);
		setField("m",subjectCategorySubdivisionCode);
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
		return "615";
	}

}
