package org.jopac2.jbal.subject;

import java.util.Vector;

import org.jopac2.jbal.abstractStructure.Field;
import org.jopac2.jbal.abstractStructure.Tag;

/*
680 LIBRARY OF CONGRESS CLASSIFICATION

Field Definition

This field contains a class number applied to the item according to the Library of Congress classification schedules, with the optional addition of a book number applied to an individual book to identify it uniquely in the cataloguing agency's collections.

Occurrence

Optional. Repeatable.

Indicators

Indicator 1: blank (not defined)
Indicator 2: blank (not defined)

Subfields

$a Class number
The class number taken from the Library of Congress classification schedules. Not repeatable.

$b Book number
The individual book number applied by the cataloguing agency. Not repeatable.

Notes on Field Contents

A Library of Congress class number may be applied by any agency in possession of the Library of Congress Classification Schedules.

Examples

EX 1
680 ##$aQL737.C27C723$b.I74
The class number for A proposed delineation of critical grizzly bear habitat in the Yellowstone region : a monograph presented at the Fourth International Conference on Bear Research and Management. A book number has been added.

EX 2
680 ##$aPZ8.3.A6A6
The class number for The adventures of Egbert the Easter egg by Richard Willard Armour. 
*/

public class LibraryOfCongressClassification implements SubjectInterface {
	private Vector<Field> fields=new Vector<Field>();
	private char indicator1,indicator2;
	
	public void setData(Tag tag) {
		this.indicator1=tag.getModifier1();
		this.indicator2=tag.getModifier2();
		this.fields=tag.getFields();
	}
	
	public SubjectInterface clone() {
		SubjectInterface c=new LibraryOfCongressClassification();
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
	public LibraryOfCongressClassification() {
		indicator1=' ';
		indicator2=' ';
	}
	
/**
 * Use null values if not applicable
 * 	
 * $a Class number
 * The class number taken from the Library of Congress classification schedules. Not repeatable.
 * 
 * $b Book number
 * The individual book number applied by the cataloguing agency. Not repeatable.
 *  
 */
	public void setSubjectData(String classNumber, String bookNumber) {
		setField("a",classNumber);
		setField("b",bookNumber);
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
		return "680";
	}

}
