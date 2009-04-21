package JSites.transformation.uploadparsing;

import java.io.*;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import org.apache.cocoon.xml.AttributesImpl;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import JSites.transformation.MyAbstractPageTransformer;

public class TableUploadParser extends MyAbstractPageTransformer {
	
	
	boolean loadFromFile = false;
	private FileInputStream fs = null; 
	private boolean readfilename = false;
	private boolean writeChars = true;
	private HSSFWorkbook wb = null;
	
	public void startElement(String namespaceURI, String localName, String qName, Attributes attributes) throws SAXException
	{
		if(localName.equals("save")){
			loadFromFile = Boolean.parseBoolean(attributes.getValue("loadFromFile"));
		}
		if(loadFromFile){
			if(localName.startsWith("field-") ||
				localName.startsWith("upper-") ||
				localName.startsWith("left-") || 
				localName.equals("rows") || localName.equals("cols") ){ writeChars = false;}
			else if(localName.equals("upload-file")){
				readfilename = true;
			}
			else
				super.startElement(namespaceURI, localName, qName, attributes);
		}
		else
			super.startElement(namespaceURI, localName, qName, attributes);
		

	}
	
	public void endDocument() throws SAXException{
		if(fs!=null)
			try {fs.close();} catch (IOException e) {e.printStackTrace();}
		super.endDocument();

	}
	
	public void endElement(String namespaceURI, String localName, String qName) throws SAXException
    {
		if(loadFromFile){
			if(localName.startsWith("field-") ||
				localName.startsWith("upper-") ||
				localName.startsWith("left-") || 
				localName.equals("rows") || localName.equals("cols") ){ writeChars = true; }
			else if(localName.equals("upload-file")){
				readfilename = false;
			}
			else
				super.endElement(namespaceURI, localName, qName);
		}
		else
			super.endElement(namespaceURI, localName, qName);
    }
	
	public void characters(char[] a, int s, int e) throws SAXException{
		
		if(readfilename){
			try{
				String filename = new String(a,s,e);
				fs = new FileInputStream(filename);
				
				if(filename.endsWith("xls")){
					POIFSFileSystem poifs = new POIFSFileSystem(fs);
					parseXLS(poifs);
				}
				else{
					BufferedReader br = new BufferedReader(new InputStreamReader(fs));
					parseTable(br);
					br.close();
				}
				
			}catch(Exception ex){ex.printStackTrace();}
		}
		else if (writeChars)
			super.characters(a, s, e);
	}
	

	@SuppressWarnings("unchecked")
	private void parseXLS(POIFSFileSystem poifs) throws IOException, SAXException{
		
		int columnCount = 0;
		int rowCount = 0;
		AttributesImpl emptyAttrs = new AttributesImpl();
		
		wb = new HSSFWorkbook(poifs);
		
		for(int ns=0;ns<wb.getNumberOfSheets();ns++){
			int rowindex = 0;
			HSSFSheet sheet = wb.getSheetAt(ns);
			
			clearSheet(sheet);
			
			HSSFRow row = sheet.getRow(rowindex);
			while(row==null){
				rowindex++;
				row = sheet.getRow(rowindex);
			}
			
			//abbiamo la prima riga buona
			Iterator cellIter = row.cellIterator();
			String cellContent = "";
			String temp ="";
			while(cellIter.hasNext()){
				
				HSSFCell cell = (HSSFCell) cellIter.next();
				temp = getStringContentFromCell(cell);
				if(temp.length()>0)cellContent = cellContent + " " + temp;
			}
			super.startElement("", "title", "title", emptyAttrs); //sparo fuori l'intestazione
			super.characters(cellContent.toCharArray(), 0, cellContent.length());
			super.endElement("", "title", "title");
			
			rowindex++;
			row = sheet.getRow(rowindex);
			while(row==null){
				rowindex++;
				if(rowindex==65536)
					break;
				row = sheet.getRow(rowindex);
			}
			if(rowindex==65536)continue;
			//abbiamo la seconda riga buona 
	
			String style = "tabella_area";
			String node = "";
	
			cellIter = row.cellIterator();
			cellContent = "";
			while(cellIter.hasNext()){
				columnCount++;
				HSSFCell cell = (HSSFCell) cellIter.next();
				cellContent = getStringContentFromCell(cell);
				
				node = "upper-" + columnCount + "-";
				super.startElement("", node+"text", node+"text", emptyAttrs); //sparo fuori l'intestazione
				super.characters(cellContent.toCharArray(), 0, cellContent.length());
				super.endElement("", node+"text", node+"text");
				
				super.startElement("", node+"style", node+"style", emptyAttrs); 	// e lo stile
				super.characters(style.toCharArray(), 0, style.length());
				super.endElement("", node+"style", node+"style");
				
			}
			
			Iterator rowIter = sheet.rowIterator();
			for(int t=0;t<rowindex;t++)
				rowIter.next();
			
			while(rowIter.hasNext()){
				
				cellContent = "";
				
				row = (HSSFRow) rowIter.next();
				rowCount++;	
				node = "left-" + rowCount + "-";
				
				short p = 0;
				
				HSSFCell cell = row.getCell(p);
				cellContent = getStringContentFromCell(cell);
				
				super.startElement("", node+"text", node+"text", emptyAttrs); 	//sparo fuori l'intestazione della riga
				super.characters(cellContent.toCharArray(), 0, cellContent.length());
				super.endElement("", node+"text", node+"text");
				
				super.startElement("", node+"style", node+"style", emptyAttrs); // e lo stile
				super.endElement("", node+"style", node+"style");
				
				int counter = 0;
				p++;
				while(counter<columnCount){
					cellContent = "";
					counter++;
					node = "field-"+rowCount+"-"+counter;
					super.startElement("", node, node, emptyAttrs); 			//sparo il campo
					try{
						cell = row.getCell(p);
						cellContent = getStringContentFromCell(cell);
						
						//if(cellContent.length()>0)System.out.println(cellContent);
						super.characters(cellContent.toCharArray(), 0, cellContent.length());
					}catch(Exception ex1){ex1.printStackTrace();}
					super.endElement("", node, node);
					p++;
				
				}
			}
			
			String nr = String.valueOf(rowCount);
			String nc = String.valueOf(columnCount);
			
			super.startElement("", "rows", "rows", emptyAttrs); 	//numero righe
			super.characters(nr.toCharArray(),0,nr.length());
			super.endElement("", "rows", "rows");
			
			super.startElement("", "cols", "cols", emptyAttrs); 	//numero colonne
			super.characters(nc.toCharArray(),0,nc.length());
			super.endElement("", "cols", "cols");
			
			break;
		}
		
	}

	@SuppressWarnings("unchecked")
	private void clearSheet(HSSFSheet sheet) {
				
		Iterator ri = sheet.rowIterator();
		HSSFRow row = null;
		Vector<HSSFRow> rowsToRemove = new Vector<HSSFRow>();
		while(ri.hasNext()){
			row = (HSSFRow) ri.next();
			if(isRowEmpty(row))
				rowsToRemove.add(row);
		}
		
		ri = rowsToRemove.iterator();
		while(ri.hasNext()){
			row = (HSSFRow) ri.next();
			sheet.removeRow(row);
		}
		
		ri = sheet.rowIterator();
		while(ri.hasNext()){
			row = (HSSFRow) ri.next();
			
		}
		
	}

	@SuppressWarnings("unchecked")
	private boolean isRowEmpty(HSSFRow row) {
		boolean empty = true;
		Iterator cellIter = row.cellIterator();
		HSSFCell cell = null;
		while(cellIter.hasNext()){
			 cell = (HSSFCell) cellIter.next();
			 if(cell.getCellType() != HSSFCell.CELL_TYPE_BLANK){
				 empty = false;
				 return empty;
			 }
		}
		
		return empty;
	}

	private void parseTable(BufferedReader br) throws IOException, SAXException {
		int columnCount = 0;
		int rowCount = 0;
		AttributesImpl emptyAttrs = new AttributesImpl();
		
		String row = br.readLine(); //prima riga - Intestazioni
		if ( row == null )return; // se è vuota -> esco
		String[] fields = row.split(";",-1);
		//columnCount = fields.length-1; //guardo quante colonne ho
		
		String style = "tabella_area";
		String node = "";
		for(int i=1 ; i<fields.length ; i++){ //per ogni colonna
			
			if(fields[i].startsWith("\"")){
				while(isNotComplete(fields[i])){
					i++;
					fields[i] = fields[i-1] + ";" + fields[i];
				}
				fields[i] = fields[i].substring(1, fields[i].length()-1).replaceAll("\"\"", "\"");
			}
			columnCount++;
			node = "upper-" + columnCount + "-";
			super.startElement("", node+"text", node+"text", emptyAttrs); //sparo fuori l'intestazione
			super.characters(fields[i].toCharArray(), 0, fields[i].length());
			super.endElement("", node+"text", node+"text");
			
			super.startElement("", node+"style", node+"style", emptyAttrs); 	// e lo stile
			super.characters(style.toCharArray(), 0, style.length());
			super.endElement("", node+"style", node+"style");
		}
		
		row = br.readLine(); //leggo la seconda riga (che è la prima con i dati)
		while(row!=null){	 //per questa ed ogni seguente riga

			fields = row.split(";",-1);
			
			if(fields.length>0){
				rowCount++;		//conto la riga
				
				node = "left-" + rowCount + "-";
				int p = 0;
				
				if(fields[p].startsWith("\"")){
					while(isNotComplete(fields[p])){
						p++;
						fields[p] = fields[p-1] + ";" + fields[p];
					}
					fields[p] = fields[p].substring(1, fields[p].length()-1).replaceAll("\"\"", "\"");
					
				}
			
			
				super.startElement("", node+"text", node+"text", emptyAttrs); 	//sparo fuori l'intestazione della riga
				super.characters(fields[p].toCharArray(), 0, fields[p].length());
				super.endElement("", node+"text", node+"text");
				
				super.startElement("", node+"style", node+"style", emptyAttrs); // e lo stile
				super.endElement("", node+"style", node+"style");
				
				
				int counter = 0;
				//int i=p+1;
				p++;
				while(counter<columnCount){
						
				//for(int i=p+1 ; i<columnCount+1 ; i++){							// e per ogni campo della riga
					
					if(fields[p].startsWith("\"")){
						while(isNotComplete(fields[p])){
							p++;
							fields[p] = fields[p-1] + ";" + fields[p];
						}
						fields[p] = fields[p].substring(1, fields[p].length()-1).replaceAll("\"\"", "\"");
					}
					counter++;
					node = "field-"+rowCount+"-"+counter;
					super.startElement("", node, node, emptyAttrs); 			//sparo il campo
					try{
						super.characters(fields[p].toCharArray(), 0, fields[p].length());
					}catch(Exception ex1){}
					super.endElement("", node, node);
					p++;
				}
			}
			row = br.readLine();
			
			
		}
		
		String nr = String.valueOf(rowCount);
		String nc = String.valueOf(columnCount);
		
		super.startElement("", "rows", "rows", emptyAttrs); 	//numero righe
		super.characters(nr.toCharArray(),0,nr.length());
		super.endElement("", "rows", "rows");
		
		super.startElement("", "cols", "cols", emptyAttrs); 	//numero colonne
		super.characters(nc.toCharArray(),0,nc.length());
		super.endElement("", "cols", "cols");

		
	}

	private boolean isNotComplete(String string) {
		int count = 0;
		char[] temp = string.toCharArray();
		for(int i=0;i<temp.length;i++){
			if(temp[i] == '"'){
				count++;
			}
		}
		return count%2 == 1;
	}
	
	private String getStringContentFromCell(HSSFCell cell){
		String ret="";
		if(cell!=null){
			int cellType = cell.getCellType();
			short fontcode = cell.getCellStyle().getFontIndex();
			HSSFFont font = wb.getFontAt(fontcode);
			short bweight = font.getBoldweight();
			boolean italic = font.getItalic();
			
			switch(cellType){
				case HSSFCell.CELL_TYPE_BOOLEAN : ret = String.valueOf(cell.getBooleanCellValue()); break;
				case HSSFCell.CELL_TYPE_ERROR : ret = String.valueOf(cell.getErrorCellValue()); break;
				case HSSFCell.CELL_TYPE_FORMULA : ret = cell.getCellFormula(); break;
				case HSSFCell.CELL_TYPE_NUMERIC : {
					Date date = cell.getDateCellValue();
					Calendar c = Calendar.getInstance();
					c.setTime(date);
					ret = c.get(Calendar.DAY_OF_MONTH)+"/"+(c.get(Calendar.MONTH)+1);
					//ret = String.valueOf(cell.getNumericCellValue()); 
					break;
				}
				case HSSFCell.CELL_TYPE_STRING : { 
					HSSFRichTextString richstring = cell.getRichStringCellValue();
					if(richstring != null)
						ret = cell.getRichStringCellValue().getString();
					break;
				}
			}
			if(ret.length()>0){
				if(italic) ret = "''" + ret + "''";
				if(bweight>400) ret = "__" + ret + "__";
				//System.out.println(ret + "\nBold weight: " + bweight);
			}
		}
		
		return ret;
	}
	
}
