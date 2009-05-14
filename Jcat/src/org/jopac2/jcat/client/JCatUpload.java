package org.jopac2.jcat.client;

import org.jopac2.jcat.client.util.DynamicCallbackForm;
import org.jopac2.jcat.client.util.DynamicFormSubmitCompleteEvent;
import org.jopac2.jcat.client.util.impl.DynamicFormHandler;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.smartgwt.client.types.Encoding;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.HiddenItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.UploadItem;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * 
 * http://code.google.com/p/smartgwt/issues/detail?id=17
 * http://forums.smartclient.com/showthread.php?t=3102
 * 
 * @author romano
 *
 */

public class JCatUpload {
	public void onModuleLoad() {  
 	VLayout layout = new VLayout();
	
	final DynamicCallbackForm uploadForm = new DynamicCallbackForm("hidden_frame");		
	uploadForm.setEncoding(Encoding.MULTIPART);
	UploadItem fileItem = new UploadItem("image");
	TextItem nameItem = new TextItem("imageName");
	TextItem descriptionItem = new TextItem("description");
	HiddenItem spaceImageIdItem = new HiddenItem("spaceImageId");
	HiddenItem propertyIdItem = new HiddenItem("propertyId");
	propertyIdItem.setValue(23);
	spaceImageIdItem.setValue(0);
	//uploadForm.setTarget("hidden_frame");
	uploadForm.setAction(GWT.getModuleBaseURL()+"/UploadFileServlet");
	IButton uploadButton = new IButton("Upload...");
	uploadButton.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler(){
		public void onClick(ClickEvent e){
			uploadForm.submitForm();
		}
	});
		
	uploadForm.setItems(fileItem, nameItem, descriptionItem, spaceImageIdItem, propertyIdItem);
	
	uploadForm.addFormHandler(new DynamicFormHandler() {
		public void onSubmitComplete(DynamicFormSubmitCompleteEvent event) {
	        Window.alert(event.getResults());
		}});
	
	
	layout.setMembers(uploadForm, uploadButton);

	layout.draw();
	//RootPanel.get("tree1").add(layout);


	}
	
/*
 * @RequestMapping("/imageUploadRest.do")
@Transactional
public void processImageUploadRest(
		@RequestParam("imageName") String imageName, 
		@RequestParam("description") String description,
		@RequestParam("spaceImageId") String spaceImageId,		
		@RequestParam("image") MultipartFile image,
		@RequestParam("propertyId") String propertyId,
		HttpServletResponse response) throws IOException, Exception {

	doImageSave(imageName, description, spaceImageId, image, propertyId);

	PrintWriter out = response.getWriter();
	out.println("<response>");  
	out.println("<data>");
	out.println("<status>0</status>");	 
	out.println("</data>  ");
	out.println("</response>  ");		
}

@Transactional
public void doImageSave(String imageName, 
					 String description,
					 String spaceImageId,		
					 MultipartFile image,
					 String propertyId ) throws IOException, Exception {
	Long _spaceImageId = new Long(spaceImageId);
	ImageBlob imageBlob = new ImageBlob(image.getContentType(), image.getInputStream());
	imageBlobDAO.create(imageBlob);
}

 */
}
