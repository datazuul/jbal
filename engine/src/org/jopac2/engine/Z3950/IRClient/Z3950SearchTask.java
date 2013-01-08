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
// Title:       Z3950SearchTracker
// @version:    $Id: Z3950SearchTask.java,v 1.3 2008-03-19 19:25:52 romano Exp $
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

import java.util.*;

// Information Retrieval Interfaces

import com.k_int.IR.*;
import com.k_int.IR.Syntaxes.*;

import com.k_int.codec.util.*;
import com.k_int.gen.Z39_50_APDU_1995.*;
import com.k_int.gen.AsnUseful.*;
 
// For logging
import com.k_int.util.LoggingFacade.*;
import com.k_int.z3950.IRClient.*;


public class Z3950SearchTask extends com.k_int.z3950.IRClient.Z3950SearchTask implements InformationFragmentSource
{
  public static final int ZSTATUS_NONE 				= 0;
  public static final int ZSTATUS_IDLE 				= 1;
  public static final int ZSTATUS_SEARCHING 		= 2;
  public static final int ZSTATUS_SEARCH_COMPLETE 	= 3;
  public static final int ZSTATUS_PRESENTING 		= 4;
  public static final int ZSTATUS_ALL_PRESENTED 	= 5;
  public static final int ZSTATUS_SORTING 			= 6;
  public static final int ZSTATUS_SORT_COMPLETE 	= 7;
  public static final int ZSTATUS_ERROR 			= 8;

  private static final String[] private_status_types = { "Undefined",
	                                                 	 "Idle", 
                                                         "Searching", 
                                                         "Search complete", 
                                                         "Requesting records", 
                                                         "All records returned",
                                                         "Sorting",
                                                         "Sort Complete",
                                                         "Error" };
  public int z3950_status 				= 0;
  private Z3950Origin protocol_endpoint = null;
  private int fragment_count 			= 0;

  private static LoggingContext cat = LogContextFactory.getContext("Z3950SearchTask");
  public static int dbg_counter 	= 0;

  // Wait indefinitely for records to be presented.
  private int default_present_timeout = 40000;
  private int default_present_chunk_size = 1;

  private Properties properties = null;

  private OIDRegisterEntry default_recsyn 			= null;
  private OIDRegister reg=null;//				= OIDRegister.getRegister();
  @SuppressWarnings("unused")
  private RecordFormatSpecification default_spec 	= null;

  //private Hashtable outstanding_requests = new Hashtable();

  // This will hold result records. It may be that we start to throw out elements
  // on a LRU basis at some point in the future
  //

  public Z3950SearchTask(Z3950Origin protocol_endpoint, 
		          		 Observer[] observers, 
		 	         	 int default_present_chunk_size,
		          		 Properties properties)
  {
    super(protocol_endpoint,observers, default_present_chunk_size,properties);
    dbg_counter++;
    this.reg = OIDRegister.getRegister();
    this.protocol_endpoint 			= protocol_endpoint;
    this.default_present_chunk_size = default_present_chunk_size;
    this.properties 				= properties;

    String default_recsyn_name 		= properties.getProperty("default_record_syntax");
    String default_element_set_name = properties.getProperty("default_element_set_name");

    if ( default_recsyn_name != null )
      this.default_recsyn = reg.lookupByName(default_recsyn_name);
    else
      this.default_recsyn = reg.lookupByName("sutrs");

    cat.debug("Default record syntax name is "+default_recsyn_name+"="+this.default_recsyn);

    if ( default_element_set_name == null )
      default_element_set_name = "b";

    this.default_spec = new RecordFormatSpecification(this.default_recsyn,null,default_element_set_name);
  }

  protected void finalize()
  {
    dbg_counter--;
    cat.info("Z3950SearchTask::finalize() ("+dbg_counter+" active)");
  }

  protected void setPrivateStatusCode(int code)
  {
    z3950_status = code;
  }

  public int getPrivateTaskStatusCode()
  {
    return z3950_status;
  }

  public String lookupPrivateStatusCode(int code)
  {
    return private_status_types[code];
  }

  public int evaluate(int timeout) throws TimeoutExceededException, SearchException
  {
    cat.debug("Z3950SearchTask::evaluateQuery("+timeout+")");
    setTaskStatusCode(SearchTask.TASK_EXECUTING );
    protocol_endpoint.evaluateTask(this, timeout);
    return ( getTaskStatusCode() );
  }

  
  
  public InformationFragment[] getFragment(int starting_fragment, 
		                           int count,
					   RecordFormatSpecification spec) throws PresentException
  {
    RecordFormatSpecification actual_spec = interpretSpec(spec);

    cat.debug("Z3950SearchTask::getFragment("+starting_fragment+","+count+","+actual_spec+")");

    if ( starting_fragment > fragment_count )
      throw new PresentException("Present out of range, only "+fragment_count+" records available");

    if ( starting_fragment+count-1 > fragment_count )
    {
      count=fragment_count-starting_fragment+1;
      cat.debug("get asks for record past end of result set, trim to "+count);
    }

    //InformationFragment[] result = null;
    PresentResponse_type pr = protocol_endpoint.fetchRecords(getTaskIdentifier(),
                                                             actual_spec,
                                                             starting_fragment,
                                                             count,
                                                             default_present_timeout);
    return processRecords(pr.records);
  }

  public void asyncGetFragment(int starting_fragment,
                               int count,
                               RecordFormatSpecification spec,
                               IFSNotificationTarget target)
  {
    RecordFormatSpecification actual_spec = interpretSpec(spec);

    cat.debug("Z3950SearchTask::asyncgetFragment("+starting_fragment+","+count+","+actual_spec+")");

    if ( starting_fragment > fragment_count )
    {
      target.notifyError("bib1-diag",null,"present out of bounds",
          new PresentException("Present out of range, only "+fragment_count+" records available"));
    }
    else 
    {
      if ( starting_fragment+count-1 > fragment_count )
      {
        count=fragment_count-starting_fragment+1;
        cat.debug("get asks for record past end of result set, trim to "+count);
      }

      protocol_endpoint.asyncFetchRecords(getTaskIdentifier(),
                                          actual_spec,
                                          starting_fragment,
                                          count,
                                          new PresentCallbackHandler(this, target));
    }
  }

  @SuppressWarnings("unchecked")
protected InformationFragment[] processRecords(Records_type r)
  {
    InformationFragment[] result = null;

    if (  r != null )
    {
      switch ( r.which )
      {
        case Records_type.responserecords_CID:
            Vector<NamePlusRecord_type> v = (Vector<NamePlusRecord_type>)(r.o);
            int num_records = v.size();
            int counter=0;
            result = new InformationFragment[num_records];

            cat.info("Response contains "+num_records+" Response Records");

            for ( Enumeration<NamePlusRecord_type> recs = v.elements(); recs.hasMoreElements(); ) 
            {
                NamePlusRecord_type npr = (NamePlusRecord_type)(recs.nextElement());

                if ( null != npr )
                {
                  String source_name = protocol_endpoint.getTargetDN();
                  String source_collection = npr.name;

                  switch ( npr.record.which )
                  {
                    case record_inline13_type.retrievalrecord_CID:
                      // RetrievalRecord is an external
                      EXTERNAL_type et = (EXTERNAL_type)npr.record.o;
                      int[] record_oid = et.direct_reference;
		      OIDRegisterEntry record_type = reg.lookupByOID(record_oid);
                      /*
                      byte[] record;
                      if(et.encoding.o instanceof String)
                      {    
                          record = ((String)et.encoding.o).getBytes();
                      }
                      else
                      {
                          record = (byte[]) et.encoding.o;
                      }
                      
                      String riga = new String(record);
                      result[counter++]=(InformationFragment)et.encoding.o;
                      */
		      RecordFormatSpecification spec = new RecordFormatSpecification(record_type,                                                                                                  null,
				                                                     null);
                      //
                      // This switch needs replacing with a lookup table REAL soon
                      //

                      switch(et.direct_reference.length)
                      {
                        case 6:
                          switch(et.direct_reference[5])
                          {
                            case 1: // Unimarc
                              /*result[counter++] = new com.k_int.IR.Syntaxes.marc.iso2709(source_name, 
			 		                                                 source_collection, 
							                                 spec,
							                                 null, 
							                                 et.encoding.o );
							                                // "UTF-8");*/
                              cat.debug("UNIMARC");
                              String unimarc_record = new String((byte[])et.encoding.o);

/*                              try {
									unimarc_record = java.net.URLEncoder.encode(unimarc_record,"UTF-8");
								} catch (UnsupportedEncodingException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} */
                              
                              result[counter++] = new SUTRS(source_name,
					                    source_collection,
							    null, unimarc_record ,spec);
                                
                              break;
                            case 3: // CCF
                              // System.out.println("CCF");
                              break;
                            case 10: // US Marc
                              cat.debug("USMARC");
                              String usmarc_record = new String((byte[])et.encoding.o);
                              
/*                              try {
									usmarc_record = java.net.URLEncoder.encode(usmarc_record,"UTF-8");
								} catch (UnsupportedEncodingException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}*/
                              
                              result[counter++] = new SUTRS(source_name,
					                    source_collection,
							    null, usmarc_record ,spec);
                                break;
                            case 11: // UK Marc
                                cat.debug("UKMARC");
                              String ukmarc_record = new String((byte[])et.encoding.o);
                              result[counter++] = new SUTRS(source_name,
					                    source_collection,
							    null, ukmarc_record ,spec);
                                break;
                            case 12: // Normarc
                                break;
                            case 13: // Librismarc
                                break;
                            case 14: // Danmarc
                                break;
                            case 15: // Finmarc
                                break;
                            case 21: // IberMarc
                                break;
                            case 22: // CatMarc 
			      // In Thailand, USMarc records contain Cp874 8-bit characters.
			      // We allow the charset property to specify how we should
			      // construct string representations of Marc records.
                              result[counter++] = new com.k_int.IR.Syntaxes.marc.iso2709(
                                                              source_name,source_collection,  
					                      spec,
							      null, 
							      et.encoding.o);
							      // protocol_endpoint.getCharset());
                              break;
                            case 100: // Explain
                              cat.debug("Explain");
                              result[counter++] = new ExplainRecord(source_name,
					                            source_collection,  
								    null, 
								    et.encoding.o,
								    spec);
                              break;
                            case 101: // SUTRS
                              cat.debug("SUTRS");
                              result[counter++] = new SUTRS(source_name,
					                    source_collection,
							    null, 
							    et.encoding.o,
							    spec);
                              break;
                            case 102: // Opac
                              cat.debug("Opac record");
                              result[counter++] = new OpacRecord(source_name,
					                         source_collection,  
								 null, 
								 et.encoding.o,
								 spec);
                            case 105: // GRS1
                              cat.debug("GRS1");
                              result[counter++] = new GRS1(source_name,
					                   source_collection,  
							   null, 
							   (java.util.Vector)(et.encoding.o),
							   spec);
                              break;
                            default:
                              cat.warn("unknow Syntax OID ending with "+et.direct_reference[4]);
                              result[counter++] = new UnknownBlob(source_name,
					                          source_collection,  
							          null, 
							          et.encoding.o,
							          spec);
                              break;
                          }
                          break;

                        case 7:
                          if ( et.direct_reference[5] == 109 )
                          {
                            // Various Brinary formats, PDF, Jpeg, etc... add later
                            switch( et.direct_reference[6] )
                            {
                              case 1:
                                cat.debug("PDF Document...");
                                result[counter++] = new UnknownBlob(source_name,
						                    source_collection,  
								    null, 
								    et.encoding.o,
								    spec);
                                break;
                              case 3:
                                cat.debug("HTML record...");
                                String html_rec = null;
                                if ( et.encoding.o instanceof byte[] )
                                  html_rec = new String((byte[])et.encoding.o);
                                else
                                  html_rec = et.encoding.o.toString();  
                                result[counter++] = new HTMLRecord(source_name,
						                   source_collection,  
								   null, 
								   html_rec,
								   spec);
                                break;
                              case 9:
                                cat.debug("SGML record...");
                                result[counter++] = new SGMLRecord(source_name,
						                   source_collection,  
								   null, 
								   et.encoding.o.toString(),
								   spec);
                                break;
                              case 10:
								String rec = new String((byte[])(et.encoding.o));
                                result[counter++] = new XMLRecord(source_name,
						                  source_collection,  
								  null, 
								  rec,
								  spec);
                                break;
                              default:
                                result[counter++] = new UnknownBlob(source_name,
						                    source_collection,  
								    null, 
								    et.encoding.o,
								    spec);
                                break;
                            }
                          }
			  else
			  {
                            cat.warn("Unhandled 7-int OID for record type");
			  }
                          break;
                      }
                      break;
 
                    case record_inline13_type.surrogatediagnostic_CID:
                      	cat.warn("SurrogateDiagnostic");
		      			DiagRec_type d = (DiagRec_type) npr.record.o;
		      			if ( d.which == DiagRec_type.defaultformat_CID )
		      			{
                        	DefaultDiagFormat_type ddf 	= (DefaultDiagFormat_type)d.o;
							String reason 				= "Diagnostic "+(ddf.addinfo != null ? ddf.addinfo.toString() : "none" );
							setDiagnosticStatus("diag.bib1."+ddf.condition,protocol_endpoint.getTargetName(), source_name+" "+source_collection);
                        	result[counter++] = new SurrogateDiagnostic(source_name,
					                            						source_collection,  
								    									ddf.condition, 
								    									null, 
								    									reason );
		      			}
		      			else // externallydefined_CID
		      			{
		      				setDiagnosticStatus("diag.k-int.7", protocol_endpoint.getTargetName(),source_name+" "+source_collection);
                        	result[counter++] = new SurrogateDiagnostic(source_name,
					                            						source_collection,  
								    									"External", 
								    									null, 
								    									"External");
		      			}
                      break;

                    case record_inline13_type.startingfragment_CID:
                      cat.warn("StartingFragment");
                      break;

                    case record_inline13_type.intermediatefragment_CID:
                      cat.warn("IntermediateFragment");
                      break;

                    case record_inline13_type.finalfragment_CID:
                      cat.warn("FinalFragment");
                      break;

		    default:
                      cat.warn("Unhandled record type");
                }
              }
              else
              {
                cat.info("Error... record ptr is null");
              }
            }
            break;

        case Records_type.nonsurrogatediagnostic_CID:
            // Record contains defaultDiagFormat object
            DefaultDiagFormat_type d = (DefaultDiagFormat_type)r.o;
            if ( d.addinfo != null )
            {
				cat.warn("Non surrogate diagnostics ["+d.condition+"] Additional Info : "+d.addinfo.o);
				setDiagnosticStatus("diag.bib1."+d.condition,protocol_endpoint.getTargetName(),"Additional Info : "+d.addinfo.o);
            }
              
            else
            {
				cat.warn("Non surrogate diagnostics ["+d.condition+"] no additional info");
				setDiagnosticStatus("diag.bib1."+d.condition,protocol_endpoint.getTargetName(),null);
            }
              
            break;

        case Records_type.multiplenonsurdiagnostics_CID:
            cat.warn("Multiple non surrogate diagnostics");
            break;

        default:
            cat.warn("Unknown choice for records response : "+r.which);
            break;
      }
    }
    else
    {
      cat.debug("Records member of present response is null");
    }
    
    return result;
  }
   
  
  public void setFragmentCount(int i)
  {
    cat.debug("Z3950SearchTask::setFragmentCount("+i+")");
    fragment_count = i;

    // SearchTask now extends Observable so call these directly
    IREvent e = new IREvent(IREvent.FRAGMENT_COUNT_CHANGE, new Integer(i));
    setChanged();
    notifyObservers(e);
  }

  public int getFragmentCount()
  {
    return fragment_count;
  } 

  // Cancel any active operation, but leave all the searchTask's data intact 
  public void cancelTask()
  {
    cat.debug("Z3950SearchTask::cancelTask()");
  }

  // A Z3950 Search Task is a lowest level FragmentSource, and that interface is
  // implemented directly by the SearchTask, so simply return this object. We might
  // want to wrap this object in some higher level PreFetch or caching Fragment Source,
  // but that object should be generically reusable.
  public InformationFragmentSource getTaskResultSet()
  {
    // Maybe one day return new CachingFragmentSource(this);
    return this;
  }

  // From InformationFragmentSource interface 
  public void destroy()
  {
    cat.debug("Z3950SearchTask::destroy()");
  }

  // From SearchTask base class
  public void destroyTask()
  {
    super.destroyTask();
    cat.debug("Z3950SearchTask::destroyTask()");
  }

  public AsynchronousEnumeration elements()
  {
    cat.debug("Z3950SearchTask::elements()");
    return new ReadAheadEnumeration(this,default_present_chunk_size);
  }

  public String toString()
  {
    return "Z3950SearchTask - "+getTaskIdentifier();
  }

  public IRStatusReport getStatusReport()
  {
  	return super.getStatusReport();
//  	return null;
/*    return new IRStatusReport(protocol_endpoint.getTargetDN(),
                              protocol_endpoint.getTargetDN(),
                              protocol_endpoint.getTargetDN(),
                              private_status_types[z3950_status],
                              getFragmentCount(),
                              getFragmentCount(),
                              null,
			      			  getLastStatusMessages()); */
  }

  // Hate this... will fix in next point release
  private RecordFormatSpecification interpretSpec(RecordFormatSpecification spec)
  {
    RecordFormatSpecification retval = spec;
    boolean new_spec_needed = false;

    String new_format = strval(spec.getFormatName());
    String new_schema = strval(spec.getSchema());
    String new_esetname = strval(spec.getSetname());

    if ( spec.getFormatName() instanceof IndirectFormatProperty )
    {
      new_format = properties.getProperty(spec.getFormatName().toString());
      new_spec_needed = true;
      cat.debug("actual format will be "+new_format);
    }

    if ( spec.getSchema() instanceof IndirectFormatProperty )
    {
      new_schema = properties.getProperty(spec.getSchema().toString());
      new_spec_needed = true;
      cat.debug("actual format will be "+new_schema);
    }

    if ( spec.getSetname() instanceof IndirectFormatProperty )
    {
      new_esetname = properties.getProperty(spec.getSetname().toString());
      if ( new_esetname == null ) {
        new_esetname = "f";
      }
      new_spec_needed = true;
      cat.debug("actual format will be "+new_esetname);
    }

    if ( new_spec_needed )
    {
      retval = new RecordFormatSpecification(new_format, new_schema, new_esetname);
      cat.debug("Converted "+spec.toString()+" into "+retval);
    }

    return retval;
  }

  private String strval(FormatProperty p)
  {
    if ( p != null )
      return p.toString();

    return null;
  }
}
