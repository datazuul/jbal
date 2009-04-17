package org.jopac2.jbal.iso2709;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;

import org.apache.xerces.impl.dv.util.Base64;
import org.jopac2.jbal.RecordFactory;
import org.jopac2.jbal.abstractStructure.Field;
import org.jopac2.jbal.abstractStructure.Tag;
import org.jopac2.utils.BookSignature;
import org.jopac2.utils.JOpac2Exception;
import org.jopac2.utils.Utils;

public class Eutmarc extends Unimarc {
	
		public static final String MONOGRAFIA = "M";
		public static final String PERIODICO = "P";
		public static final String COLLANA = "C";
		public static final String FASCICOLO = "F";
		public static final String SPOGLIO = "S";
	
        private int maxx=104, maxy=150;

        public Eutmarc(String stringa, String tipo) {
                super(stringa, tipo);
        }
        
        public Eutmarc(String stringa, String tipo, String livello) {
            super(stringa, tipo, livello);
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
       
        public void setImage(BufferedImage image) {
                ByteArrayOutputStream a=new ByteArrayOutputStream();
                   try {
                        Image im=image.getScaledInstance(maxx, maxy, Image.SCALE_SMOOTH);
                        BufferedImage dest = new BufferedImage(maxx,maxy,
                                        BufferedImage.TYPE_INT_RGB);
                        dest.createGraphics().drawImage(im, 0, 0, null);
                        ImageIO.write (dest, "jpeg", a);                        
                        String coded=Base64.encode(a.toByteArray());
                        Tag t=new Tag("911",' ',' ');
                        t.addField(new Field("a",coded));
                        try {
                                removeTags("911");
                        } catch (JOpac2Exception e) {
                        }
                        addTag(t);
                        a.reset();
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }
       
        public BufferedImage getImage() {
                BufferedImage r=null;
                Tag t=getFirstTag("911");
                if(t!=null) {
                        Field i=t.getField("a");
                        if(i!=null) {
                                String coded=i.getContent();
                                byte[] b=Base64.decode(coded);
                                try {
                                        r=ImageIO.read(new ByteArrayInputStream(b));
                                } catch (IOException e) {
                                        e.printStackTrace();
                                }
                        }
                }
                return r;
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
    	
    	public String getPrezzoNote(){
    		String r = null;
    		Tag t = getFirstTag("904");
    		if(t!=null){
	    		Field f = t.getField("n");
	    		if(f!=null)
	    			r = f.getContent();
    		}
    		return r;
    	}
    	
    	public String getPrezzo(){
    		Tag t = getFirstTag("904");
    		Field f = t.getField("p");
    		String r = f.getContent();
    		if(r==null)r ="";
    		return r;
    	}
    	

		@Override
		public void setDescription(String description) throws JOpac2Exception {
			//la stringa splittata su "&" deve avere 4 parti
			//pagine&ill.&cm&alleg
			String[] split = description.split("&");
			if(split.length!=4) throw new JOpac2Exception("Description not in right format");
			else{	
				Tag t = new Tag("215",' ',' ');
				boolean add = false;
				if(split[0].length()>1){
					t.addField(new Field("a",split[0].trim())); add = true;
				}
				if(split[1].length()>1){
					t.addField(new Field("c",split[1].trim())); add = true;
				}
				if(split[2].length()>1){
					t.addField(new Field("d",split[2].trim())); add = true;
				}
				if(split[3].length()>1){
					t.addField(new Field("e",split[3].trim())); add = true;
				}
				if(add){
					removeTags("215");
					addTag(t);
				}
			}
		}

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
			return Utils.ifExists("",tag.getField("a"));
		}

		public void setEditor() {

			Tag t = new Tag("210",' ',' ');
			t.addField(new Field("a","Trieste"));
			t.addField(new Field("c","Edizioni Universit�"));
			
			
		}
		
		public String getTitle() {
		    String r=null;
		    Tag tag=getFirstTag("200");
		    Vector<Field> a=tag.getFields("a");
		    r=a.elementAt(0).getContent();
		    for(int i=1;i<a.size();i++) r+=" ; "+a.elementAt(i).getContent();
//		    r+=Utils.ifExists(" [] ",tag.getField("b"));
//		    r+=Utils.ifExists(" . ",tag.getField("c"));
//		    r+=Utils.ifExists(" = ",tag.getField("d"));
//		    r+=Utils.ifExists(" : ",tag.getField("e"));
//		    r+=Utils.ifExists(" / ",tag.getField("f"));
//		    r+=Utils.ifExists(" ; ",tag.getField("g"));
//		    r+=Utils.ifExists(" . ",tag.getField("h"));
//		    r+=Utils.ifExists(" , ",tag.getField("i"));

		    return r;
		  }
		
    	
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

