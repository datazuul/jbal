package org.jopac2.jbal.subject;

import java.util.Vector;

import org.jopac2.jbal.abstractStructure.Field;
import org.jopac2.jbal.abstractStructure.Tag;

/*
660 GEOGRAPHIC AREA CODE

Field Definition

This field contains an indication of the region covered by the work, in coded form, according to the Library of Congress geographic area codes.

Occurrence

Optional. Repeatable for each region indicated.

Indicators

Indicator 1: blank (not defined)
Indicator 2: blank (not defined)

Subfield

$a Code
Geographic area code. Not repeatable. For codes see Appendix D.

Notes on Field Contents

The geographic area code was developed by the Library of Congress to facilitate retrieval of records by a geographic approach. The geographic area code is applied to an item when its text has a geographic orientation.

The coding scheme contains seven lower case alphabetic characters and/or hyphens and, as far as possible, provides a hierarchical breakdown of geographical and political entities.

When more than one geographic area code is assigned, each is entered in a separate field.

Related Fields

607 GEOGRAPHICAL NAME USED AS SUBJECT
The area code does not replace the geographical name used as subject, but may be included in the record in addition to it.

Examples

EX 1
660 ##$an-us-md

A geographic area code for a book entitled Crabs in the United States, concentrating on the state of Maryland.

EX 2
660 ##$ae-gx--
A geographic area code for a book entitled Popular songs of working-class culture relating to Germany.

EX 3
660 ##$aa-np---
A geographic area code for a book entitled Buddha and Buddhism about Buddhism in Nepal.

EX 4

660 ##$an-uso--
660 ##$an-usm--
Geographic area codes for a book entitled Collection of steamboat records relating to steamboats operating in the Ohio and Mississippi Valleys concentrating on both the Ohio and Mississippi rivers. Field 660 is repeated for code for each river.

EX 5
660 ##$an-us---
660 ##$ae-fr---
660 ##$aa-ja---
Geographic area codes for a book entitled Comparative studies of national libraries relating to the United States, France, and Japan. Field 660 is repeated for each region. 
*/

public class GeographicAreaCode implements SubjectInterface {
	private Vector<Field> fields=new Vector<Field>();
	private char indicator1,indicator2;
	
	public void setData(Tag tag) {
		this.indicator1=tag.getModifier1();
		this.indicator2=tag.getModifier2();
		this.fields=tag.getFields();
	}
	
	public SubjectInterface clone() {
		SubjectInterface c=new GeographicAreaCode();
		for(int i=0;fields!=null && i<fields.size();i++)
			c.setField(fields.elementAt(i)); // clone?
		return c;
	}
	
	public void setField(Field field) {
		fields.addElement(field);
	}
	
/**
 * Indicator 1: blank (not defined)
 * Indicator 2: blank (not defined)
 */
	public GeographicAreaCode() {
		indicator1=' ';
		indicator2=' ';
	}
	
/**
 * Use null values if not applicable
 * 	
 * $a Code
 * Geographic area code. Not repeatable. For codes see Appendix D.
 *  
 */
	public void setSubjectData(String code) {
		setField("a",code);
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
		return "660";
	}

}
