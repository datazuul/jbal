package org.jopac2.test;

import org.jopac2.engine.utils.SingleSearch;

public class MetaSearchExample {

	/**
	 * @param args
	 * 
	 * 
	newclient.cmdOpen("opac.units.it:2100");
    newclient.cmdBase("sebinaopac");
    newclient.cmdFormat("sutrs");
    newclient.cmdFind("@attrset bib-1 @attr 1=1 \"trampus\"");
    //newclient.cmdFind("@attrset bib-1 @attr 1=1032 \"IT\\\\ICCU\\\\AQ1\\\\0064742\"");

    newclient.cmdElements("F");
    newclient.cmdShow("1");
    newclient.disconnect();
	 */
	public static void main(String[] args) {
		// 10.0.0.22,2100,sebinaopac,JOpac2.Managers.JOpac2Z3950RequestManager,Sutrs:Sol_sutrs,a,a,0,20000

		try {
			SingleSearch singleSearch=new SingleSearch("opac.units.it", 2100, "sutrs:Sol_sutrs", null, null, null, null, 0, 0, null, null, null);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
