package com.jinter.db.c3p0.dialect;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.jinter.core.Column;
import com.jinter.core.Table;
import com.jinter.exception.JinterException;
import com.jinter.kit.ConstKit;
import com.jinter.kit.PathKit;
import com.jinter.kit.PropKit;
import com.jinter.kit.StrKit;

import oracle.jdbc.OracleDriver;

public abstract class Helper {
	
	public abstract String genInsertSql(List<Map> listMap , String tableName);
	
	public static boolean isOracleDriver(String cfgPath){
		String fileName = null;
		if (cfgPath != null && PathKit.hasFile(cfgPath)) {
			fileName = cfgPath;
		} else if (PathKit.hasFile("Jinter.properties")) {
			fileName = "Jinter.properties";
		} else if (PathKit.hasFile("application.properties")) {
			fileName = "application.properties";
		} else {
			throw new IllegalArgumentException(
					"can not find file Jinter.properties or application.properties. or the configured config path. please add config file first ");
		}
		String driverClass = (String) PropKit.use(fileName, "driverClass");
		Class<?> clazz;
		try {
			clazz = Class.forName(driverClass);
			if(clazz.newInstance() instanceof OracleDriver || clazz.newInstance() instanceof oracle.jdbc.driver.OracleDriver){
				return true;
			}
		} catch (Exception e) {
			throw new JinterException(e.getMessage());
		}
		return false;
	}
	
	public abstract String genCreateTableSql(Table table) ;
	
	@SuppressWarnings("rawtypes")
	public static String getTableName(String jsonStr){
		return (String) ((Map)JSON.parse(jsonStr)).get("tableName");
	}
	
	@SuppressWarnings("rawtypes")
	public static Table buildTable(String jsonStr) {
		Table table = new Table();
		List<Column> colList = new ArrayList<Column>();
		Map jsonMap = (Map) JSON.parse(jsonStr);
		table.tableName = (String) jsonMap.get("tableName");

		JSONArray jsonList = (JSONArray) jsonMap.get("jsonDataType");
		
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
			
			// can be null .
			col.fkTableAndColumn = (String)mapObj.get("fkTableAndColumn");
			
			if (col.isPrimaryKey == 1) {
				sb.append(col.name).append(ConstKit.PK_SEP);
			}
			colList.add(col);
		}
		table.primaryKeys = sb.toString().substring(0, sb.toString().length() - 1);
		table.cols = colList;
		
		return table;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List<Map> fetchDataList(String jsonStr){
		List<Map> listMap = new ArrayList<Map>();
		Map jsonMap = (Map) JSON.parse(jsonStr);
		JSONArray jsonArray = (JSONArray) jsonMap.get("jsonDataVal");
		
		for(int i=0;i<jsonArray.size();i++){
			JSONArray jsonList = (JSONArray) jsonArray.get(i);
			Map retMap = new LinkedHashMap();
			for(int j=0;j<jsonList.size();j++){
				Map entryMap = (Map) jsonList.get(j);
				String key = (String) entryMap.keySet().iterator().next();
				Object val = entryMap.get(key);
				if(StrKit.isBlank(key)){
					throw new IllegalArgumentException("columnName can not be blank");
				}
				retMap.put(key, val);
			}
			listMap.add(retMap);
		}
		
		return listMap;
	}
	
	
}
