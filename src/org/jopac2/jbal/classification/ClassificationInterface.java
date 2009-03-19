package org.jopac2.jbal.classification;

import java.util.Vector;

import org.jopac2.jbal.abstractStructure.Field;

public interface ClassificationInterface {
	public Vector<Field> getData();
	public String getClassificationName();
}
