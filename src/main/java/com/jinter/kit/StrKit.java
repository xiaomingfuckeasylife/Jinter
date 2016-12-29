package com.jinter.kit;
/**
 * 
 * @author clark
 * 
 * Dec 29, 2016
 */
public final class StrKit<T> {
	
	public static<T> boolean isBlank(T str){
		
		return str == null || "".equals((str+"").trim());
	}
	
	
}
