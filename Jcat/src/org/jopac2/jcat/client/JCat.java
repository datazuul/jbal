package org.jopac2.jcat.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.PopupPanel;
import com.smartgwt.client.core.DataClass;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.JSOHelper;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.events.CloseClientEvent;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.SearchForm;
import com.smartgwt.client.widgets.form.ValuesManager;
import com.smartgwt.client.widgets.form.events.ItemChangedEvent;
import com.smartgwt.client.widgets.form.events.ItemChangedHandler;
import com.smartgwt.client.widgets.form.events.ItemKeyPressEvent;
import com.smartgwt.client.widgets.form.events.ItemKeyPressHandler;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.FormItem;  
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.grid.CellFormatter;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;  

import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuButton;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.MenuItemSeparator;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;

public class JCat implements EntryPoint {
	
	private JcatDynamicForm descriptionFormMonografie=null, dataFormMonografie=null;
	private JcatDynamicForm descriptionFormPeriodici=null, dataFormPeriodici=null;;
	private TabSet theTabs=null;
	private VLayout vLayout=null;
	final Window searchWindow = new Window();  

	
	private final String[] descriptionFieldsMonografie = {
			"M","Titolo","Titolo proprio = eventuale titolo parallelo : complemento del titolo " +
					"/ formulazione di responsabilità ; successive formulazioni di responsabilità",
					"[ = ][ : ][ / ][ ; ]",
					"TextItem",
			"M","Edizione","Formulazione di edizione / formulazione di responsabilità relativa all'edizione " +
					"; successiva formulazione di responsabilità",
					"[ / ][ ; ]",
					"TextItem",

			"M","Editore","Luogo di pubblicazione ; altro luogo di pubblicazione : nome dell'editore (oppure:  " +
					"luogo : editore ; altro luogo : altro editore) . data di pubblicazione",
					"[ ; ][ : ][ . ]",
					"TextItem",
			"M","Collazione","Indicazione specifica del materiale ed estensione : indicazione di " +
					"illustrazioni ; dimensioni + materiale allegato",
					"[ : ][ ; ][ + ]",
					"TextItem",
			"M","Collana","Titolo della collana . Titolo proprio della sottocollana / formulazioni di " +
					"responsabilità relative alla collana , ISSN ; numerazione all'interno della collana",
					"[ . ][ / ][ , ][ ; ]",
					"TextItem",
			"M","ISBN", "ISBN"," ","TextItem",
			"M","Copertina","Copertina"," ","FileItem"
			};
	
	private final String[] descriptionFieldsPeriodici = {
			"S","Titolo","Titolo proprio = eventuale titolo parallelo : complemento del titolo " +
			"/ formulazione di responsabilità ; successive formulazioni di responsabilità",
			"[ = ][ : ][ / ][ ; ]",
			"TextItem",
	"S","Editore","Luogo di pubblicazione ; altro luogo di pubblicazione : nome dell'editore (oppure:  " +
			"luogo : editore ; altro luogo : altro editore) . data di pubblicazione",
			"[ ; ][ : ][ . ]",
			"TextItem",
	"S","Collazione","Indicazione specifica del materiale ed estensione : indicazione di " +
			"illustrazioni ; dimensioni + materiale allegato",
			"[ : ][ ; ][ + ]",
			"TextItem",
	"S","ISSN", "ISSN"," ","TextItem"
	};
	
	private final String[] dataFieldsMonografie = {
			"M","Note", "Note"," ","TextItem",
			"M","Area disciplinare","Area disciplinare"," ","TextItem",
			"M","Abstract","Abstract"," ","TextItem",
			"M","Bibliografia autore","Bibliografia autore"," ","TextItem",
			"M","Copertina","Copertina"," ","FileItem",
			"M","Link DSPACE","Link DSPACE"," ","TextItem",
			"M","Link OPAC","Link OPAC"," ","TextItem"
	};
	
	private final String[] dataFieldsPeriodici = {
			"S","Abstract","Abstract"," ","TextItem",
			"S","Sintesi del posseduto","Sintesi del posseduto"," ","TextItem",
			"S","Copertina","Copertina"," ","FileItem",
			"S","Link DSPACE","Link DSPACE"," ","TextItem",
			"S","Link OPAC","Link OPAC"," ","TextItem"
	};
	
	public void onModuleLoad() {
		MenuButton menuIns=menuInserisci();
		Button menuCer=menuCerca();
		
		final ValuesManager vmMonografie = new ValuesManager();  
		final ValuesManager vmPeriodici = new ValuesManager();  
         
		theTabs = new TabSet();  
		theTabs.setWidth(900);  
		theTabs.setHeight(500);

		setupSearchWindow();
		
		descriptionFormMonografie = new JcatDynamicForm("descriptionFormMonografie","descriptionFormMonografie",descriptionFieldsMonografie,true,vmMonografie);
		descriptionFormMonografie.addItemKeyPressHandler(new ItemKeyPressHandler() {
			public void onItemKeyPress(ItemKeyPressEvent event) {
				FormItem fi = event.getItem();
				String n = fi.getTitle();
				FormItem hint = descriptionFormMonografie.getItem(n + "_hint");
				String hintText = (String) hint.getValue();
				String areaText = (String) fi.getValue();

				String newHintText = JcatDynamicForm.checkMash(hintText, areaText, fi.getAttributeAsStringArray("syntax"));
				hint.setValue(newHintText);
			}
		});
		dataFormMonografie = new JcatDynamicForm("dataFormMonografie","dataFormMonografie",dataFieldsMonografie,false,vmMonografie);
		
		descriptionFormPeriodici = new JcatDynamicForm("descriptionFormPeriodici","descriptionFormPeriodici",descriptionFieldsPeriodici,true,vmMonografie);
		descriptionFormPeriodici.addItemKeyPressHandler(new ItemKeyPressHandler() {
			public void onItemKeyPress(ItemKeyPressEvent event) {
				FormItem fi = event.getItem();
				String n = fi.getTitle();
				FormItem hint = descriptionFormPeriodici.getItem(n + "_hint");
				String hintText = (String) hint.getValue();
				String areaText = (String) fi.getValue();

				String newHintText = JcatDynamicForm.checkMash(hintText, areaText, fi.getAttributeAsStringArray("syntax"));
				hint.setValue(newHintText);
			}
		});
		dataFormPeriodici = new JcatDynamicForm("dataFormPeriodici","dataFormPeriodici",dataFieldsPeriodici,false,vmPeriodici); 
		
		vLayout = new VLayout();  
		vLayout.setMembersMargin(10);
		vLayout.addMember(menuIns);
		vLayout.addMember(menuCer);
		//vLayout.addMember(MP); 
		vLayout.addMember(theTabs);  
//		vLayout.addMember(submit);  
	 
		vLayout.draw();
	}

	private void setupSearchWindow() {
		searchWindow.setWidth(800);  
		searchWindow.setHeight(400);
		searchWindow.setTitle("Search");  
		searchWindow.setShowMinimizeButton(false);  
		searchWindow.setIsModal(true);  
		searchWindow.centerInPage();  
		searchWindow.setCanDragReposition(true);  
		searchWindow.setCanDragResize(true);
		
		
		final DataSource dataSource = RecordFinder.getInstance();
   
		ListGridField rowNum = new ListGridField("JID", "JID");  
		rowNum.setWidth(40);  
		rowNum.setCellFormatter(new CellFormatter() {  
		     public String format(Object value, ListGridRecord record, int rowNum, int colNum) {  
		        return rowNum +"";  
		     }  
		});  
   
        ListGridField description = new ListGridField("ISBD", 700);  
 
        final ListGrid listGrid = new ListGrid();  
        listGrid.setWidth100();  
        listGrid.setHeight100();  
        listGrid.setAutoFetchData(false);  
        listGrid.setDataSource(dataSource);  
        listGrid.setShowAllRecords(false);
  
        listGrid.setFields(rowNum, description);
        
        final TextItem isbd = new TextItem("ISBD");  
        
        final DynamicForm filterForm = new DynamicForm();
		filterForm.setIsGroup(true);  
		filterForm.setGroupTitle("Search");  
		filterForm.setNumCols(6);  
		filterForm.setDataSource(dataSource);  
		filterForm.setAutoFocus(false);
		
		ButtonItem button = new ButtonItem();  
      button.setTitle("Search");  
      button.setStartRow(false);  
      button.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {  
          public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
        	  if(((String)isbd.getValue()).length()==0) {
        		  listGrid.fetchData(new Criteria("ISBD","null"));
        	  }
        	  else {
        		  listGrid.fetchData(filterForm.getValuesAsCriteria());
        	  }
          }  
      }); 
		
		
        filterForm.setFields(isbd,button);  
          
        filterForm.addItemChangedHandler(new ItemChangedHandler() {  
            public void onItemChanged(ItemChangedEvent event) {
            	if(((String)isbd.getValue()).length()==0) {
          		  listGrid.fetchData(new Criteria("ISBD","null"));
          	  }
          	  else {
          		  listGrid.fetchData(filterForm.getValuesAsCriteria());
          	  }
            }  
        });
        
        
        listGrid.addRecordClickHandler(new RecordClickHandler() {
            public void onRecordClick(RecordClickEvent event) {
            	ListGridRecord rec=listGrid.getSelectedRecord();
            	String jid=rec.getAttributeAsString("JID");
            	updateData(jid);
            }
        });
        
//        final SearchForm form = new SearchForm();
//        form.setDataSource(RecordFinder.getInstance());
//        form.setTop(50);  
//        form.setNumCols(3);  
//        TextItem query = new TextItem();  
//        query.setName("query");  
//        query.setTitle("Query");  
//        query.setDefaultValue("snowboarding");  
  
//        ButtonItem button = new ButtonItem();  
//        button.setTitle("Search");  
//        button.setStartRow(false);  
//        button.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {  
//            public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {  
//                listGrid.fetchData(form.getValuesAsCriteria());  
//            }  
//        }); 
//  
//        form.setItems(query, button);  
//        listGrid.draw();  
		
		
		VLayout layout=new VLayout();
		layout.addMember(filterForm);
		layout.addMember(listGrid);
		searchWindow.addItem(layout);
		searchWindow.hide();
        
		searchWindow.addCloseClickHandler(new CloseClickHandler() {  
            public void onCloseClick(CloseClientEvent event) {  
            	searchWindow.hide();  
            }  
         });
	}
	
	protected void updateData(String jid) {
		removeAllTabs();
    	Tab descrizioneMonografie = new Tab(), datiMonografie = new Tab();   
		descrizioneMonografie.setTitle("Descrizione Monografie");
		datiMonografie.setTitle("Dati Monografie");
		descrizioneMonografie.setPane(descriptionFormMonografie);
		datiMonografie.setPane(dataFormMonografie);
    	theTabs.setTabs(descrizioneMonografie, datiMonografie);
    	theTabs.selectTab(0);

    	JcatServerAsync myService = JcatServer.Util.getInstance();
		AsyncCallback<Object> callback = new AsyncCallback<Object>() {
			public void onSuccess(Object result) {
				RecordInfo ri=(RecordInfo)result;
				descriptionFormMonografie.setValue("Titolo", ri.getTitolo());
				descriptionFormMonografie.setValue("Edizione", ri.getEdizione());
				descriptionFormMonografie.setValue("Editore", ri.getEditore());
				descriptionFormMonografie.setValue("Collazione", ri.getCollazione());
				descriptionFormMonografie.setValue("Collana", ri.getCollazione());
				descriptionFormMonografie.setValue("ISBN", ri.getNumerostandard());
			}

			public void onFailure(Throwable caught) {
				PopupPanel p = new PopupPanel();
				p.add(new Label(caught.toString()));
				searchWindow.addChild(p);
			}
		};
		myService.getRecord(jid, callback);
	}

	private MenuButton menuInserisci() {
		Menu menu = new Menu();  
		menu.setShowShadow(true);  
		menu.setShadowDepth(10);
	
		MenuItem monografiaItem,collanaItem,periodicoItem,fascicoloItem;

  
		monografiaItem = new MenuItem("Monografia", "icons/16/document_plain_new.png", "Ctrl+M");
		monografiaItem.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {  
  
            public void onClick(final MenuItemClickEvent event) {
            	removeAllTabs();
            	Tab descrizioneMonografie = new Tab(), datiMonografie = new Tab();   
        		descrizioneMonografie.setTitle("Descrizione Monografie");
        		datiMonografie.setTitle("Dati Monografie");
        		descrizioneMonografie.setPane(descriptionFormMonografie);
        		datiMonografie.setPane(dataFormMonografie);
            	theTabs.setTabs(descrizioneMonografie, datiMonografie);
            	theTabs.selectTab(0);
            }


         }); 
		collanaItem = new MenuItem("Collana", "icons/16/disk_blue.png", "Ctrl+C");
		MenuItemSeparator separator = new MenuItemSeparator();  
		periodicoItem = new MenuItem("Periodico", "icons/16/folder_out.png", "Ctrl+P");
		periodicoItem.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {  
			  
            public void onClick(final MenuItemClickEvent event) {  
            	removeAllTabs();
            	Tab descrizionePeriodici = new Tab(), datiPeriodici = new Tab();  
        		descrizionePeriodici.setTitle("Descrizione Periodico");
        		datiPeriodici.setTitle("Dati Periodico");  
        		descrizionePeriodici.setPane(descriptionFormPeriodici);
        		datiPeriodici.setPane(dataFormPeriodici);
            	theTabs.setTabs(descrizionePeriodici, datiPeriodici);
            	theTabs.selectTab(0);
            }


         }); 
		
		fascicoloItem = new MenuItem("Fascicolo", "icons/16/save_as.png","Ctrl+F");
		MenuItem esciItem = new MenuItem("Esci", "icons/16/save_as.png","Ctrl+E");
		
		
		menu.setItems(monografiaItem, collanaItem, separator, periodicoItem, fascicoloItem,  
		      separator, esciItem);  

		return new MenuButton("Inserisci", menu);  
 
	}
	
	private Button menuCerca() {
		Button mb = new Button("Cerca");
		mb.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

			public void onClick(ClickEvent event) {
				searchWindow.show();
			}  
         });  
		return mb; 
 
	}
	


	protected void removeAllTabs() {
		for(int i=theTabs.getTabs().length-1;i>=0;i--) theTabs.removeTab(i);
		
	}
	
	
	
}
