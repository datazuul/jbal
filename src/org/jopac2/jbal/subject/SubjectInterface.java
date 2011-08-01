package org.jopac2.jbal.subject;

import java.util.Vector;

import org.jopac2.jbal.abstractStructure.Field;
import org.jopac2.jbal.abstractStructure.Tag;

public interface SubjectInterface {
	public Vector<Field> getData();
	public void setData(Tag tag);
	public void setField(Field field);
	public String getTagIdentifier();
	public char getIndicator1();
	public char getIndicator2();
	public SubjectInterface clone();
}
