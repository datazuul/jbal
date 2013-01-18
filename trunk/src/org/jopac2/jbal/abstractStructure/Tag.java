package org.jopac2.jbal.abstractStructure;

import java.nio.charset.Charset;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.commons.lang.ArrayUtils;
import org.jopac2.utils.JOpac2Exception;

public class Tag implements Comparable<Tag> {
	String tagName;
	char modifier1=0,modifier2=0;
	Vector<Field> fields=null;
	String rawContent=null;
	
	private Delimiters delimiters=new Delimiters();

	/**
	 * Create a empty Tag with modifiers (indicators) as given
	 * @param tagName
	 * @param modifier1
	 * @param modifier2
	 */
	public Tag(String tagName, char modifier1, char modifier2) {
		this.tagName=tagName;
		this.modifier1=modifier1;
		this.modifier2=modifier2;
	}
	
	/**
	 * Build a Tag on the coded tag string given
	 * @param tagString
	 */
	public Tag(String tagString) {
		Charset charset=Charset.defaultCharset();
		init(tagString.substring(0,3),tagString.substring(3).getBytes(charset),charset,delimiters);
//		init(tagString,delimiters);
	}
	
	/**
	 * Shortcut to create a tag with ONE field
	 * @param tagName
	 * @param fieldName
	 * @param fieldContent
	 */
	public Tag(String tagName, String fieldName, String fieldContent) {
		this.tagName=tagName;
		Field f=new Field(fieldName, fieldContent);
		this.addField(f);
	}
	
	
	public Tag(String tagString, Delimiters d) {
		Charset charset=Charset.defaultCharset();
		init(tagString.substring(0,3),tagString.substring(3).getBytes(charset),charset,d);
	}
	
	public Tag(String tagName, byte[] tag, Charset charset, Delimiters d) {
		init(tagName,tag,charset,d);
	}
	
	private void init(String tagName, byte[] tag, Charset charset, Delimiters d) {
		this.tagName=tagName;
		int tl=tag.length;
		if(tl>0 && tag[tag.length-1]==d.getByteFt()) tl--;
		
		if(ArrayUtils.contains(tag, d.getByteDl())) {
			// check if there are modifiers
			if(tag[0]!=d.getByteDl()) {
				modifier1=(char)tag[0];
				modifier2=(char)tag[1];
				byte[] newtag=new byte[tl-2];
				System.arraycopy(tag, 2, newtag, 0, newtag.length);
				tag=newtag;
				tl=tag.length;
//				tagString=tagString.substring(5);
			}
			
			
			if(fields==null) fields=new Vector<Field>();
			
			int p=ArrayUtils.indexOf(tag, d.getByteDl());
			while(p>=0 && p!=tl) {
				int p1=ArrayUtils.indexOf(tag, d.getByteDl(),p+1);
				p1=p1>=0?p1:tl;
				String element=String.valueOf((char)tag[p+1]);
				String content=new String(tag,p+2,p1-p-2,charset);
				if(content.length()>0) fields.addElement(new Field(element,content,d.getDl()));
				p=p1;
			}
			
//			String ctk;
//		    StringTokenizer tk=new StringTokenizer(tagString,d.getDl());
//		    while(tk.hasMoreTokens()) {
//		      ctk=tk.nextToken();
//		      String element=ctk.substring(0,1).trim();
//		      String content=ctk.substring(1);
//		      if(content.length()>0) fields.addElement(new Field(element,content,d.getDl()));
//		    }
		}
		else {
			rawContent=new String(tag,0,tl,charset);
		}
	}
	
	/**
	 * Add a new Field to this Tag (duplicate field code if already exists)
	 * @param field
	 */
	public void addField(Field field) {
		if(fields==null) fields=new Vector<Field>();
		fields.add(field);
	}
	
	/**
	 * Set the value of a Field. If the field exists the value is changed.
	 * If the value doesn't exist, the field is added
	 * @param field
	 */
	public void setField(Field field) {
		Field f=getField(field.getFieldCode());
		if(f==null) addField(field);
		// se gli oggetti sono passati by reference dovrebbe essere sufficiente
		else f.setContent(field.getContent());
	}
	
	/**
	 * Get the first Field with the fieldCode given
	 * @param fieldCode
	 * @return
	 */
	public Field getField(String fieldCode) {
		Field r=null;
		if(fields!=null && fields.size()>0) {
			for(int i=0;i<fields.size();i++) {
				if(fields.elementAt(i).getFieldCode().equals(fieldCode)) {
					r=fields.elementAt(i);
					break;
				}
			}
		}
		return r;
	}
	
	/**
	 * Get a Field vector of fields with the fieldCode given
	 * @param fieldCode
	 * @return
	 */
	public Vector<Field> getFields(String fieldCode) {
		Vector<Field> r=new Vector<Field>();
		if(fields!=null && fields.size()>0) {
			for(int i=0;i<fields.size();i++) {
				if(fields.elementAt(i).getFieldCode().equals(fieldCode)) {
					r.addElement(fields.elementAt(i));
				}
			}
		}
		return r;
	}
	
	/**
	 * Get the Tag name. I.e. 200, 201, ....
	 * @return
	 */
	public String getTagName() {
		return tagName;
	}
	
	/**
	 * Set the Tag name. I.e. 200, 201, ....
	 * @param tagName
	 */
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
	
	/**
	 * Get the value of the first modifier (indicator)
	 * @return
	 */
	public char getModifier1() {
		return modifier1;
	}
	
	/**
	 * Set the value of the first modifier (indicator)
	 * @param modifier1
	 */
	public void setModifier1(char modifier1) {
		this.modifier1 = modifier1;
	}
	
	/**
	 * Get the value of the second modifier (indicator)
	 * @return
	 */
	public char getModifier2() {
		return modifier2;
	}
	
	/**
	 * Set the value of the second modifier (indicator)
	 * @param modifier2
	 */
	public void setModifier2(char modifier2) {
		this.modifier2 = modifier2;
	}
	
	/**
	 * Get a Field vector of all fields in this Tag
	 * @return
	 */
	public Vector<Field> getFields() {
		return fields;
	}
	
	public void removeField(String field) {
		if(fields!=null)
		for(int j=fields.size()-1;j>=0;j--) {
			if(fields.elementAt(j).getFieldCode().equals(field))
				fields.removeElementAt(j);
		}
	}
	
	/**
	 * Return a iso2709 compilant rappresentation of this Tag
	 */
	public String toString() {
		String r = "";
		if (fields != null) {
			r = tagName;
			if(modifier1>0) r+= modifier1 + "" + modifier2;
			for (int i = 0; fields != null && i < fields.size(); i++) {
				r += fields.elementAt(i).toString();
			}
		} else {
			r = tagName + rawContent;
		}
		return r;
	}
	
	
	/**
	 * Return a iso2709 byte compilant rappresentation of this Tag
	 */
	public byte[] toBytes() {
		String r = "";
		if (fields != null) {
			r = tagName;
			if(modifier1>0) r+= modifier1 + "" + modifier2;
			for (int i = 0; fields != null && i < fields.size(); i++) {
				r += fields.elementAt(i).toString();
			}
		} else {
			r = tagName + rawContent;
		}
		return r.getBytes();
	}
	
	
	/**
	 * 
	 * @return XML fragment for this tag
	 */
	public String toXML() {
		String r="";
		if (fields != null) {
			r = "<datafield tag=\"" + tagName+"\"";
			if(modifier1>0) {
				r+= " ind1=\""+modifier1 + "\" ind2=\"" + modifier2 + "\"";
			}
			r=r+ ">\n";
			
			for (int i = 0; fields != null && i < fields.size(); i++) {
				r += "\t\t"+fields.elementAt(i).toXML()+"\n";
			}
			
			r=r+"</datafield>\n";
			
		} else {
			r = "<controlfield tag=\"" + tagName + "\">" + rawContent + "</controlfield>\n";
		}
		return r;
	}
	
	/**
	 * Get raw content of Tag. This value is significant if and only if there are no Field in Tag
	 * @return
	 */
	public String getRawContent() {
		return rawContent;
	}

	
	public void setRawContent(String rawContent) throws JOpac2Exception {
		if(fields!=null && fields.size()>0) throw new JOpac2Exception("Tag contains Fields! rawContent not set!");
		this.rawContent = rawContent;
	}

	/**
	 * Check NSB-NSE correctness for any Field in this Tag, for NSB,NSE given
	 * @param nsb
	 * @param nse
	 */
	public void checkNSBNSE(String nsb, String nse) {
		for(int i=0;fields!=null && i<fields.size();i++) {
			String content=fields.elementAt(i).getContent();
			int  insb=content.indexOf(nsb);
			int  inse=content.indexOf(nse);
			int cp=0;
			while(inse>=0) {
				if(insb>inse || insb==-1) {
					// rimuovi nse
					content=content.substring(0,inse)+content.substring(inse+nse.length());
				}
				else {
					cp=inse+1;
				}
				insb=content.indexOf(nsb,cp);
				inse=content.indexOf(nse,cp);
			}
			fields.elementAt(i).setContent(content); // non fare trim(): se dl e' all'inizio viene perso
		}
	}

	/**
	 * Check NSB-NSE correctness for any Field in this Tag
	 * with
	 * 		nsb=0x88
	 * 		nse=0x89
	 * as defined in ISO 6630
	 * 
	 * ICCU used for a time:
	 *     nsb=0x1b+"H"
	 *     nse=0x1b+"I"
	 */

	public void checkNSBNSE() {
		String nse=String.valueOf((char)0x88);
		String nsb=String.valueOf((char)0x89);
		checkNSBNSE(nsb,nse);
	}

	public int compareTo(Tag arg0) {
		return tagName.compareTo(arg0.getTagName());
	}

	/**
	 * Remove empty fields, I.e. content of field is null or 0 length
	 */
	public void removeEmptyFields() {
		if(fields!=null && fields.size()>0)
			for(int i=fields.size()-1;i>=0;i--) {
				if(fields.elementAt(i).getContent()==null || fields.elementAt(i).getContent().length()==0) fields.removeElementAt(i);
		}
	}

	public boolean equalsContent(Tag tag) {
		String q=this.toString();
		String o=tag.toString();
		return q.equals(o);
	}
}
