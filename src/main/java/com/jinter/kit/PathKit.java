package com.jinter.kit;
/**
 * 
 * @author clark
 * 
 * Dec 29, 2016
 */
final public class PathKit {
	
	public static boolean hasFile(String filePath){
		
		return Thread.currentThread().getContextClassLoader().getResource(filePath) != null;
		
	}
	
}
