package org.jopac2.jbal.subject;

import java.util.Vector;

import org.jopac2.jbal.abstractStructure.Field;

/*
675 UNIVERSAL DECIMAL CLASSIFICATION (UDC)

Field Definition

This field contains a class number applied to the item according to the Universal Decimal Classification scheme, with an indication of the edition being used.

Occurrence

Optional. Repeatable.

Indicators

Indicator 1: blank (not defined)
Indicator 2: blank (not defined)

Subfields

$a Number
The class number as taken from the UDC schedules. Not repeatable.

$v Edition
The number of the edition from which the number in subfield $a is taken. Not repeatable.

$z Language of edition
The language in coded form of the edition from which the number in subfield $a is taken. For codes see Appendix A. Not repeatable.

Notes on Field Contents

The number is taken from the UDC schedules used by the agency preparing the record. UDC is produced in various language versions each of which is revised from time to time and published as a new edition. Each published edition of UDC is authorized by the International Federation for Documentation (FID) and is available from national standards organizations, or, in countries where there is no national organization, from the International Organization for Standardization (ISO).

Examples

EX 1
675 ##$a633.13-155 (410) "18"$v4$zeng
The class number constructed from the schedules of the 4th English edition of UDC for Machinery for harvesting oats in Great Britain in the 19th century.

EX 2
675 ##$a681.3.04.071.8:025.3:05:07$v4$zeng
The class number constructed for Data elements essential to the interchange of serials records. 
*/

public class UniversalDecimalClassification implements SubjectInterface {
	private Vector<Field> fields=new Vector<Field>();
	private char indicator1,indicator2;
	
/**
 * Indicator 1: blank (not defined)
 * Indicator 2: blank (not defined)
 */
	public UniversalDecimalClassification() {
		indicator1=' ';
		indicator2=' ';
	}
	
/**
 * Use null values if not applicable
 * 	
 * $a Number
 * The class number as taken from the UDC schedules. Not repeatable.
 * 
 * $v Edition
 * The number of the edition from which the number in subfield $a is taken. Not repeatable.
 * 
 * $z Language of edition
 * The language in coded form of the edition from which the number in subfield $a is taken. For codes see Appendix A. Not repeatable.
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
		return "675";
	}

}
