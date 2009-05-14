package org.jopac2.jcat.client;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.ValuesManager;
import com.smartgwt.client.widgets.form.fields.FileItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.StaticTextItem;
import com.smartgwt.client.widgets.form.fields.TextItem;

public class JcatDynamicForm extends DynamicForm {
	private JcatFieldItem[] jcatFields=null;
	private FormItem[] fi=null;

	public JcatDynamicForm(String id, String title, String[] fields, boolean displayHints, ValuesManager vm) {
		super();
		setID(id);  
		setValuesManager(vm); 
		
		//setGroupTitle(title);
		//setIsGroup(true);
		setWidth("100%");
		setHeight("*");
		setNumCols(2);
		setColWidths(60, "*");
		setBorder("1px solid blue");
		setPadding(5);
		setCanDragResize(true);
		//setResizeFrom("R");
		
		jcatFields=setupFields(fields);
		fi=newArea(displayHints);
		setFields(fi);
	}

	public static JcatFieldItem[] setupFields(String[] f) {
		int n=5;
		JcatFieldItem[] r=new JcatFieldItem[f.length/n];
		for(int i=0;i<f.length/n;i++) {
			r[i]=new JcatFieldItem(f[i*n],f[i*n+1],f[i*n+2],f[i*n+4],parseSyntax(f[i*n+3]));
		}
		return r;
	}
	
	
	private static String[] parseSyntax(String string) {
		if(string.startsWith("[")) string=string.substring(1);
		if(string.endsWith("]")) string=string.substring(0,string.length()-1);
		String[] r=string.split("\\]\\[");
		return r;
	}

	public void showType(String t) {
		for(int i=0;i<fi.length;i++) {
			String t1=fi[i].getAttribute("cattype");
			if(t1.contains(t)) fi[i].setVisible(new Boolean(true));
			else fi[i].setVisible(new Boolean(false));
		}
	}

	private FormItem[] newArea(boolean hints) {
		final FormItem[] fi = new FormItem[jcatFields.length*2+1];
		
		FileItem fileItem=new FileItem();
		fi[jcatFields.length*2]=fileItem;
		
		
		for (int i = 0; i < jcatFields.length; i++) {
			fi[i * 2] = new TextItem(); //new TextAreaItem();
			fi[i * 2].setTitle(jcatFields[i].getName());
			fi[i * 2].setWidth("*");
			fi[i * 2].setAttribute("cattype", jcatFields[i].getType());
			fi[i * 2].setAttribute("syntax", jcatFields[i].getSyntax());

			StaticTextItem sti = new StaticTextItem("");
			sti.setValue(jcatFields[i].getHint());
			sti.setHeight(30);
			sti.setTitle("");
			sti.setName(jcatFields[i].getName() + "_hint");
			if(hints) {
				sti.setAttribute("cattype", jcatFields[i].getType());
			}
			else {
				sti.setAttribute("cattype", "");
				sti.setVisible(new Boolean(false));
			}
			fi[i * 2 + 1] = sti;
		}
		
		return fi;
	}
}
