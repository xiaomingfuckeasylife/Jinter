package com.jinter.core;

import java.util.List;
/**
 * 
 * @author clark
 * 
 * Dec 29, 2016
 */
public class Table {
	
	public String tableName;
	public String primaryKeys; // if there are more then one column as primary key . then seperate them using ","
	public List<Column> cols;
	
}
