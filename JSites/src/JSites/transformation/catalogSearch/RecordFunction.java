package JSites.transformation.catalogSearch;

import javax.xml.parsers.ParserConfigurationException;

import org.jopac2.jbal.RecordInterface;
import org.w3c.dom.Document;

public interface RecordFunction {
	/*
	 <record>
	 	<a>uno</a>
	 	<b>due</b>
	 	<c>tre</c>
	 </record>
	 
	 
	 <record>
	 	<a>uno</a>
	 	<b>xxx</b>
	 	<c>yyy</c>
	 </record>
	 
	 ==============
	 
	 <record>
	 	<a>uno</a>
	 	<item>
	 		<b>due</b>
	 		<c>tre</c>
	 	</item>
	 	<item>
	 		<b>xxx</b>
	 		<c>yyy</c>
	 	</item>
	 </record>
	 
	 */

	/**
	 * @throws Exception 
	 * 
	 */
	public Document[] processRecords(Document aDoc, Document bDoc, String... args) throws Exception;
	
}
