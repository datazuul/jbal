package org.jopac2.jbal.iso2709;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.imageio.ImageIO;

import org.apache.xerces.impl.dv.util.Base64;
import org.jopac2.jbal.ElectronicResource;
import org.jopac2.jbal.abstractStructure.Field;
import org.jopac2.jbal.abstractStructure.Tag;
import org.jopac2.utils.BookSignature;
import org.jopac2.utils.JOpac2Exception;
import org.jopac2.utils.Utils;

public class Eutmarc extends Unimarc {
	
//		public static final String MONOGRAFIA = "M";
//		public static final String PERIODICO = "P";
//		public static final String COLLANA = "C";
//		public static final String FASCICOLO = "F";
//		public static final String SPOGLIO = "S";
	
        private int maxx=104, maxy=150;

        public Eutmarc(byte[] stringa, Charset charset, String tipo)  throws Exception {
                super(stringa, charset, tipo);
        }
        
        public Eutmarc(byte[] stringa,Charset charset, String tipo, String livello)  throws Exception {
            super(stringa, charset, tipo, livello);
        }
        
        public Hashtable<String, List<Tag>> getRecordMapping() {
    		Hashtable<String, List<Tag>> r=super.getRecordMapping();
    		
    		List<Tag> nat=new Vector<Tag>();
    		nat.add(new Tag("901","a",""));
    		r.put("NAT", nat);
    		
    		return r;
    	}

    	public String getRecordTypeDescription() {
    		return "EUT unimarc format.";
    	}
        
//        901 ^a mettiamo il tipo:
//            M = monografia
//            P = periodico
//            C = collana
//            F = fascicolo
//            S = spoglio (articolo)
        
        public void setPublicationNature(String type){
        	try {
				removeTags("901");
			} catch (JOpac2Exception e) { }
        	Tag t = new Tag("901",' ',' ');
        	t.addField(new Field("a",type));
        	addTag(t);
        }
        
        public String getPublicationNature(){
        	String s = null;
        	Field a = null;
        	Tag t=getFirstTag("901");
      	  	if(t!=null) a=t.getField("a");
      	  	if(a!=null) s=a.getContent();
      	  	return s;
      	  
        }


        @Override
        public Vector<BookSignature> getSignatures() {
                // TODO Auto-generated method stub
                return null;
        }

        public void addSignature(BookSignature signature) throws JOpac2Exception {
                // TODO Auto-generated method stub
               
        }
       


        public void setMaxx(int maxx) {
                this.maxx = maxx;
        }

        public int getMaxx() {
                return maxx;
        }

        public void setMaxy(int maxy) {
                this.maxy = maxy;
        }

        public int getMaxy() {
                return maxy;
        }
        
    	
    	/**
    	 * Use UNIMARC getAvailibilityAndOrPrice
    	 * @deprecated
    	 *
    	 * 
    	 * 904^p^n 
    	 */
    	public void setPrezzo(String p, String note){
    		boolean ok = false;
    		
    		Tag a = new Tag("904",' ',' ');
    		if(p!=null && p.length()>0){
    			a.addField(new Field("p",p));
    			ok = true;
    		}
    		if(note!=null && note.length()>0){
    			a.addField(new Field("n",note));
    			ok = true;
    		}
    		if(ok)
    			addTag(a);
    	}
    	
    	/**
    	 * Use UNIMARC getAvailibilityAndOrPrice
    	 * @deprecated
    	 */
    	public String getPrezzo(){
    		Tag t = getFirstTag("904");
    		if(t==null)return "";
    		Field f = t.getField("p");
    		if(f==null)return "";
    		String r = f.getContent();
    		if(r==null)r ="";
    		return r;
    	}
    	
    	/**
    	 * Use UNIMARC getAvailibilityAndOrPrice
    	 * @deprecated
    	 */
    	public String getPrice() {
    		String r = null;
    		Tag t = getFirstTag("904");
    		if(t!=null){
	    		Field f = t.getField("n");
	    		if(f!=null)
	    			r = f.getContent();
    		}
    		return r;
    	}
    	

//		@Override
//		public void setDescription(String description) throws JOpac2Exception {
//			//la stringa splittata su "&" deve avere 4 parti
//			//pagine&ill.&cm&alleg
//			String[] split = description.split("&");
//			if(split.length!=4) throw new JOpac2Exception("Description not in right format");
//			else{	
//				Tag t = new Tag("215",' ',' ');
//				boolean add = false;
//				if(split[0].length()>1){
//					t.addField(new Field("a",split[0].trim())); add = true;
//				}
//				if(split[1].length()>1){
//					t.addField(new Field("c",split[1].trim())); add = true;
//				}
//				if(split[2].length()>1){
//					t.addField(new Field("d",split[2].trim())); add = true;
//				}
//				if(split[3].length()>1){
//					t.addField(new Field("e",split[3].trim())); add = true;
//				}
//				if(add){
//					removeTags("215");
//					addTag(t);
//				}
//			}
//		}

		public void setSeriesTitle(String collana, String collanaN) {
			Tag t = new Tag("225",'|',' ');
			if(collana != null && collana.length()>0){
				t.addField(new Field("a",collana));
				if(collanaN != null && collanaN.length()>0)
					t.addField(new Field("v",collanaN+"."));
				addTag(t);
			}
		}
		
		public String getSeriesTitle() {
			Tag tag=getFirstTag("225");
			if(tag == null) return "";
			return Utils.ifExists("",tag.getField("a"));
		}

		public void setEditor() {
			Tag t = new Tag("210",' ',' ');
			t.addField(new Field("a","Trieste"));
			t.addField(new Field("c","Edizioni Universita'"));
		}
		
//		public String getTitle() {
//		    String r=null;
//		    Tag tag=getFirstTag("200");
//		    if(tag==null)return "";
//		    Vector<Field> a=tag.getFields("a");
//		    r=a.elementAt(0).getContent();
//		    for(int i=1;i<a.size();i++) r+=" ; "+a.elementAt(i).getContent();
////		    r+=Utils.ifExists(" [] ",tag.getField("b"));
////		    r+=Utils.ifExists(" . ",tag.getField("c"));
////		    r+=Utils.ifExists(" = ",tag.getField("d"));
////		    r+=Utils.ifExists(" : ",tag.getField("e"));
////		    r+=Utils.ifExists(" / ",tag.getField("f"));
////		    r+=Utils.ifExists(" ; ",tag.getField("g"));
////		    r+=Utils.ifExists(" . ",tag.getField("h"));
////		    r+=Utils.ifExists(" , ",tag.getField("i"));
//
//		    return r;
//		  }
		
    	
		public void setLink(String link, String tipo) {
//			903 ^a tipo link ^l link
//	        C = catalogo opac
//	        A = OpenstarTS (archivio)
//	        B = biobibliografia
			Tag t = new Tag("903",' ',' ');
			if(tipo != null && tipo.length()>0){
				t.addField(new Field("a",tipo));
				if(link != null && link.length()>0)
					t.addField(new Field("l",link));
				addTag(t);
			}
		}
		
		public String getLink() {
			Tag tag=getFirstTag("903");
			if(tag == null) return null;
			return Utils.ifExists("",tag.getField("l"));
		}
		
		public String getLinkType() {
//	        C = catalogo opac
//	        A = OpenstarTS (archivio)
//	        B = biobibliografia
			Tag tag=getFirstTag("903");
			return Utils.ifExists("",tag.getField("a"));
		}
	

}

