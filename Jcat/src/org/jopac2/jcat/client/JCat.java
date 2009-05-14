package org.jopac2.jcat.client;

import com.google.gwt.core.client.EntryPoint;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.form.ValuesManager;
import com.smartgwt.client.widgets.form.events.ItemKeyPressEvent;
import com.smartgwt.client.widgets.form.events.ItemKeyPressHandler;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.events.ClickEvent;  
import com.smartgwt.client.widgets.events.ClickHandler; 
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;

public class JCat implements EntryPoint {

	
	private final String[] descriptionFields = {
			"MS","Titolo","Titolo proprio = eventuale titolo parallelo : complemento del titolo " +
					"/ formulazione di responsabilità ; successive formulazioni di responsabilità",
					"[ = ][ : ][ / ][ ; ]",
					"TextItem",
			"M","Edizione","Formulazione di edizione / formulazione di responsabilità relativa all'edizione " +
					"; successiva formulazione di responsabilità",
					"[ / ][ ; ]",
					"TextItem",
			"","AREA3","Peculiarità del materiale o del tipo di pubblicazione (ISBD(G))",
					"",
					"TextItem",
			"MS","Editore","Luogo di pubblicazione ; altro luogo di pubblicazione : nome dell'editore (oppure:  " +
					"luogo : editore ; altro luogo : altro editore) . data di pubblicazione",
					"[ ; ][ : ][ . ]",
					"TextItem",
			"MS","Collazione","Indicazione specifica del materiale ed estensione : indicazione di " +
					"illustrazioni ; dimensioni + materiale allegato",
					"[ : ][ ; ][ + ]",
					"TextItem",
			"M","Collana","Titolo della collana . Titolo proprio della sottocollana / formulazioni di " +
					"responsabilità relative alla collana , ISSN ; numerazione all'interno della collana",
					"[ . ][ / ][ , ][ ; ]",
					"TextItem",
			"M","ISBN", "ISBN"," ","TextItem",
			"S","ISSN", "ISSN"," ","TextItem"
			};
	private final String[] dataFields = {
			"M","Note", "Note"," ","TextItem",
			"M","Area disciplinare","Area disciplinare"," ","TextItem",
			"MS","Abstract","Abstract"," ","TextItem",
			"S","Sintesi del posseduto","Sintesi del posseduto"," ","TextItem",
			"M","Bibliografia autore","Bibliografia autore"," ","TextItem",
			"MS","Copertina","Copertina"," ","FileItem",
			"MS","Link DSPACE","Link DSPACE"," ","TextItem",
			"MS","Link OPAC","Link OPAC"," ","TextItem"
	};
	
	public void onModuleLoad() {
		
		final ValuesManager vm = new ValuesManager();  
         
		final TabSet theTabs = new TabSet();  
		theTabs.setWidth(800);  
		theTabs.setHeight(500);  
          
		Tab descrizione = new Tab();  
		descrizione.setTitle("Descrizione");
		
		final JcatDynamicForm descriptionForm = new JcatDynamicForm("descriptionForm","ISBD",descriptionFields,true,vm);

		descriptionForm.addItemKeyPressHandler(new ItemKeyPressHandler() {

			public void onItemKeyPress(ItemKeyPressEvent event) {
				FormItem fi = event.getItem();
				String n = fi.getTitle();
				FormItem hint = descriptionForm.getItem(n + "_hint");
				String hintText = (String) hint.getValue();
				String areaText = (String) fi.getValue();

				String newHintText = checkMash(hintText, areaText, fi.getAttributeAsStringArray("syntax"));
				hint.setValue(newHintText);
			}
		});

		descrizione.setPane(descriptionForm);  
 
		Tab dati = new Tab();  
		dati.setTitle("Dati");  
          
		final JcatDynamicForm dataForm = new JcatDynamicForm("dataForm","Dati",dataFields,false,vm); 
		dati.setPane(dataForm);
          
		theTabs.setTabs(descrizione, dati);  
         
		Button submit = new Button();  
		submit.setTitle("Submit");  
		 submit.addClickHandler(new ClickHandler() {  
		       public void onClick(ClickEvent event) {  
		           vm.validate();  
		       if (descriptionForm.hasErrors()) {  
		                     theTabs.selectTab(1);  
		                } else {  
	                    theTabs.selectTab(0);  
	               }  
	            }  
	       });
		 
		 final Toggle status=new Toggle();
		 
		 Button MP = new Button();  
			 MP.setTitle("Monografia / Periodico");  
			 MP.addClickHandler(new ClickHandler() {  
			       public void onClick(ClickEvent event) {
			    	   status.toggle();
			    	   descriptionForm.showType(status.toString());
			    	   dataForm.showType(status.toString());
			    	   descriptionForm.redraw();
			    	   dataForm.redraw();
			       }
		       });
		          
		VLayout vLayout = new VLayout();  
		vLayout.setMembersMargin(10);
		vLayout.addMember(MP); 
		vLayout.addMember(theTabs);  
		vLayout.addMember(submit);  
	 
		vLayout.draw();
	}

	protected String checkMash(String hintText, String areaText, String[] p) {
		hintText=hintText.replaceAll("<b>", "");
		hintText=hintText.replaceAll("</b>", "");
		int i=0;
		
		if(areaText.length()>0) {
			hintText="<b>"+hintText.substring(0,hintText.indexOf(p[0]))+"</b>"+hintText.substring(hintText.indexOf(p[0]));
		}
		
		for(i=0;i<p.length-1;i++) {
			 int k=areaText.indexOf(p[i]);
			 if(k>=0) {
				 hintText=hintText.substring(0,hintText.indexOf(p[i]))+"<b>"+hintText.substring(hintText.indexOf(p[i]),hintText.indexOf(p[i+1]))+"</b>"+hintText.substring(hintText.indexOf(p[i+1]));
			 }
		}

		int k=areaText.indexOf(p[p.length-1]);
		if(k>=0) {
			 hintText=hintText.substring(0,hintText.indexOf(p[i]))+"<b>"+hintText.substring(hintText.indexOf(p[i]))+"</b>";
		 }
		return hintText;
	}
}
