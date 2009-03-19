package org.jopac2.jbal.subject;

import java.util.Vector;

import org.jopac2.jbal.abstractStructure.Field;

public interface SubjectInterface {
	public Vector<Field> getData();
	public String getTagIdentifier();
	public char getIndicator1();
	public char getIndicator2();
}
