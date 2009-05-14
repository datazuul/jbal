package org.jopac2.jcat.client.util.impl;

import java.util.EventListener;

import org.jopac2.jcat.client.util.DynamicFormSubmitCompleteEvent;

public interface DynamicFormHandler extends EventListener {
	  /**
	   * Fired when a form has been submitted successfully.
	   * 
	   * @param event an event object containing information about the form
	   *          submission
	   */
	  void onSubmitComplete(DynamicFormSubmitCompleteEvent event);

}
