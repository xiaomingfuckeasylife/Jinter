package com.jinter.core;

/**
 * 
 * @author clark
 * 
 *         Dec 29, 2016
 */
public class Column {
	
	public String name;
	public Integer length;	 
	public String type;  // mysql database type 
	public Integer isPrimaryKey; //  0 means not primary key , 1 means primary key.
	public Boolean isNullable ; // column is nullable 
	
	
}
