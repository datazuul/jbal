package org.jopac2.jbal.classification;

import java.util.Vector;

import org.jopac2.jbal.abstractStructure.Field;

/**
 * 		$a Class number (repeatable)
 *		$b Book Number (repeatable)
 * 		$c Classification Subdivision (repeatable)
 *		$2 System Code (not repeatable)
 * @author romano
 *
 */

public class OtherClassification implements ClassificationInterface {
	private Vector<Field> data=new Vector<Field>();
	private String code=null;
	
	public OtherClassification(String classNumber, String bookNumber, String classificationSubdivision, String systemCode) {
		code=systemCode;
		if(systemCode!=null && systemCode.length()>0) data.addElement(new Field("2",systemCode));
		if(classNumber!=null && classNumber.length()>0) data.addElement(new Field("a",classNumber));
		if(bookNumber!=null && bookNumber.length()>0) data.addElement(new Field("b",bookNumber));
		if(classificationSubdivision!=null && classificationSubdivision.length()>0) data.addElement(new Field("c",classificationSubdivision));
	}
	
	public String getClassificationName() {
		return "DDC";
	}

	public Vector<Field> getData() {
		return data;
	}

	
}
