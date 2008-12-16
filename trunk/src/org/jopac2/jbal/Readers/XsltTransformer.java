package org.jopac2.jbal.Readers;
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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PipedOutputStream;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class XsltTransformer extends Thread {
	private String xslt=null;
	String xml=null;
	PipedOutputStream out=null;
	InputStream in=null;
	
	public XsltTransformer(String xml,PipedOutputStream out, String xslt) throws FileNotFoundException {
		this.xslt=xslt;
		this.xml=xml;
		this.out=out;
		File inn=new File(xml);
		in=new FileInputStream(inn);
	}
	
	public XsltTransformer(InputStream in,PipedOutputStream out, String xslt) throws FileNotFoundException {
		this.xslt=xslt;
		this.in=in;
		this.out=out;
	}
	
	public void run() {
		TransformerFactory tFactory = TransformerFactory.newInstance();
		try {
			Transformer transformer = tFactory.newTransformer(new StreamSource(xslt));
			transformer.transform(new StreamSource(in), new StreamResult(out));
			in.close();
			out.flush();
			out.close();
			
			//System.out.println("\nfine");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
