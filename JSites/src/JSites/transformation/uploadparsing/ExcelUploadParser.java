package JSites.transformation.uploadparsing;

import java.io.*;
import java.util.Iterator;
import java.util.Vector;

import org.apache.cocoon.xml.AttributesImpl;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import JSites.transformation.MyAbstractPageTransformer;

public class ExcelUploadParser extends MyAbstractPageTransformer {
	
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
		else{
			if(localName.equals("save") && o.getParameter("upload-file") == null)
				super.startElement("", "nosave", "nosave", attributes);
			else
				super.startElement(namespaceURI, localName, qName, attributes);
		}
			
		

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
		else{
			if(localName.equals("save") && o.getParameter("upload-file") == null)
				super.endElement("", "nosave", "nosave");
			else
				super.endElement(namespaceURI, localName, qName);
		}
			
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
			
			columnCount = 0;
			rowCount = 0;
			
			HSSFSheet sheet = wb.getSheetAt(ns);
			
			String clear = o.getParameter("clean");
			if(clear!=null && clear.equals("on"))
				clearSheet(sheet);
			
			int p=0;
			HSSFRow r = sheet.getRow(p);
			while(r==null){
				p++;
				if(p>1000)break;
				r = sheet.getRow(p);
			}
			if(p>1000)continue;
						
			super.startElement("", "excel", "excel", emptyAttrs);
			
			String title = wb.getSheetName(ns);
			
			super.startElement("", "title", "title", emptyAttrs);
			super.characters(title.toCharArray(), 0, title.length());
			super.endElement("", "title", "title");
			
			columnCount = r.getLastCellNum();
			
			String cellContent = "";
			HSSFRow row = null;
			String node = "";
			
			Iterator<HSSFRow> rowIter = (Iterator<HSSFRow>)sheet.rowIterator();
			
			while(rowIter.hasNext()){
				
				cellContent = "";
				row = rowIter.next();
				rowCount++;	
				node = "left-" + rowCount + "-";
				
				
				for(short c=0;c<columnCount;c++){
					cellContent = "";

					node = "field-"+rowCount+"-"+(c+1);
					super.startElement("", node, node, emptyAttrs); 			//sparo il campo
					try{
						HSSFCell cell = row.getCell(c);
						if(cell==null)cellContent="";
						else
							cellContent = getStringContentFromCell(cell);
						
						super.characters(cellContent.toCharArray(), 0, cellContent.length());
					}catch(Exception ex1){ex1.printStackTrace();}
					super.endElement("", node, node);
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
			
			super.endElement("", "excel", "excel");
		}
		
	}

	@SuppressWarnings("unchecked")
	private void clearSheet(HSSFSheet sheet) {
				
		Iterator<HSSFRow> ri = sheet.rowIterator();
		HSSFRow row = null;
		Vector<HSSFRow> rowsToRemove = new Vector<HSSFRow>();
		while(ri.hasNext()){
			row = ri.next();
			if(isRowEmpty(row))
				rowsToRemove.add(row);
		}
		
		ri = rowsToRemove.iterator();
		while(ri.hasNext()){
			row = ri.next();
			sheet.removeRow(row);
		}
		
		ri = sheet.rowIterator();
		while(ri.hasNext()){
			row = ri.next();
			
		}
		
	}

	@SuppressWarnings("unchecked")
	private boolean isRowEmpty(HSSFRow row) {
		boolean empty = true;
		Iterator<HSSFCell> cellIter = row.cellIterator();
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
					
					int type = cell.getCellStyle().getDataFormat();
					
					switch(type){
						case 170:
						case 177: {
							java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("dd/MM");
							ret = formatter.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()));
							break;
						}
						case 14:
						case 15:
						case 16:
						case 17: {
							java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("dd/MM/yy");
							ret = formatter.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()));
							break;
						}
						case 18:
						case 19:
						case 20:
						case 21:
						case 179: {
							java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("H.mm");
							ret = formatter.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()));
							break;
						}
						default: {
							Double d = new Double(cell.getNumericCellValue());

							if(d - d.intValue() == 0)
								ret = String.valueOf(d.intValue());
							else
								ret = String.valueOf(d);
						}

					}
					
					/*if(HSSFDateUtil.isValidExcelDate(cell.getNumericCellValue())){
						java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("dd/MM/yy");
						ret = formatter.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()));
					}
					else
						ret = String.valueOf(cell.getNumericCellValue());*/
					
					/*Date date = cell.getDateCellValue();
					Calendar c = Calendar.getInstance();
					c.setTime(date);
					ret = c.get(Calendar.DAY_OF_MONTH)+"/"+(c.get(Calendar.MONTH)+1);*/
					//ret = String.valueOf(cell.getNumericCellValue()); 
					//System.out.println(ret + ": " + cell.getCellStyle().getDataFormat());
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
