/*
 * Copyright 2008 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.jopac2.jcat.client.util.impl;

import java.util.ArrayList;

import org.jopac2.jcat.client.util.DynamicCallbackForm;
import org.jopac2.jcat.client.util.DynamicFormSubmitCompleteEvent;

import com.google.gwt.user.client.ui.FormSubmitCompleteEvent;

/**
 * Helper class for widgets that accept
 * {@link com.google.gwt.user.client.ui.FormHandler FormHandlers}. This subclass
 * of ArrayList assumes that all items added to it will be of type
 * {@link com.google.gwt.user.client.ui.FormHandler}.
 */
@SuppressWarnings("serial")
public class DynamicCallbackFormHandlerCollection extends
		ArrayList<DynamicFormHandler> {

	/**
	 * Fires a {@link FormHandler#onSubmitComplete(FormSubmitCompleteEvent)} on
	 * all handlers in the collection.
	 * 
	 * @param sender
	 *            the object sending the event
	 * @param results
	 *            the results of the form submission
	 */
	public void fireOnComplete(DynamicCallbackForm sender, String results) {
		DynamicFormSubmitCompleteEvent event = new DynamicFormSubmitCompleteEvent(
				sender, results);
		for (DynamicFormHandler handler : this) {
			handler.onSubmitComplete(event);
		}
	}

}
