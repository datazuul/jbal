package org.jopac2.jbal.subject;

import java.util.Vector;

import org.jopac2.jbal.abstractStructure.Field;
import org.jopac2.jbal.abstractStructure.Tag;

/**
604 NAME AND TITLE USED AS SUBJECT

Field Definition

This field contains an author and title of a work which is one of the subjects of the item. The field is structured like the 4-- Linking Entry fields, Embedded fields technique.

Occurrence

Optional. Repeatable.

Indicators

Indicator 1: blank (not defined)
Indicator 2: blank (not defined)

Subfields

$1 (one) Linking data
(See explanation under 4-- LINKING ENTRY BLOCK)

Notes on Field Contents

This field is used to record the name of a work used as a subject, when it is represented by a name/title heading. The title of the work is recorded in an embedded 500 Uniform Title field. The subject system code ($2), Authority Record Number ($3) and any subject subdivisions ($j, $x, $y, $z) needed should also be carried in the embedded 500 field. The name of the author is carried in an embedded 7-- Intellectual Responsibility field.

Related Fields

4-- LINKING ENTRY BLOCK

600 PERSONAL NAME USED AS SUBJECT

601 CORPORATE BODY NAME USED AS SUBJECT

602 FAMILY NAME USED AS SUBJECT

605 TITLE USED AS SUBJECT

When a personal name, corporate body, family or title alone is the subject, the above fields are used.

Examples

EX 1
604 ##$1700#1$aBeethoven,$bLudwig van,$f1770-1827.$150000$aSymphonies, $sno. 5, op. 67,
$uC minor$2lc

EX 2
604 ##$1700#0$aOvid$f43B.C.-17 or 18.$4070$150001$aMetamorphoses$hLiber 2$2lc

EX 3
604 ##$171001$aUnited States.$150010$aConstitution.$h1st Amendment.$21c

EX 4
604 ##$1700#1$aCervantes Saavedra$bMiguel de$f1547-1616$150001$aDon Quixote$jIllustrations$21c 
*/

public class NameAndTitleSubject implements SubjectInterface {
	private Vector<Field> fields=new Vector<Field>();
	private char indicator1,indicator2;
	
	public void setData(Tag tag) {
		this.indicator1=tag.getModifier1();
		this.indicator2=tag.getModifier2();
		this.fields=tag.getFields();
	}
	
	public SubjectInterface clone() {
		SubjectInterface c=new NameAndTitleSubject();
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
	public NameAndTitleSubject() {
		indicator1=' ';
		indicator2=' ';
	}
	
/**
 * $1 (one) Linking data
 * (See explanation under 4-- LINKING ENTRY BLOCK) 
 * TODO SBAGLIATO, verificare come sono i link 4--
 */
	public void setSubjectData(SubjectInterface linked) {
		Tag t=new Tag(linked.getTagIdentifier(),linked.getIndicator1(),linked.getIndicator2());
		for(int i=0;i<linked.getData().size();i++) {
			t.addField(linked.getData().elementAt(i));
		}
		/**
		 * TODO SBAGLIATO, verificare come sono i link 4--
		 */
		String ta=t.toString();
		setField("1",ta);
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
		return "604";
	}

}
