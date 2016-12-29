package com.jinter.db.c3p0;

import java.sql.Connection;

/**
 * 
 * @author clark
 * 
 * Dec 29, 2016
 */
public interface DateSource {
	
	/**
	 * get connection 
	 * @return
	 */
	public Connection getConnection();
	
	/**
	 * 
	 * @return
	 */
	public boolean close();
}
