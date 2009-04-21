package JSites.transformation;

import org.apache.cocoon.environment.Session;
import org.xml.sax.SAXException;

public class EmptySession extends MyAbstractPageTransformer {
	
	public void endDocument() throws SAXException{
		
		Session s = this.sessionManager.getSession(false);
		if(s!=null){
			s.removeAttribute("newPageID");
			s.removeAttribute("newPacid");
		}

		super.endDocument();
	}

}
