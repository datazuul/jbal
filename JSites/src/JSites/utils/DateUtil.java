package JSites.utils;
/*******************************************************************************
*
*  JOpac2 (C) 2002-2009 JOpac2 project
*
*     This file is part of JOpac2. http://www.jopac2.org
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
*  Please, see NOTICE.txt AND LEGAL directory for more info. Different licences
*  may apply for components included in JOpac2.
*
*******************************************************************************/
import java.util.Calendar;
import java.util.TimeZone;

public class DateUtil {
	public static String getDateString() {
		Calendar cal = Calendar.getInstance(TimeZone.getDefault());
	    
	    String DATE_FORMAT = "yyyyMMdd";
	    java.text.SimpleDateFormat sdf = 
	          new java.text.SimpleDateFormat(DATE_FORMAT);
	    
	    //sdf.setTimeZone(TimeZone.getTimeZone("EST"));

	    sdf.setTimeZone(TimeZone.getDefault());          
	          
	    return sdf.format(cal.getTime());
	}
}
