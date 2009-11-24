package org.jopac2.jbal.classification;

import java.util.Vector;

import org.jopac2.jbal.abstractStructure.Field;

/**
 * 		$a Number (not repeatable)
 *      $c Description // dedotto da sebina
 * 		$v Edition (not repeatable)
 * 		$z Language of edition (not repeatable)
 * @author romano
 *
 */

public class DDC implements ClassificationInterface {
	private Vector<Field> data=new Vector<Field>();
	
	
	public DDC(String number, String desc, String edition, String language) {
		if(number!=null && number.length()>0) data.addElement(new Field("a",number));
		if(edition!=null && edition.length()>0) data.addElement(new Field("v",edition));
		if(language!=null && language.length()>0) data.addElement(new Field("z",language));
		if(desc!=null && desc.length()>0) data.addElement(new Field("c",desc));
	}
	
	public String getClassificationName() {
		return "DDC";
	}

	public Vector<Field> getData() {
		return data;
	}
	
	public String toString() {
		String r=getClassificationName()+": ";
		for(int i=0;i<data.size();i++) {
			r+=data.elementAt(i).getContent()+" ";
		}
		return r;
	}

	
}
