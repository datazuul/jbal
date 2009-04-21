package JSites.utils;

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
