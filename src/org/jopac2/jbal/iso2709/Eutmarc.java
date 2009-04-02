package org.jopac2.jbal.iso2709;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;

import org.apache.xerces.impl.dv.util.Base64;
import org.jopac2.jbal.abstractStructure.Field;
import org.jopac2.jbal.abstractStructure.Tag;
import org.jopac2.utils.BookSignature;
import org.jopac2.utils.JOpac2Exception;

public class Eutmarc extends Unimarc {
	private int maxx=104, maxy=150;

	public Eutmarc(String stringa, String tipo) {
		super(stringa, tipo);
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

}
