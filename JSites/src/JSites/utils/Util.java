package JSites.utils;

import java.io.UnsupportedEncodingException;

public class Util {
	
	public static String readRequestParameter(String a){
		String ret ="";
		byte[] original;
		try {
			original = a.getBytes("ISO-8859-1");
			ret = new String(original, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return ret;
	}
	
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

}
