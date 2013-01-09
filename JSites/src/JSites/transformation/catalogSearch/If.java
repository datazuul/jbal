package JSites.transformation.catalogSearch;

import java.text.NumberFormat;
import java.util.Locale;

import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.MapContext;
import org.apache.commons.lang.StringEscapeUtils;
import org.w3c.dom.Document;

/**
 * 
 * @author romano
 * If, prende in arg[0] un'espressione  booleana in java, ed esegue il ramo else solo se è vera.
 * Si basa su Apache Common jexl2.1.1
 * Esempio:
 * 
 * {{^If((("[[/record/prezzo]]").length()!=0)&&((_NumberFormatItalian.parse("[[/record/prezzo]]")).doubleValue()!=0))::&#8364; [[/record/prezzo]]}}
 * 
 * ATTENZIONE: il valutatore non istanzia variabili, quindi non e' possibile usare i metodi statici delle classi. Per ovviare a questo
 * nel contesto del valutatore sono state caricate delle variabili predefinite:
 * 
 * 
 *				jctx.set("_NumberFormatItalian", NumberFormat.getInstance(Locale.ITALIAN));
 *				jctx.set("_Integer", new Integer(0));
 *				jctx.set("_String", new String(""));
 *				jctx.set("_Double", new Double(0));
 */


public class If implements TextFunction {

	public String parse(Document doc, String in, String... args) throws Exception {
			String expression="";
			String r="";
			try {
				expression=Templator.parseContext(doc, "{{/::"+args[0]+"}}");
				
				JexlEngine jexl = new JexlEngine();
				// Create an expression object
				
				Expression e = jexl.createExpression( expression );
				 
				// Create a context and add data
				JexlContext jctx = new MapContext();
				jctx.set("_Double", new Double(0));
				jctx.set("_NumberFormatItalian", NumberFormat.getInstance(Locale.ITALIAN));
				jctx.set("_Integer", new Integer(0));
				jctx.set("_String", new String(""));
				 
				// Now evaluate the expression, getting the result
				Boolean test = (Boolean)e.evaluate(jctx);
				
				if(test) {
					r=Templator.parseContext(doc, "{{/::"+in+"}}");
				}
				
//				
			}
			catch(Exception e) {
				r=StringEscapeUtils.escapeXml(e.getMessage()+"<br/>"+expression);
//				temp=args[0];
			}
//			int length=temp.length();
//			int limit=Integer.MAX_VALUE;
//			
//			try {
//				limit=Integer.parseInt(args[1]);
//			}
//			catch(Exception e) {}
//			
//			if(limit<length) {
//				int u=temp.lastIndexOf(" ",limit);
//				temp=temp.substring(0,u);
//				if(args.length>2) {
//					String t2=Templator.parseContext(doc, "{{/:"+args[2]+"}}");
//					temp=temp+t2;
//				}
//			}
			
			
			return r;
	}

	
}
