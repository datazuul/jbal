package org.jopac2.jbal.subject;

import java.util.Vector;

import org.jopac2.jbal.abstractStructure.Field;

/*
661 TIME PERIOD CODE

Field Definition

An indication of the date covered by the work, coded according to the Time Period Code (formerly called the Chronological Coverage Code).

Occurrence

Repeatable for each period covered when an item deals with a number of distinct periods (EX 1).

Indicators

Indicator 1: blank (not defined)
Indicator 2: blank (not defined)

Subfields

$a Time Period Code
It consists of four alphanumeric characters. Not repeatable.

Notes on Field Contents

In the absence of an international coding scheme for time period codes, the scheme used in the USMARC format (details in Appendix E) should be used. The code is not used for prehistoric dates, e.g. geological eras.

Related Fields

122 CODED DATA FIELD: TIME PERIOD OF ITEM CONTENT
This field contains a formatted indication of the period covered by the item. It holds greater detail.

Examples

See also the examples in Appendix E.

EX 1
661 ##$aw3x0
661 ##$ad5d3
The codes (the 19th century and Greek eras) for "The Victorians and Ancient Greece" by Richard Jenkyns.

EX 2
661 ##$ad6d6
The codes for ca 300 B.C. As only one date is involved, the code is repeated to create the four characters.

EX 3
661 ##$ax-x-
The codes for a book on the 20th century. */

public class TimePeriodCode implements SubjectInterface {
	private Vector<Field> fields=new Vector<Field>();
	private char indicator1,indicator2;
	
/**
 * Indicator 1: blank (not defined)
 * Indicator 2: blank (not defined)
 */
	public TimePeriodCode() {
		indicator1=' ';
		indicator2=' ';
	}
	
/**
 * Use null values if not applicable
 * 	
 * $a Time Period Code
 * It consists of four alphanumeric characters. Not repeatable.
 *  
 */
	public void setSubjectData(String timePeriodCode) {
		setField("a",timePeriodCode);
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
		return "661";
	}

}
