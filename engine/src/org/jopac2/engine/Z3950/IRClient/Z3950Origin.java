/*******************************************************************************
*
*  JOpac2 (C) 2002-2007 JOpac2 project
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
* @author	Iztok Cergol
* @version	18/08/2004
*/
// Title:       Z3950Origin
// @version:    $Id: Z3950Origin.java,v 1.2 2007-01-27 18:09:14 romano Exp $
// Copyright:   Copyright (C) 1999,2000 Knowledge Integration Ltd.
// @author:     Ian Ibbotson (ibbo@k-int.com)
// Company:     KI
// Description: 
//


//
// This program is free software; you can redistribute it and/or
// modify it under the terms of the GNU General Public License
// as published by the Free Software Foundation; either version 2.1 of
// the license, or (at your option) any later version.
// 
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
// 
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place - Suite
// 330, Boston, MA  02111-1307, USA.
// 

package org.jopac2.engine.Z3950.IRClient;

import java.io.*;
import java.util.*;
import java.io.IOException;

import com.k_int.gen.Z39_50_APDU_1995.*;

//import JOpac2.utils.*;

// for OID Register
import com.k_int.codec.util.*;

// Information Retrieval Interfaces
import com.k_int.IR.*;

// For logging
import com.k_int.util.LoggingFacade.*;
import com.k_int.z3950.IRClient.*;
import com.k_int.z3950.util.*;

// For weak reference to SearchTask
import java.lang.ref.*;
@SuppressWarnings("unchecked")
public class Z3950Origin extends com.k_int.z3950.IRClient.Z3950Origin implements APDUListener, Searchable, Scanable
{
  protected java.util.Properties properties = null;

  private ZEndpoint assoc             = null;
  private boolean assoc_is_accepting_searches   = false;
  private OIDRegister reg             = null;
  //private int portnum              = 0;
  //private String hostname            = null;
  private Vector db_names             = new Vector();

  // Hashtable of **weak** references to active search objects
  private Hashtable active_searches     = new Hashtable();
  private Hashtable outstanding_requests   = new Hashtable();

  private int ref_counter;
  private String recsyn_to_use   = null;
  private String target_name   = null;
  private String target_id     = null;
    
  // Authentication settings
  @SuppressWarnings("unused")
  private int auth_type=0; // 0=no auth, 1=anon, 2=open, 3=idpass
  @SuppressWarnings("unused")
  private String principal;
  @SuppressWarnings("unused")
  private String group;
  @SuppressWarnings("unused")
  private String credentials;
  private String charset;

  private static LoggingContext cat       = LogContextFactory.getContext("Z3950Origin");

  public static int dbg_count           = 0;
  private Vector outstanding_queries       = new Vector();
  private boolean supports_named_result_sets   = true;
  private boolean supports_scan         = false;

  // Should we attempt to work around broken REFID implementatations?
  private boolean target_has_broken_refid = false;

  // Are concurrent operations supported?
  private boolean target_supports_concurrent_operations = true;

  private String last_search_refid   = null;
  private String last_present_refid = null;

  private Object op_count_lock     = new Object();
  private int outstanding_operations= 0;

  // Helper to convert the stream of Observable/Observer events into notification calls agaist
  // the APDUListener interface of this object.
  private GenericEventToOriginListenerAdapter message_adapter = null;

  public static final String SERVICE_HOST_PROP       = "ServiceHost";
  public static final String SERVICE_PORT_PROP       = "ServicePort";
  public static final String SERVICE_SHORT_NAME_PROP   = "service_short_name";
  public static final String SERVICE_LONG_NAME_PROP   = "service_long_name";
  public static final String DEFAULT_RECSYN_PROP     = "default_record_syntax";
  public static final String PREF_MSG_SIZE_PROP      = "pref_message_size";
  public static final String MAX_MSG_SIZE_PROP       = "max_message_size";
  public static final String AUTH_TYPE_PROP       = "service_auth_type";
  public static final String AUTH_PRINCIPAL_PROP     = "service_user_principal";
  public static final String AUTH_GROUP_PROP       = "service_user_group";
  public static final String AUTH_CREDENTIALS_PROP     = "service_user_credentials";
  public static final String DEFAULT_ELEMENT_SET_PROP   = "default_element_set_name";
  public static final String SMALL_SET_NAME_PROP     = "small_set_setname";
  public static final String SMALL_SET_ELEMENTS_PROP   = "small_set_setname";
  public static final String DEFAULT_PRESENT_CHUNK_SIZE = "default_present_chunk_size";
  public static final String CHARSET_PROP         = "charset";
  public static final String SERVICE_ID_PROP       = "service_id";
  public static final String BROKEN_REFID_PROP       = "broken_refid";
  

  public int default_present_chunk_size = 10;


  public Z3950Origin()
  {
    dbg_count++;
    cat.debug("Z3950Origin::Z3950Origin() ("+dbg_count+" active)");

    db_names.add("Default");
    reg = OIDRegister.getRegister();
  }


  protected void finalize()
  {
    dbg_count--;
    cat.info("Z3950Origin::finalize() ("+dbg_count+" active)");
    assoc=null;
    active_searches=null;
  }

 
  public void init(Properties p)
  {
    cat.debug("init "+p);

    this.properties = p;

    target_name = (String)(p.get(SERVICE_SHORT_NAME_PROP));
    target_id   = (String)(p.get(SERVICE_ID_PROP));

    // Authentication parameters
    Integer auth_type_str = (Integer)(properties.get(AUTH_TYPE_PROP));
    if ( auth_type_str != null )
    {
//       auth_type = Integer..parseInt((String)(auth_type_str)); 
    	auth_type=auth_type_str.intValue();
    }

    principal  = (String)(properties.get(AUTH_PRINCIPAL_PROP));
    group    = (String)(properties.get(AUTH_GROUP_PROP));
    credentials = (String)(properties.get(AUTH_CREDENTIALS_PROP));
    charset   = (String)(properties.get(CHARSET_PROP));

    if ( ( properties.get(BROKEN_REFID_PROP) != null ) &&
         ( ((String)properties.get(BROKEN_REFID_PROP)).equalsIgnoreCase("true") ) )
    {
      target_has_broken_refid        = true;
      target_supports_concurrent_operations = false;
      cat.debug("Setting broken refid - true, concurrent ops false");
    }

    if ( charset == null )
      charset="US-ASCII";

    String chunk_size = (String)(properties.get(DEFAULT_PRESENT_CHUNK_SIZE));
    if ( chunk_size != null )
      default_present_chunk_size = Integer.parseInt(chunk_size);
  }


  public void destroy()
  { 
    cat.debug("Z3950Origin::destroy()");


    if ( assoc != null )
    {
      if ( message_adapter != null )
      {
        assoc.getPDUAnnouncer().deleteObserver(message_adapter);
        message_adapter = null;
      }

      try
      {
        assoc.shutdown();
        cat.debug("Waiting for assoc thread...");
        assoc.join();
        cat.debug("Done waiting for assoc thread...");
      }
      catch ( Exception e )
      {
        cat.info(e.toString());
      }
    }
  }


  public int getManagerType()
  {
    return Searchable.SPECIFIC_SOURCE;
  }


  public SearchTask createTask(IRQuery q, Object user_data)
  {
    cat.debug("Z3950Origin::createTask(...)");

    return createTask(q,user_data,null);
  }

  
  public SearchTask createTask(IRQuery q,
                               Object user_info,
                               Observer[] observers)
  {
    cat.debug("Z3950Origin::createTask(...,observers)");

    // Create a new search task ( A search ) 
    Z3950SearchTask st = new Z3950SearchTask(this, 
                                 observers, 
               default_present_chunk_size,
               properties);
    st.setUserData(user_info);
    st.setTaskStatusCode(SearchTask.TASK_IDLE );
    st.setQuery(q);

    String task_id = st.getTaskIdentifier();

    active_searches.put(task_id, new WeakReference(st));

    cat.debug("Returning Z3950 search task");
    return st;
  }

  
  public void evaluateTask(SearchTask st, int wait_for) throws SearchException, TimeoutExceededException
  {
    cat.debug("Z3950Origin::evaluateTask(...,observers)");

    IRQuery q = st.getQuery();
    String task_id = st.getTaskIdentifier();
    String refid = task_id+":srch";

    Z3950SearchTask zst = (Z3950SearchTask)st;

    if ( cat.isDebugEnabled() )
    {
      try
      {
        StringWriter sw = new StringWriter();
        com.k_int.util.RPNQueryRep.PrefixQueryVisitor.visit(q.query.toRPN(),sw);
        cat.debug("Query as RPN Is : "+sw.toString());
      }
      catch ( Exception iqe )
      {
        cat.warn("Problem converting query",iqe);
      }
    }

    if ( assoc_is_accepting_searches )
    {
      // If the connection is already up, Just send the search.
      try
      {
    zst.broadcastStatusMessage(SearchTask.TASK_MESSAGE_INFO,"Sending query to remote repository");
        zst.setPrivateStatusCode(Z3950SearchTask.ZSTATUS_SEARCHING);
        cat.debug("Sending query directly to target");
        sendQuery(q,st.getUserData(),task_id,refid);
        ((Z3950SearchTask)st).z3950_status = 1;
      }
      catch ( java.io.IOException ioe )
      {
        // Set failed status on tracker and add log reason for failure with tracker also.
        // May also need to invalidate association....???
    zst.broadcastStatusMessage(SearchTask.TASK_MESSAGE_ERROR,"Error sending search to repository");
    zst.setDiagnosticStatus("diag.k-int.1", target_name, ioe.toString());
        zst.setPrivateStatusCode(Z3950SearchTask.ZSTATUS_ERROR);
        st.setTaskStatusCode(SearchTask.TASK_FAILURE);
        cat.warn(ioe.toString());
      }
      catch ( InvalidQueryException iqe )
      {
    zst.broadcastStatusMessage(SearchTask.TASK_MESSAGE_ERROR,"Failed to parse query");
    zst.setDiagnosticStatus("diag.k-int.5", target_name, iqe.toString());
        zst.setPrivateStatusCode(Z3950SearchTask.ZSTATUS_ERROR);
        st.setTaskStatusCode(SearchTask.TASK_FAILURE);
        cat.warn("Invalid query exception "+iqe.toString());
      }
      catch ( SearchException se )
      {
    zst.broadcastStatusMessage(SearchTask.TASK_MESSAGE_ERROR,"General search exception : "+se.toString());
    zst.setDiagnosticStatus("diag.k-int.3",target_name, se.toString() );
        zst.setPrivateStatusCode(Z3950SearchTask.ZSTATUS_ERROR);
        st.setTaskStatusCode(SearchTask.TASK_FAILURE);
        cat.warn("Search exception "+se.toString());
      }
    }
    else
    {
      zst.broadcastStatusMessage(SearchTask.TASK_MESSAGE_INFO, "Connecting to repository..."); 
      cat.debug("Association is not yet active, queue the search for later");
      // Add the query to the queue of waiting queries
      synchronized(outstanding_queries)
      {
        outstanding_queries.add(new PendingSearch(q,st.getUserData(),task_id,refid,st));
      }

      // And then check the connection
      checkConnection();
    }

    // One way or another, we have sent a query to the remote target. Either the assoc was
    // already accepting targets, or we have queued the search and asked the assoc to be 
    // brought up. Now we hang around and wait to see if any response comes along before
    // the timeout.
    try
    {
      if ( wait_for > 0 )
      {
        // Here we should be checking st.getTaskStatus for SearchTask.TASK_COMPLETE or SearchTask.TASK_FAILURE
        cat.debug("evaluateQuery is waiting for up to "+wait_for+" ms task status complete or failure");
        st.waitForStatus(SearchTask.TASK_COMPLETE | SearchTask.TASK_FAILURE, wait_for);
      }
      else
      {
        cat.debug("timeout <= 0.");
      }
    }
    catch ( TimeoutExceededException tee )
    {
      cat.info("Timeout waiting for search response");
      // Should probably re-throw adding search tracker to exception, since it's
      // OK for an operation to take longer than timeout millis to complete, just
      // nice if it does complete. Do we want to force users of the API to check
      // the task status after every little operation... I guess not!
      throw new TimeoutExceededException();
    }
  }

 
  @SuppressWarnings("unused")
private void sortResultSet(Vector setnames_to_sort,
                             String target_set,
                             String refid,
                             String sort_specification)
  {
  }

  
  private void sendQuery(IRQuery q,
                         Object user_info,
                         String task_id,
                         String refid) throws SearchException, 
                                              IOException, 
                InvalidQueryException
  {
    synchronized(op_count_lock)
    {
      outstanding_operations++;
    }

    String small_set_setname = null;
    String default_element_set_name = null;
    Vector v = q.collections;

    if ( q.hints != null )
    {
      recsyn_to_use       = (String)(q.hints.get("record_syntax"));
      small_set_setname     = (String)(q.hints.get("small_set_setname"));
      default_element_set_name   = (String)(q.hints.get("default_element_set_name"));
    }

    if ( recsyn_to_use == null )
      recsyn_to_use = getDefaultRecordSyntax();
  
  last_search_refid=refid;

  if(target_has_broken_refid)  // added to check - Rob
    refid=null;
         
  cat.debug("Sending search request with ID: "+refid+" recsyn is "+recsyn_to_use);

    // Call sendSearchRequest with appropriate parameters ( task_id is used as result set name )
    assoc.sendSearchRequest(v, 
                            q.query,
                            refid, 
                            0,          // Small Set Upper Bound was 0
                            1,        // Large Set Lower Bound was 200
                            1,          // Medium Set Present Number was 0
                            true, 
                            ( supports_named_result_sets == true ? task_id : "default" ), 
                            small_set_setname != null ? small_set_setname : "F", 
                            default_element_set_name != null ? default_element_set_name : "b", 
                            reg.oidByName(recsyn_to_use));
  }

  public PresentResponse_type fetchRecords(                   String task_id,
                                           RecordFormatSpecification spec,
                                                                 int start, 
                                                                 int count, 
                                                                 int wait_for) throws PresentException
  {
    if ( cat.isDebugEnabled() )
      cat.debug("Z3950Origin::fetchRecords("+task_id+","+
                         spec.getSetname()+","+start+","+count+","+wait_for+")");

    if ( assoc == null )
      throw new PresentException("Connection to "+target_name+" seems to have died. Cannot request records");

    synchronized(op_count_lock)
    {
      outstanding_operations++;
    }

    cat.debug("Z3950Origin::fetchRecords() from "+target_name);
    PresentResponse_type retval = null;
    //String schema = "";
    // Hmmm.. The III target only returns the first 15 characters of the refid.... How rude!
    // String refid = task_id+":"+(ref_counter++)+":"+start+":"+count+":"+setname+":"+setname+":present";

    String refid     = null;  
  refid         = task_id+":"+(ref_counter++);
  last_present_refid   = refid;
  
  if (target_has_broken_refid)
    refid=null;
    
  cat.debug("Set refid to "+refid); 
  
    // Set up the semaphore to wait for a response message _before_ sending the
    // PDU, since we would hate for the response to arrive in-between sending the
    // request and us completing setup of the semaphore.
    ReferencedPDUAvaialableSemaphore s = new ReferencedPDUAvaialableSemaphore(refid, assoc.getPDUAnnouncer());
  cat.debug("About to send present request");
    try
    {
      // We name result sets according to task_id
      assoc.sendPresentRequest(refid, 
                               (supports_named_result_sets == true ? task_id : "default"), 
                               start, 
                               count,
                     spec);

      cat.debug("Waiting for present response PDU with refid "+refid);
      s.waitForCondition(wait_for); // Wait up to wait_for seconds for a response
      retval = (PresentResponse_type) s.the_pdu.o;
    }
    catch ( java.io.IOException ioe )
    {
      cat.warn("IO Exception waiting for present response",ioe);
      throw new PresentException("IO Exception waiting for records from remote source "+target_name);
    }
    catch ( TimeoutExceededException tee )
    {
      cat.warn("Timeout waiting for present response",tee);
      throw new PresentException("Timeout waiting for records from remote source "+target_name);
    }
    finally
    {
      s.destroy();
    }

    cat.debug("fetchRecords returning presentResponse");

    if ( retval == null )
    {
      cat.warn("Present found no records");
      throw new PresentException("Failed to fetch records from remote source "+target_name);
    }

    return retval;
  }


  public void asyncFetchRecords(                   String task_id,
                                RecordFormatSpecification spec,
                                                      int start, 
                                                      int count, 
                                          ZCallbackTarget callback)
  {
      if ( cat.isDebugEnabled() )
          cat.debug("Z3950Origin::asyncfetchRecords("+task_id+","+
                    spec.getSetname()+","+start+","+count+",notification_targets)");
      try
      {
          if ( assoc == null )
             throw new PresentException("Connection to "+target_name+" seems to have died. Cannot request records");

          synchronized(op_count_lock)
          {
            outstanding_operations++;
          }

          cat.debug("Z3950Origin::asyncfetchRecords() from "+target_name);
          //PresentResponse_type retval = null;
          //String schema = "";
          // Hmmm.. The III target only returns the first 15 characters of the refid.... How rude!
          // String refid = task_id+":"+(ref_counter++)+":"+start+":"+count+":"+setname+":"+setname+":present";

          String refid     = null;    
      refid          = task_id+":"+(ref_counter++);  
      last_present_refid   = refid;  
      //cat.debug("Set refid to "+refid);

          // Set up the semaphore to wait for a response message _before_ sending the
          // PDU, since we would hate for the response to arrive in-between sending the
          // request and us completing setup of the semaphore.
          // We name result sets according to task_id
              
          outstanding_requests.put(refid, new OutstandingOperationInfo(refid,"Present",callback));

        if (target_has_broken_refid)
          refid=null;
      
      cat.debug("Set refid to "+refid);
          assoc.sendPresentRequest(refid, 
                               (supports_named_result_sets == true ? task_id : "default"), 
                               start, 
                               count,
                     spec);

      }
      catch ( java.io.IOException ioe )
      {
          cat.warn("IO Exception waiting for present response",ioe);
      }
      catch ( PresentException pe )
      {
          cat.warn("Present Exception",pe);
      }
      finally
      {
      }
    }

  // Private functions
  // This function needs to throw an exception if there is something badly wrong
  // with the connect data...
  // private void checkConnection() throws SearchException
  private synchronized void checkConnection()
  {
    if ( assoc == null )
    {
      cat.info("Assoc is null.... Create new association");
      try
      {
  cat.debug("Create association and message adapter");
        assoc = new ZEndpoint(properties);

  // If we are given a charset,set the encoding for the Assoc
  if ( properties.getProperty(CHARSET_PROP) != null )
          assoc.setCharsetEncoding(charset);

        message_adapter = new GenericEventToOriginListenerAdapter(this);
        assoc.getPDUAnnouncer().addObserver( message_adapter );

  cat.debug("Calling ZEndpoint.start()");
        assoc.start();
      }
      catch( Exception e )
      {
  if ( ( assoc != null ) && ( message_adapter != null ) )
  {
          assoc.getPDUAnnouncer().deleteObserver( message_adapter );
  }
        assoc=null;
        message_adapter=null;

        cat.error("Probelm connecting",e);
      }
    }
    else
    {
      cat.debug("checkConnection: Association is present");
    }
  }

  // Notification Handlers

  public void incomingAPDU(APDUEvent e)
  {
    // We don't care about this
    cat.warn("Un-handled Generic Incoming APDU notification");
  }

  public void incomingInitResponse(APDUEvent e)
  {
    if ( cat.isDebugEnabled() )
      cat.debug("Processing init response from "+target_name);

    InitializeResponse_type init_response = (InitializeResponse_type) (e.getPDU().o);

    if ( cat.isDebugEnabled() )
    {
      if ( init_response.referenceId != null )
        cat.debug("  Reference ID : "+new String(init_response.referenceId));
      else
        cat.debug("  Incoming refid is NULL!");

      cat.info("  Implementation ID : "+init_response.implementationId);
      cat.info("  Implementation Name : "+init_response.implementationName);
      cat.info("  Implementation Version : "+init_response.implementationVersion);
      //try
	  //{
      //	cat.info(new String((byte[])init_response.userInformationField.encoding.o));
	  //}
      //catch(Exception exc)
	  //{
      //	exc.printStackTrace();
	  //}
    }

    // Preparation for synchronous Retrieval API

    // Send any queued searches

    if ( init_response.result.booleanValue() )
    {
      assoc_is_accepting_searches=true;

      if ( init_response.options.isSet(14) )
      {
        cat.info("Target supports named result sets");
      }
      else
      {
        cat.info("Target does not support named result sets");
        supports_named_result_sets=false;
      }

      if ( init_response.options.isSet(8) )
      {
        cat.info("Target claims scan support");
        supports_scan=true;
      }

      if ( init_response.options.isSet(13) )
      {
        cat.info("Target claims support for concurrent operations");
      }
      else
      {
        cat.info("Target does not support concurrent operations");
        target_supports_concurrent_operations=false;
      }

      // If for some reason, because of the config or init negotiation, we don't want to do
      // concurrent ops on this assoc, set the assoc to only do serial ops.
      if ( !target_supports_concurrent_operations )
        assoc.setSerialOps();

      synchronized(outstanding_queries)
      {
        // We are ready to accept searches

        for ( Enumeration oq = outstanding_queries.elements(); oq.hasMoreElements(); )
        {
            PendingSearch p   = (PendingSearch) oq.nextElement();
        Z3950SearchTask zst = (Z3950SearchTask)p.st;
      zst.broadcastStatusMessage(SearchTask.TASK_MESSAGE_INFO, "Sending query to remote repository");
            zst.setPrivateStatusCode(Z3950SearchTask.ZSTATUS_SEARCHING);
            cat.debug("Sending outstanding query, task="+p.task_id);

          try
          {
            sendQuery(p.q, p.user_info, p.task_id, p.refid);
          }
          catch ( java.io.IOException ioe )
          {
            // Set failed status on tracker and add log reason for failure with tracker also.
            // May also need to invalidate association....???
            p.st.setDiagnosticStatus("diag.k-int.1",target_name, ioe.toString());
            p.st.setTaskStatusCode(SearchTask.TASK_FAILURE);
            cat.info(ioe.toString());
          }
          catch ( InvalidQueryException iqe )
          {
      p.st.setDiagnosticStatus("diag.k-int.2",target_name, iqe.toString());
            p.st.setTaskStatusCode(SearchTask.TASK_FAILURE);
            cat.info("Invalid query exception "+iqe.toString());
          }
          catch ( SearchException se )
          {
      p.st.setDiagnosticStatus("diag.k-int.3",target_name, se.toString());
      p.st.setTaskStatusCode(SearchTask.TASK_FAILURE);
            cat.info("Search exception "+se.toString());
          }
        }
      }
    }
    else
    {
      cat.warn("Init was not OK, not sending outstanding queries, and failing those queries in the queue");
      synchronized(outstanding_queries)
      {
        for ( Enumeration oq = outstanding_queries.elements(); oq.hasMoreElements(); )
        {
            PendingSearch p   = (PendingSearch) oq.nextElement();
        Z3950SearchTask zst = (Z3950SearchTask)p.st;
        zst.broadcastStatusMessage(SearchTask.TASK_MESSAGE_ERROR, "Remote target rejected the connection");
        zst.setDiagnosticStatus("diag.k-int.4", target_name, "Remote target rejected connection");        
            zst.setPrivateStatusCode(Z3950SearchTask.ZSTATUS_ERROR);
      zst.setTaskStatusCode(SearchTask.TASK_FAILURE);
        }
      }
    }
    outstanding_queries.clear();

    synchronized(this)
    {
      notifyAll();
    }
  }

  public void incomingSearchResponse(APDUEvent e)
  {
    SearchResponse_type search_response = (SearchResponse_type) e.getPDU().o;

    synchronized(op_count_lock)
    {
      outstanding_operations--;
    }

    if ( target_has_broken_refid )
    {
      cat.debug("Broken REFID, working around by manually setting the last Search_refid used");
      search_response.referenceId = last_search_refid.getBytes();
    }

    if ( cat.isDebugEnabled() )
    {
      if ( search_response.referenceId != null )
        cat.debug("Search Response - Reference ID : "+new String(search_response.referenceId));
      else
        cat.fatal("The search response has NO REFID!");

      cat.debug("  Search Result : "+search_response.searchStatus);
      cat.debug("  Result Count : "+search_response.resultCount);
      cat.debug("  Num Records Returned : "+search_response.numberOfRecordsReturned);
      cat.debug("  Next RS position : "+search_response.nextResultSetPosition);
    }

    // We need to split refid down into task-id : Srch
    System.out.println(new String(search_response.referenceId));
    StringTokenizer st = new StringTokenizer(new String(search_response.referenceId),":");    //Questa riga e' stata cambiata a causa di z3950.izumi.si perche' nella stringa indicata non c'era l'id aspettato
    //StringTokenizer st = new StringTokenizer(last_search_refid,":");
    if ( st.hasMoreTokens() )
    {
      String taskid = st.nextToken();
      WeakReference wr = (WeakReference)active_searches.get(taskid);
      Z3950SearchTask tsk;
      if ( wr != null )
      {
         tsk = (Z3950SearchTask)wr.get();
      
        if ( null != tsk )
        {
          // We set the fragment count before announcing that the search has completed
          tsk.setFragmentCount(search_response.resultCount.intValue());
          tsk.setPrivateStatusCode(Z3950SearchTask.ZSTATUS_SEARCH_COMPLETE);

          tsk.z3950_status = 2;
          if ( search_response.searchStatus.booleanValue() )
          {
            // SearchRequest completed without problems... Check to see if there was a sort order
            // If there are no sort criteria
            IRQuery q = tsk.getQuery();
            if ( ( q.sorting != null ) && ( ! q.sorting.equals("") ) )
            {
              tsk.setPrivateStatusCode(Z3950SearchTask.ZSTATUS_SORTING);
              Vector setnames_to_sort = new Vector();
              setnames_to_sort.add(taskid);
              cat.debug("Search task contains sort critera: "+q.sorting);
              // We should leave the task status as working, and send a sort request
              // OnSortResponse should deal with finalizing the task.
              try
              {
                assoc.sendSortRequest(tsk.getTaskIdentifier()+":sort",
                                      setnames_to_sort,
                                      taskid,
                                      (String) q.sorting);
              }
              catch ( com.k_int.util.SortSpecLang.SortStringException sse )
              {
                cat.info(sse.toString());
                tsk.setDiagnosticStatus("diag.k-int.6",target_name,sse.toString());
                tsk.setTaskStatusCode(SearchTask.TASK_FAILURE);
              }
              catch ( java.io.IOException ioe )
              {
                cat.info(ioe.toString());
                tsk.setDiagnosticStatus("diag.k-int.1", target_name,ioe.toString());
                tsk.setTaskStatusCode(SearchTask.TASK_FAILURE);
              }
            }
            else
            {
              cat.debug("No sorting instructions in task. All complete!");

              // There are no sort critera, so we are all finished.
              tsk.setTaskStatusCode(SearchTask.TASK_COMPLETE );

              // Handle any piggyback records
              if ( null != search_response.records )
              {
                handleRecords(tsk, search_response.records);
              }
            }
          }
          else
          {
            // Watch out for any diagnostic records
            if ( null != search_response.records )
            {
              handleRecords(tsk, search_response.records);
            }      
      cat.warn("Search failure.....");
      tsk.broadcastStatusMessage(SearchTask.TASK_MESSAGE_INFO,"Search failure");
      tsk.setDiagnosticStatus("diag.k-int.3",target_name,"Search failure");
      tsk.setTaskStatusCode(SearchTask.TASK_FAILURE);
      //tsk.setTaskStatusCode(SearchTask.TASK_FAILURE,new BigInteger("3"), "Search failure");
          }
        }
        else
        {
          // The task is no longer refereced and the object has been GC'd
          cat.warn("The SearchTask associated with REFID "+ new String(search_response.referenceId)+
            " is no longer referenced and has been garbage collected.");
          // remove it from the active_searches map. The task should have
          // arranged for itself to be removed from the map... Never mind
          active_searches.remove(taskid);
        }
      } 
      else
      {
        cat.warn("Unable to locate a search for the REFID "+
           new String(search_response.referenceId)+
           ". REFID processing at the target may be BROKEN!");
      }
    }
    else
    {
      cat.fatal("Unable to parse refid for search response");
    }

    // Preparation for synchronous Retrieval API. If we have sent a sort request
    // any waiters will be updated, but the status will still not be complete.
    // onSortResponse will set status to complete and update as needed.
    synchronized(this)
    {
      notifyAll();
    } 
  }

  private void handleRecords(Z3950SearchTask tsk, Records_type r)
  {
    switch ( r.which )
    {
      case Records_type.responserecords_CID :
        Vector records = (Vector)r.o;
        if ( records.size() > 0 )
        {
          cat.debug("  Search has records (type="+r.which+", but Z3950Origin should use MSPN of 0?");
        }
        break;
      case Records_type.nonsurrogatediagnostic_CID :
        cat.debug("NonSurrogate diagnostics");
        DefaultDiagFormat_type diag = (DefaultDiagFormat_type)r.o;
        String message = "Diagnostic ("+target_name+"): "+diag.condition+" addinfo: "+diag.addinfo.toString();
        cat.debug(message);
        tsk.broadcastStatusMessage(SearchTask.TASK_MESSAGE_DIAGNOSTIC,message);
        tsk.setDiagnosticStatus("diag.bib1."+diag.condition,target_name,message);
        break;
      case Records_type.multiplenonsurdiagnostics_CID:
        cat.debug("Multiple NonSurrogate diagnostics");
    //  TODO Write the multiple diagnostic code
        break;
      default:
        cat.debug("Unknown choice type in Records");
        break;
    }
  }

  public void incomingPresentResponse(APDUEvent e)
  {
    PresentResponse_type present_response = (PresentResponse_type) e.getPDU().o;

    synchronized(op_count_lock)
    {
      outstanding_operations--;
    }
    cat.debug("Incoming PresentResponse from "+e.getSource().hashCode());
    
    if (target_has_broken_refid)
    {
      cat.debug("broken refid - manually setting refid using last present refid");
      present_response.referenceId = last_present_refid.getBytes();
    }

    if ( present_response.referenceId != null )
    {
      String refid = new String(present_response.referenceId);
      cat.debug("Present Response - Reference ID : \""+refid+"\" target="+target_name);
      OutstandingOperationInfo ooi = (OutstandingOperationInfo) outstanding_requests.remove(refid);
      if ( ooi != null )
      {
        ooi.getCallbackTarget().notifyPresentResponse(present_response);
      }
    }
    else
    {
        cat.warn("incomingPresentResponse::Null refid");      
    }

    synchronized(this)
    {
      notifyAll();
    } 
  }

  public void incomingDeleteResultSetResponse(APDUEvent e)
  {
    cat.debug("Incoming DeleteResultSetResponse");

    // Preparation for synchronous Retrieval API
    synchronized(this)
    {
      notifyAll();
    } 
  }

  public void incomingAccessControlRequest(APDUEvent e)
  {
    // Preparation for synchronous Retrieval API
    synchronized(this)
    {
      notifyAll();
    } 
  }

  public void incomingAccessControlResponse(APDUEvent e)
  {
    cat.debug("Incoming AccessControlResponse");

    // Preparation for synchronous Retrieval API
    synchronized(this)
    {
      notifyAll();
    } 
  }

  public void incomingResourceControlRequest(APDUEvent e)
  {
    // Preparation for synchronous Retrieval API
    synchronized(this)
    {
      notifyAll();
    } 
  }

  public void incomingResourceControlResponse(APDUEvent e)
  {
    cat.debug("Incoming ResourceControlResponse");

    // Preparation for synchronous Retrieval API
    synchronized(this)
    {
      notifyAll();
    } 
  }

  public void incomingTriggerResourceControlRequest(APDUEvent e)
  {
    // Preparation for synchronous Retrieval API
    synchronized(this)
    {
      notifyAll();
    } 
  }

  public void incomingResourceReportRequest(APDUEvent e)
  {
    cat.debug("Incoming ResourceReportResponse");

    // Preparation for synchronous Retrieval API
    synchronized(this)
    {
      notifyAll();
    } 
  }

  public void incomingResourceReportResponse(APDUEvent e)
  {
    // Preparation for synchronous Retrieval API
    synchronized(this)
    {
      notifyAll();
    } 
  }

  public void incomingScanRequest(APDUEvent e)
  {
    cat.debug("Incoming ScanResponse");

    // Preparation for synchronous Retrieval API
    synchronized(this)
    {
      notifyAll();
    } 
  }

  public void incomingScanResponse(APDUEvent e)
  {
    // Preparation for synchronous Retrieval API
    synchronized(this)
    {
      notifyAll();
    } 
  }

  public void incomingSortRequest(APDUEvent e)
  {
    cat.debug("Incoming SortResponse");

    // Preparation for synchronous Retrieval API
    synchronized(this)
    {
      notifyAll();
    } 
  }

  public void incomingSortResponse(APDUEvent e)
  {
    SortResponse_type sort_response = (SortResponse_type) e.getPDU().o;
 
    if ( cat.isDebugEnabled() )
    {

      cat.debug("Sort Response");
      if ( sort_response.referenceId != null )
        cat.debug("  Reference ID : "+new String(sort_response.referenceId));
      cat.debug("  Sort Status : "+sort_response.sortStatus);
      cat.debug("  Result Set Status : "+sort_response.resultSetStatus);
    }
 
    // We need to split refid down into task-id : Srch
    StringTokenizer st = new StringTokenizer(new String(sort_response.referenceId),":");
    if ( st.hasMoreTokens() )
    {
      String taskid = st.nextToken();
      WeakReference wr = (WeakReference)active_searches.get(taskid);
 
      if ( wr != null )
      {
        Z3950SearchTask tsk = (Z3950SearchTask)wr.get();
        tsk.setPrivateStatusCode(Z3950SearchTask.ZSTATUS_SORT_COMPLETE);
 
        if ( null != tsk )
        {
          switch( sort_response.sortStatus.intValue() )
          {
            case 0: 
              // success - The sort was performed successfully.
              tsk.setTaskStatusCode(SearchTask.TASK_COMPLETE );
              break;
            case 1: 
              // partial-1 - The sort was performed but target encountered missing values in one or more sort elements
              tsk.setTaskStatusCode(SearchTask.TASK_COMPLETE );
              break;
            case 2: 
              // The sort was not performed... See Diagnostics. resultSetStatus set if sortStatus == failure.
              cat.info ("Sort Failure, Result set status is "+sort_response.resultSetStatus);
              tsk.broadcastStatusMessage(SearchTask.TASK_MESSAGE_ERROR,"Sort Failure, Result set status is "+sort_response.resultSetStatus);
              tsk.setDiagnosticStatus("diag.k-int.6",target_name,"Sort Failure, Result set status is "+sort_response.resultSetStatus);
              tsk.setTaskStatusCode(SearchTask.TASK_FAILURE );
              break;
          }
        }
        else
        {
          // The task is no longer refereced and the object has been GC'd, remove it from
          // the active_searches map. The task should have arranged for itself to be removed from the
          // map... Never mind...
          active_searches.remove(taskid);
        }
      }
    }

    // Preparation for synchronous Retrieval API
    synchronized(this)
    {
      notifyAll();
    } 
  }

  public void incomingSegmentRequest(APDUEvent e)
  {
    cat.debug("Incoming SegmentResponse");

    // Preparation for synchronous Retrieval API
    synchronized(this)
    {
      notifyAll();
    } 
  }

  public void incomingExtendedServicesRequest(APDUEvent e)
  {
    // Preparation for synchronous Retrieval API
    synchronized(this)
    {
      notifyAll();
    } 
  }

  public void incomingExtendedServicesResponse(APDUEvent e)
  {
    cat.debug("Incoming ExtendedServicesResponse");

    // Preparation for synchronous Retrieval API
    synchronized(this)
    {
      notifyAll();
    } 
  }

  public void incomingClose(APDUEvent e)
  {
    Close_type close = (Close_type) e.getPDU().o;

    // ToDo: II: Delete this
    System.err.println("got Incoming CLose APDU reasonCode="+close.closeReason+" diag="+close.diagnosticInformation);

    cat.debug("Z3950Origin::incomingClose");

    // Let anyone waiting for an operation to complete know that it's not going to get
    // finished!
    for ( Enumeration req_enum = outstanding_requests.elements(); req_enum.hasMoreElements(); )
    {
      OutstandingOperationInfo ooi = ( OutstandingOperationInfo ) req_enum.nextElement();
      ooi.getCallbackTarget().notifyClose("closed");
    }

    // No more requests
    outstanding_requests.clear();

    // Preparation for synchronous Retrieval API
    synchronized(this)
    {
      notifyAll();
    } 

    cat.debug("Clearing out old z-association");
    this.assoc = null;
    this.assoc_is_accepting_searches = false;
  }

  private String getDefaultRecordSyntax()
  {
    String retval = null;

    if ( null != properties )
      retval = (String)(properties.get(DEFAULT_RECSYN_PROP));

    if ( retval == null )
      retval = "Sutrs";

    return retval;
  }

  public String getTargetDN()
  {
    return target_id;
  }

  public String getTargetName()
  {
    return target_name;
  }

  public boolean isScanSupported()
  {
    return supports_scan;
  }

  public ScanInformation doScan(com.k_int.IR.ScanRequestInfo req)
  {
    return null;
  }

  public String toString()
  {
    return "Z3950Origin - "+target_name;
  }

  public String getCharset()
  {
    return charset;
  }
}
