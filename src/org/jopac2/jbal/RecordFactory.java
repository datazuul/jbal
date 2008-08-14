package org.jopac2.jbal;

/*******************************************************************************
*
*  JOpac2 (C) 2002-2007 JOpac2 project
*
*     This file is part of JOpac2. http://jopac2.sourceforge.net
*
*  JOpac2 is free software; you can redistribute it and/or modify
*  it under the terms of the GNU General Public License as published by
*  the Free Software Foundation; either version 2 of the License, or
*  (at your option) any later version.
*
*  JOpac2 is distributed in the hope that it will be useful,
*  but WITHOUT ANY WARRANTY; without even the implied warranty of
*  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*  GNU General Public License for more details.
*
*  You should have received a copy of the GNU General Public License
*  along with JOpac2; if not, write to the Free Software
*  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*
*******************************************************************************/

public class RecordFactory {
	
	public static final RecordInterface buildRecord(long id, String codedData, String type, String syntax, long level) {
		RecordInterface ma=null;
		if(syntax!=null && syntax.length()>0) {
			ma=buildRecord(id,codedData,syntax,level);
			if(ma==null) ma=buildRecord(id,codedData,type,level);
		}
		else {
			ma=buildRecord(id,codedData,type,level);
		}
		return ma;
	}

	//  Object not=ISO2709.creaNotizia(id,contenuto,tipo,livelloPadre);
	@SuppressWarnings("unchecked")
	public static final RecordInterface buildRecord(long id, String codedData, String type, long level) {
		RecordInterface ma=null;
		String fl=type.substring(0,1).toUpperCase();
		String type_upper=fl+type.substring(1);
		
		try {
			Class<RecordInterface> iso=null;
			if(iso==null) iso=getClassForName("JOpac2.dataModules.iso2709."+type);
			if(iso==null) {
	    		iso=getClassForName("JOpac2.dataModules.iso2709."+type_upper);
			}
			
			if(iso==null) iso=getClassForName("JOpac2.dataModules.sutrs."+type);
			if(iso==null) {
	    		iso=getClassForName("JOpac2.dataModules.sutrs."+type_upper);
			}
			
			if(iso==null) iso=getClassForName("JOpac2.dataModules.xml."+type);
			if(iso==null) {
	    		iso=getClassForName("JOpac2.dataModules.xml."+type_upper);
			}
	    	
			java.lang.reflect.Constructor c=iso.getConstructor(new Class[] {String.class, String.class, String.class });
			ma=(RecordInterface)c.newInstance(new Object[] {codedData,type,Long.toString(level)});
		}
		catch (java.lang.reflect.InvocationTargetException te) {
			System.out.println("id="+id+"\nnotizia raw="+codedData+"\ntipo notizia="+type+"\nlivello="+level);
			te.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}


		return ma;
	}
	
	@SuppressWarnings("unchecked")
	private static Class<RecordInterface> getClassForName(String name) {
		Class<RecordInterface> iso=null;
    	try {
    		iso=(Class<RecordInterface>) Class.forName(name);
    	}
    	catch(java.lang.NoClassDefFoundError er){
    		//System.out.println(name + " non trovato");
    		iso = null;
    	}
    	catch (Exception e) {
    		iso=null;
    	}
    	return iso;
	}

}
