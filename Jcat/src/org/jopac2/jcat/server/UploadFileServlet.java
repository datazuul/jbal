package org.jopac2.jcat.server;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * Servlet implementation class for Servlet: UploadFileServlet
 */
public class UploadFileServlet extends HttpServlet implements Servlet {

	private static final long serialVersionUID = 8305367618713715640L;
	
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/plain");
		
		String down=request.getParameter("download");
		
		if(down==null) {
			FileItem uploadItem = getFileItem(request);
			if (uploadItem == null) {
				response.getWriter().write("NO-SCRIPT-DATA");
				return;
			}
	
			String fileName=null;
			try {
				fileName=saveFile(uploadItem, request.getSession().getId());
			} catch (Exception e) {
				e.printStackTrace();
			}

			String pathName = getPathName(request.getSession().getId());
			
			response.getWriter().write("File uploaded");
		}
		
	}




	private String getPathName(String id) throws IOException {
		// TODO ottimizzare, le properties non ha senso caricarle a ogni request
		InputStream is = this.getClass().getResourceAsStream(
				"jcat.properties");
		Properties p = new Properties();
		p.load(is);
		is.close();
		return p.getProperty("tempUserDir") + "/" + id;
	}

	private String saveFile(FileItem uploadItem, String id) throws Exception {
		String pathname = getPathName(id);

		File dir = new File(pathname);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		//estrae il nome del file senza il path (su windows diverso da mac)
		String name=uploadItem.getName();
		if(name.contains("\\")) {
			name=name.substring(name.lastIndexOf("\\")+1);
		}
		name=(new File(name)).getName();
		uploadItem.write(new File(pathname + "/" + name));
		
		
		// System.out.println(new String(fileContents));
		return name;
	}

	@SuppressWarnings("unchecked")
	private FileItem getFileItem(HttpServletRequest request) {
		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);

		try {
			List items = upload.parseRequest(request);
			Iterator it = items.iterator();
			while (it.hasNext()) {
				FileItem item = (FileItem) it.next();
				if (!item.isFormField()
						&& "uploadFormElement".equals(item.getFieldName())) {
					return item;
				}
			}
		} catch (FileUploadException e) {
			return null;
		}
		return null;
	}

}
