package com.jinter.kit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author clark
 * 
 * Dec 30, 2016
 */
public class DateKit {
	
	/**
	 * only match yyyy-MM-dd hh24:mi:ss
	 * @param strDate
	 * @return
	 */
	public static boolean isDate(String strDate){
		Pattern pattern = Pattern.compile("^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$");
		return pattern.matcher(strDate).matches();
	}
	
		
}
