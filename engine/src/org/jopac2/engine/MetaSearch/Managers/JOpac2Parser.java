package org.jopac2.engine.MetaSearch.Managers;
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

/**
* @author	Iztok Cergol
* @version	19/08/2004
*/
import java.util.*;
import java.io.*;
//import javax.xml.parsers.*;

import org.apache.xerces.parsers.DOMParser;
import org.jopac2.utils.RecordItem;
import org.w3c.dom.*;

public class JOpac2Parser implements Parser
{
    public Vector<RecordItem> parse(BufferedReader br, String host, String contextDir, String dbname)
    {
        Vector<RecordItem> result = new Vector<RecordItem>();
        if(dbname==null) dbname="JOpac2";
        
        try{
            //in = endec.encode(in);
            //DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            DOMParser builder = new DOMParser();
            builder.parse(new org.xml.sax.InputSource(br));
            Document doc = builder.getDocument();
            
            NodeList nodes = doc.getElementsByTagName("record");
            //NodeList nodes = record.item(0);
            for (int i = 0; i < nodes.getLength(); i++){
                
               Element element = (Element) nodes.item(i);

               NodeList data = element.getElementsByTagName("data");
               Element linedata = (Element) data.item(0);
               
               NodeList type = element.getElementsByTagName("type");
               Element linetype = (Element) type.item(0);
               
               NodeList id = element.getElementsByTagName("id");
               Element lineid = (Element) id.item(0);

               
               result.add(new RecordItem(host,dbname,
               		java.net.URLDecoder.decode(getCharacterDataFromElement(linedata),"UTF-8"),
               		java.net.URLDecoder.decode(getCharacterDataFromElement(linetype),"UTF-8"),
               		java.net.URLDecoder.decode(getCharacterDataFromElement(linetype),"UTF-8"),
               		null,
               		java.net.URLDecoder.decode(getCharacterDataFromElement(lineid),"UTF-8")));  // host,data,type,syntax
            }
        }
        catch(Exception e){
            //e.printStackTrace();
        }
        
        return result;
        
        
    }
    
    private static String getCharacterDataFromElement(Element e) {
        Node child = e.getFirstChild();
        if (child instanceof CharacterData) {
            CharacterData cd = (CharacterData) child;
            return cd.getData();
        }
        return "?";
    }
}
