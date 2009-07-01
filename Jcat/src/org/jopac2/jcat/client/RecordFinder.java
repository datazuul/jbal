package org.jopac2.jcat.client;

import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;  
import com.smartgwt.client.data.fields.*;  
 

public class RecordFinder extends DataSource {  

     private static RecordFinder instance = null;  
   
   public static RecordFinder getInstance() {  
         if (instance == null) {  
             instance = new RecordFinder("RecordFinder");  
         }  
         return instance;  
     }
   
   
   
     public RecordFinder(String id) {
         setID(id);
         setRecordXPath("/response/data/record");  
         DataSourceIntegerField pkField = new DataSourceIntegerField("JID");  
         pkField.setHidden(false);  
         pkField.setPrimaryKey(true);  
  
   
         DataSourceTextField descriptionField = new DataSourceTextField("ISBD", "ISBD", 2000);
   
         setFields(pkField, descriptionField);  
   
         setDataURL("http://localhost:8080/Jcat/RecordFinderServlet");  
         setClientOnly(false);          
     }
     
     protected Object transformRequest(DSRequest dsRequest) {
    	 	dsRequest.setContentType("plain/text; charset=utf-8");
                return super.transformRequest(dsRequest);  
             }  
  
            protected void transformResponse(DSResponse response, DSRequest request, Object data) {  
                 super.transformResponse(response, request, data);  
             } 
 }  
