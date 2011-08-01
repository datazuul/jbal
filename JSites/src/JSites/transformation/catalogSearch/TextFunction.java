package JSites.transformation.catalogSearch;

import org.w3c.dom.Document;

public interface TextFunction {

	/**
	 * Example:
	 * {{^Group(,|item|/record/primo|/record/secondo):<a href='[[primo]]'>[[secondo]]</a>}}
	 * @param doc
	 * @param in = <a href='[[primo]]'>[[secondo]]</a>
	 * @param args = '', 'item', '/record/primo', '/record/secondo'
	 * @return
	 * @throws Exception
	 */
	String parse(Document doc, String in, String... args) throws Exception;

}
