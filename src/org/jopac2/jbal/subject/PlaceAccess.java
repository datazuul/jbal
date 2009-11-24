package org.jopac2.jbal.subject;

import java.util.Vector;

import org.jopac2.jbal.abstractStructure.Field;
import org.jopac2.jbal.abstractStructure.Tag;

/*
620 PLACE ACCESS

Field Definition

This field contains an access point form of a place of publication, production, etc. The field may include the name of a country, state or province, county and/or city.

Occurrence

Optional. Repeatable.

Indicators

Indicator 1: blank (not defined)
Indicator 2: blank (not defined)

Subfields

$a Country. Not repeatable.
$b State or Province etc. Not repeatable.
$c County. Not repeatable.
$d City. Not repeatable.
$3 Authority Record Number
The control number for the authority record for the heading. This subfield is for use with UNIMARC/Authorities (EX 1). Not repeatable.

Notes on Field Contents

The content of this field may be in hierarchical form, e.g. country, state, and city; or it may be in non-hierarchical form, e.g. city alone, depending on institutional practice.

Examples

EX 1
620 ##$398-8685$aUnited States$bAlabama$dMontgomery
An item published in the city of Montgomery, Alabama. The field is in hierarchical form. There is a record for Montgomery in the authorities file, with an 001 of 98-8685.

EX 2
620 ##$dRoma
An item published in Rome. The field is in non-hierarchical form.

EX 3
620 ##aUnited States$bVirginia$cPrince William County$dHaymarket
An item published in Haymarket, Virginia. The field is in hierarchical form. County is also recorded. 
*/

public class PlaceAccess implements SubjectInterface {
	private Vector<Field> fields=new Vector<Field>();
	private char indicator1,indicator2;
	
	public void setData(Tag tag) {
		this.indicator1=tag.getModifier1();
		this.indicator2=tag.getModifier2();
		this.fields=tag.getFields();
	}
	
	public SubjectInterface clone() {
		SubjectInterface c=new PlaceAccess(indicator1);
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
	public PlaceAccess(char levelOfSubjectTerm) {
		indicator1=' ';
		indicator2=' ';
	}
	
/**
 * Use null values if not applicable
 * 	
 * $a Country. Not repeatable.
 * $b State or Province etc. Not repeatable.
 * $c County. Not repeatable.
 * $d City. Not repeatable.
 * $3 Authority Record Number
 */
	public void setSubjectData(String country, String stateOrProvince, String county, String city, String authorityRecordNumber) {
		setField("a",country);
		setField("b",stateOrProvince);
		setField("c",county);
		setField("d",city);
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
		return "620";
	}

}
