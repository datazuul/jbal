package org.jopac2.jbal;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

/*******************************************************************************
 * 
 * JOpac2 (C) 2002-2007 JOpac2 project
 * 
 * This file is part of JOpac2. http://jopac2.sourceforge.net
 * 
 * JOpac2 is free software; you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * JOpac2 is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * JOpac2; if not, write to the Free Software Foundation, Inc., 59 Temple Place,
 * Suite 330, Boston, MA 02111-1307 USA
 * 
 *******************************************************************************/

public class RecordFactory {

	public static final RecordInterface buildRecord(long id, String codedData,
			String type, String syntax, long level) throws SecurityException,
			IllegalArgumentException, NoSuchMethodException,
			InstantiationException, IllegalAccessException, InvocationTargetException {
		RecordInterface ma = null;
		if (syntax != null && syntax.length() > 0) {
			ma = buildRecord(id, codedData, syntax, level);
			if (ma == null)
				ma = buildRecord(id, codedData, type, level);
		} else {
			ma = buildRecord(id, codedData, type, level);
		}
		return ma;
	}

	// Object not=ISO2709.creaNotizia(id,contenuto,tipo,livelloPadre);
	@SuppressWarnings("unchecked")
	public static final RecordInterface buildRecord(long id, String codedData,
			String type, long level) throws SecurityException,
			NoSuchMethodException, IllegalArgumentException,
			InstantiationException, IllegalAccessException, InvocationTargetException {
		RecordInterface ma = null;
		String fl = type.substring(0, 1).toUpperCase();
		String type_upper = fl + type.substring(1);

		try {
			Class<RecordInterface> iso = null;
			if (iso == null)
				iso = getClassForName("org.jopac2.jbal.iso2709." + type);
			if (iso == null) {
				iso = getClassForName("org.jopac2.jbal.iso2709." + type_upper);
			}

			if (iso == null)
				iso = getClassForName("org.jopac2.jbal.sutrs." + type);
			if (iso == null) {
				iso = getClassForName("org.jopac2.jbal.sutrs." + type_upper);
			}

			if (iso == null)
				iso = getClassForName("org.jopac2.jbal.xml." + type);
			if (iso == null) {
				iso = getClassForName("org.jopac2.jbal.xml." + type_upper);
			}

			java.lang.reflect.Constructor c = iso.getConstructor(new Class[] {
					String.class, String.class, String.class });
			ma = (RecordInterface) c.newInstance(new Object[] { codedData,
					type, Long.toString(level) });
		} catch (java.lang.reflect.InvocationTargetException te) {
			System.out.println("id=" + id + "\nnotizia raw=" + codedData
					+ "\ntipo notizia=" + type + "\nlivello=" + level);
			te.printStackTrace();
			throw te;
		}
		return ma;
	}

	public static String[] getRecordInterfaces() {
		return 	ClassList.findClasses(Thread.currentThread().getContextClassLoader(), "org.jopac2.jbal");
	}

	@SuppressWarnings("unchecked")
	private static Class<RecordInterface> getClassForName(String name) {
		Class<RecordInterface> iso = null;
		try {
			iso = (Class<RecordInterface>) Class.forName(name);
		} catch (java.lang.NoClassDefFoundError er) {
			// System.out.println(name + " non trovato");
			iso = null;
		} catch (Exception e) {
			iso = null;
		}
		return iso;
	}

	private static String[] getClasses(String packageName)
			throws ClassNotFoundException, IOException {
		ClassLoader classLoader = Thread.currentThread()
				.getContextClassLoader();
		assert classLoader != null;
		String path = packageName.replace('.', '/');
		Enumeration<URL> resources = classLoader.getResources(path);
		List<File> dirs = new ArrayList<File>();
		while (resources.hasMoreElements()) {
			URL resource = resources.nextElement();
			dirs.add(new File(resource.getFile()));
		}
		ArrayList<String> classes = new ArrayList<String>();
		for (File directory : dirs) {
			classes.addAll(findClasses(directory, packageName));
		}
		return classes.toArray(new String[classes.size()]);
	}

	/**
	 * Recursive method used to find all classes in a given directory and
	 * subdirs.
	 * 
	 * @param directory
	 *            The base directory
	 * @param packageName
	 *            The package name for classes found inside the base directory
	 * @return The classes
	 * @throws ClassNotFoundException
	 */
	private static List<String> findClasses(File directory, String packageName)
			throws ClassNotFoundException {
		List<String> classes = new ArrayList<String>();
		if (!directory.exists()) {
			return classes;
		}
		File[] files = directory.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				assert !file.getName().contains(".");
				classes.addAll(findClasses(file, packageName + "."
						+ file.getName()));
			} else if (file.getName().endsWith(".class")) {
				// classes.add(packageName + '.' + file.getName().substring(0,
				// file.getName().length() - 6));
				String rName = file.getName().substring(0,
						file.getName().length() - 6);
				try {
					RecordInterface ma = RecordFactory.buildRecord(0, "",
							rName, 0);
					ma.destroy();
					classes.add(rName);
				} catch (Exception e) {
				}

			}
		}
		return classes;
	}
}
