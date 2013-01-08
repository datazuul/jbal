package org.jopac2.jbal.subject;

import java.util.Vector;

import org.jopac2.jbal.abstractStructure.Field;
import org.jopac2.jbal.abstractStructure.Tag;

/*
676 DEWEY DECIMAL CLASSIFICATION

Field Definition

This field contains a class number applied to the item being recorded according to the Dewey Decimal Classification.

Occurrence

Optional. Repeatable.

Indicators

Indicator 1: blank (not defined)
Indicator 2: blank (not defined)

Subfields

$a Number
The number as taken from the Dewey Decimal Classification schedules. Prime marks are indicated by /. Not repeatable.

$v Edition
The number of the edition used (EX 1-7). An 'a' is added to the number to indicate abridged edition (EX 6). Not repeatable.

$z Language of edition
The language in coded form of the edition from which the number in subfield $a is taken (EX 7). For codes see Appendix A. Not repeatable.

Notes on Field Contents

The number is entered in subfield $a in the form prescribed by the schedules used by the agency preparing the record. The number may include prime marks (/), which indicate internationally agreed points at which the number may be truncated. The number should not include extensions used solely to assign a bookmark to an individual item.

Subfield $z should be used only if the translated version contains differences from the original, e.g. when parts of the schedule have been rewritten to cover local requirements.

Examples

EX 1
676 ##$a943.0840924$v19
The class number for a book entitled Kaiser Wilhelm II : new interpretations, a collection of biographical essays on Wilhelm II, Emperor of Germany, 1890-1917. The item is classified according to the nineteenth edition of the schedules.

EX 2
676 ##$a823.912$v19
The class number for a book entitled Paddington at the station, a children's storybook. The item is classified according to the nineteenth edition of the Dewey Decimal Classification schedules.

EX 3
676 ##$a823/.912$v19
The class number for the same work where the agency uses prime marks.

EX 4
676 ##$a001.64/092/2$v19
A class number for a book entitled "Computer pioneers".

EX 5
676 ##$aA823/.2$v19
The class number for a book of Australian fiction of the period 1890-1945. The use of A823.2 to distinguish it from 823.2 (which indicates British fiction of the period 1400-1558) is allowed by the schedules.

EX 6
676 ##$a629.132$v13a
A class number for a book entitled Pilot's weather: the commonsense approach to meteorology. The item is classified according to the 13th Abridged edition. The 21st, full, edition would class the book at 629.1324.

EX 7
676 ##$a944/.0252$v21$zfre
A class number from the French 21st edition for a book on France at the time of Philip le Bon. The main edition has no subdivisions of 944 beyond 025 (Period of the House of Valois); the French edition has a number for each monarch. 
*/

public class DeweyDecimalClassification implements SubjectInterface {
	private Vector<Field> fields=new Vector<Field>();
	private char indicator1,indicator2;
	
	public void setData(Tag tag) {
		this.indicator1=tag.getModifier1();
		this.indicator2=tag.getModifier2();
		this.fields=tag.getFields();
	}
	
/**
 * Indicator 1: blank (not defined)
 * Indicator 2: blank (not defined)
 */
	public DeweyDecimalClassification() {
		indicator1=' ';
		indicator2=' ';
	}
	
	public SubjectInterface clone() {
		SubjectInterface c=new DeweyDecimalClassification();
		for(int i=0;fields!=null && i<fields.size();i++)
			c.setField(fields.elementAt(i)); // clone?
		return c;
	}
	
	public void setField(Field field) {
		fields.addElement(field);
	}
	
/**
 * Use null values if not applicable
 * 	
 * $a Number
 * The number as taken from the Dewey Decimal Classification schedules. Prime marks are indicated by /. Not repeatable.
 * 
 * $v Edition
 * The number of the edition used (EX 1-7). An 'a' is added to the number to indicate abridged edition (EX 6). Not repeatable.
 * 
 * $z Language of edition
 * The language in coded form of the edition from which the number in subfield $a is taken (EX 7). For codes see Appendix A. Not repeatable.
 *  
 */
	public void setSubjectData(String number, String edition, String languageOfEdition) {
		setField("a",number);
		setField("v",edition);
		setField("z",languageOfEdition);
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
		return "676";
	}

}
