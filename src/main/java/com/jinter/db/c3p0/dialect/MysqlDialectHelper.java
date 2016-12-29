package com.jinter.db.c3p0.dialect;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.jinter.core.Column;
import com.jinter.core.Table;
import com.jinter.kit.ConstKit;
import com.jinter.kit.StrKit;
import com.mysql.jdbc.profiler.LoggingProfilerEventHandler;

/**
 * 
 * @author clark
 * 
 *         Dec 29, 2016
 */
/*
 * help build mysqlDialect
 */
public class MysqlDialectHelper {
	
	public static String getTableName(String jsonStr){
		
		return (String) ((Map)JSON.parse(jsonStr)).get("tableName");
	}
	
	public static Table buildTable(String jsonStr) {
		Table table = new Table();
		List<Column> colList = new ArrayList<Column>();
		Map jsonMap = (Map) JSON.parse(jsonStr);
		table.tableName = (String) jsonMap.get("tableName");

		JSONArray jsonList = (JSONArray) jsonMap.get("jsonData");
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < jsonList.size(); i++) {
			Map mapObj = (Map) jsonList.get(i);
			Column col = new Column();
			col.isPrimaryKey = (Integer) mapObj.get("isPrimaryKey");
			if(StrKit.isBlank(col.isPrimaryKey)){
				throw new IllegalArgumentException("isPrimaryKey's value can not be null");
			}
			col.length = (Integer) mapObj.get("columnLength");
			if(StrKit.isBlank(col.length)){
				throw new IllegalArgumentException("length's value can not be null");
			}
			col.name = (String) mapObj.get("columnName");
			if(StrKit.isBlank(col.name)){
				throw new IllegalArgumentException("name's value can not be null");
			}
			col.type = (String) mapObj.get("columnType");
			if(StrKit.isBlank(col.type)){
				throw new IllegalArgumentException("type's value can not be null");
			}
			
			col.isNullable = (Boolean) mapObj.get("isNullable");
			if(StrKit.isBlank(col.isNullable)){
				throw new IllegalArgumentException("isNullable's value can not be null");
			}
			
			if (col.isPrimaryKey == 1) {
				sb.append(col.name).append(ConstKit.PK_SEP);
			}
			colList.add(col);
		}
		table.primaryKeys = sb.toString().substring(0, sb.toString().length() - 1);
		table.cols = colList;
		
		return table;
	}
	
	public static String createTableSql(Table table) {
		StringBuilder sb = new StringBuilder();
		sb.append(" create table ").append(table.tableName).append("(");
		List<Column> cols = table.cols;
		for (int i = 0; i < cols.size(); i++) {
			Column c = cols.get(i);
			if(c.type.equalsIgnoreCase("char") || c.type.equalsIgnoreCase("varchar")){
				sb.append(c.name +"  " + c.type +  "(" +c.length + ")"); 
			}else{
				sb.append(c.name + "  " + c.type);
			}
			if(cols.size() -1 != i){
				sb.append(",");
			}
		}
		sb.append(")");
		return sb.toString();
	}
}
