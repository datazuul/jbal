package JSites.components;

/*******************************************************************************
 *
 *  JOpac2 (C) 2002-2005 JOpac2 project
 *
 *     This file is part of JOpac2. http://jopac2.sourceforge.net
 *
 *  JOpac2 is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  JOpac2 is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with JOpac2; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *******************************************************************************/

/**
 * Originally by: 
 * Component to upload file 
 * @author <a href="mailto:n.maisonneuve@hotpop.com">Nicolas Maisonneuve</a>
 * @version 1.0
 */

/*
 * @author	Romano Trampus
 * @version	30/12/2004
 * 
 * @author   Romano Trampus
 * @version	19/05/2005
 */
import org.apache.avalon.framework.context.Context;
import org.apache.avalon.framework.context.ContextException;
import org.apache.avalon.framework.context.Contextualizable;
import org.apache.avalon.framework.logger.AbstractLogEnabled;

import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;

import org.apache.cocoon.servlet.multipart.Part;
import org.apache.cocoon.servlet.multipart.PartOnDisk;
import org.apache.cocoon.Constants;
import java.io.*;

public class FileUploadManagerImpl extends AbstractLogEnabled implements
		FileUploadManager, Configurable, Contextualizable {

	static final String DEFAULT_DIRECTORY = "uploadfolder";

	static final int BUFFER = 4096;

	// private Context env_context;
	File uploadFolder;

	public void contextualize(Context context) throws ContextException {
		this.uploadFolder = (File) context.get(Constants.CONTEXT_UPLOAD_DIR);
	}

	public void configure(Configuration conf) throws ConfigurationException {
		Configuration dirconf = conf.getChild(DEFAULT_DIRECTORY, false);
		if (dirconf != null) {
			String dir = dirconf.getValue("");
			if (!dir.equals("")) {
				this.setUploadFolder(dir);
			}
		}
	}

	public String getUploadFolder() {
		return uploadFolder.getAbsolutePath();
	}

	public void setUploadFolder(String uploadfolder) {
		this.uploadFolder = new File(uploadfolder);
		// Create the upload folder if need be
		if (!uploadFolder.exists()) {
			uploadFolder.mkdirs();
		}
	}

	public String upload(Part source) throws Exception {

		String destfilename = this.uploadFolder.getAbsolutePath()
				+ File.separator + source.getFileName();
		this.upload(source, destfilename);
		return destfilename;
	}

	public void upload(Part source, String destfilename) throws Exception {
		String filename = source.getFileName();
		File destFile = new File(destfilename);

		// Check to see if the object is a PartOnDisk object as this can
		// simply be renamed if it is on the same file system
		if (source instanceof PartOnDisk) {

			getLogger().debug(
					"Renaming " + source.getFileName() + " to " + destFile);
			if (((PartOnDisk) source).getFile().renameTo(destFile)) {
				getLogger().debug("Successfully renamed ");
			}
			// Maybe it's on a different filesystem...
			else if (copyFile(source.getInputStream(), new FileOutputStream(
					destFile))) {
				getLogger().debug(
						"Successfully copied " + filename + " to " + destFile);
			} else {
				getLogger()
						.error(
								"FAILED to rename/copy " + filename + " to "
										+ destFile);
				throw new Exception("FAILED to rename/copy " + filename
						+ " to " + destFile);
			}
		} else {
			getLogger().debug("Streaming file to" + destFile);
			if (copyFile(source.getInputStream(),
					new FileOutputStream(destFile))) {
				getLogger().debug("Successfully copied stream to " + destFile);
			} else {
				getLogger().error("FAILED to copy stream to " + destFile);
				throw new Exception("FAILED to copy stream to " + destFile);
			}
		}
	}

	/**
	 * Copy Stream
	 * 
	 * @param in
	 * @param out
	 * @return
	 */
	private boolean copyFile(InputStream in, FileOutputStream out) {
		byte[] buffer = new byte[BUFFER];
		int read;
		try {
			read = in.read(buffer);

			while (read > 0) {
				out.write(buffer, 0, read);
				read = in.read(buffer);
			}
			out.close();
		} catch (IOException ex) {
			return false;
		}
		return true;
	}

}
