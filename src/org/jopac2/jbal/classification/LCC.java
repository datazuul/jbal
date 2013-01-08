package org.jopac2.jbal.classification;

import java.util.Vector;

import org.jopac2.jbal.abstractStructure.Field;

/**
 * 		$a Class number (not repeatable)
 *		$b Book number (not repeatable)
 * @author romano
 *
 */

public class LCC implements ClassificationInterface {
	private Vector<Field> data=new Vector<Field>();
	
	public LCC(String classNumber, String bookNumber) {
		if(classNumber!=null && classNumber.length()>0) data.addElement(new Field("a",classNumber));
		if(bookNumber!=null && bookNumber.length()>0) data.addElement(new Field("b",bookNumber));
	}
	
	public String getClassificationName() {
		return "LCC";
	}

	public Vector<Field> getData() {
		return data;
	}
}
