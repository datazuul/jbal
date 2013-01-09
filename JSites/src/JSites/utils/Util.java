package JSites.utils;
/*******************************************************************************
*
*  JOpac2 (C) 2002-2009 JOpac2 project
*
*     This file is part of JOpac2. http://www.jopac2.org
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
*  Please, see NOTICE.txt AND LEGAL directory for more info. Different licences
*  may apply for components included in JOpac2.
*
*******************************************************************************/
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.apache.cocoon.environment.Request;

public class Util {
	
	/**
	 * @deprecated
	 * @param a
	 * @return
	 */
//	public static String readRequestParameter(String a){
//		String ret ="";
//		byte[] original;
//		try {
//			original = a.getBytes("ISO-8859-1");
//			ret = new String(original, "UTF-8");
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//		
//		return ret;
//	}
	
	/**
	 * @deprecated
	 * @param message
	 * @param attribute
	 * @return
	 */
	public static String[] extractAttribute(String message, String attribute) {
		if(message.startsWith("&")) message=message.substring(1);
		String[] att=message.split("&");
		int k=0;
		for(int i=0;i<att.length;i++) {
			if(att[i].startsWith(attribute+"=")) { // qui solo conto quanti saranno
				String[] z=att[i].substring(attribute.length()+1).split(",");
				k+=z.length;
			}
		}
		if(k>0)	{
			String[] r=new String[k];
			for(int i=0;i<att.length;i++) {
				if(att[i].startsWith(attribute+"=")) {
					String[] z=att[i].substring(attribute.length()+1).split(",");
					for(int h=0;h<z.length;h++) {
						k--;
						r[k]=z[h];
					}
				}
			}
			return r;
		}
		else {
			return null;
		}		
	}
	
	public static String removeAttribute(String queryString, String attribute) {
		if(queryString != null) {
			attribute=attribute.trim();
			boolean questionMark=queryString.startsWith("?");
			if(questionMark) queryString=queryString.substring(1);
			if(queryString.endsWith("&")) queryString=queryString.substring(0,queryString.length()-1);
			String[] attributes=queryString.split("&");
			StringBuffer ret=new StringBuffer();
			for(int i=0;i<attributes.length;i++) {
				if(attributes[i].contains("=")) {
					String[] p=attributes[i].split("=");
					if(!p[0].toLowerCase().trim().equalsIgnoreCase(attribute)) ret.append("&"+attributes[i]);
				}
				else {
					if(!attributes[i].toLowerCase().trim().equalsIgnoreCase(attribute)) ret.append("&"+attributes[i]);
				}
			}
			String r=ret.toString();
			if(r.startsWith("&")) r=r.substring(1);
			if(questionMark) r="?"+r;
			return r;
		}
		else {
			return null;
		}
	}

	public static String getRequestData(Request o, String p) {
		String r=o.getParameter(p);
		/**
		 * ATTENZIONE: tomcat > 4.x ha impostato come default charset encoding iso-8859-1
		 *             A PRESCINDERE DAL CHARSET INDICATO NELLA PAGINA!!
		 */
		String encoding="iso-8859-1"; //o.getCharacterEncoding();
		if(r==null) r=(String)o.getAttribute(p);
		try {
			if(r!=null && !p.equals("template"))
				r = URLDecoder.decode(r, encoding);
		} catch (Exception e) { }
		return r;
	}

}
