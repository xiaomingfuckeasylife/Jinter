package com.jinter.core;

import com.jinter.db.c3p0.dialect.Dialect;
import com.jinter.db.c3p0.dialect.MysqlDialect;
/**
 * 
 * @author clark
 * 
 * Dec 30, 2016
 * 
 * core class. 
 * <code>
 * 		Jinter jinter = new Jinter();
 * 		jinter.goFetch(jsonStr);
 * </code>
 */
public class Jinter {
	
	private Dialect dialect; 
	
	public void setDialect(Dialect dialect){
		if(dialect == null){
			throw new IllegalArgumentException("dialect can not be set to null");
		}
		this.dialect = dialect;
	}
	
	public void goFetch(String jsonStr){
		dialect.putData(jsonStr);
	}
	
	public Jinter(){
		this.dialect = new MysqlDialect(); // default dialect
	}
	
	public Jinter(String configPath){
		
		this.dialect = new MysqlDialect(configPath);
	}
}
