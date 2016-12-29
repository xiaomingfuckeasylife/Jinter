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
import com.jinter.kit.ConstKit;
import com.jinter.kit.StrKit;

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
	
	@SuppressWarnings("rawtypes")
	public static String genInsertSql(List<Map> listMap , String tableName){
		
		StringBuilder sbHead = new StringBuilder();
		sbHead.append("insert into " + tableName +"(");
		StringBuilder sbTail = new StringBuilder();
		sbTail.append("values ");
		for(int i=0;i<listMap.size();i++){
			Map map = (Map) listMap.get(i);
			Set set = map.keySet();
			Iterator it = set.iterator();
			sbTail.append("(");
			while(it.hasNext()){
				String column = (String) it.next();
				if(i == 0){
					sbHead.append(column);
				}
				Object val = map.get(column);
				if(val instanceof String){
					sbTail.append("'" + val + "'");
				}else {
					sbTail.append(val);
				}
				if(true == it.hasNext()){
					if(i== 0){
						sbHead.append(",");
					}
					sbTail.append(",");
				}
			}
			if(i==0){
				sbHead.append(")");
			}
			sbTail.append(")");
			if(i != listMap.size() -1){
				sbTail.append(",");
			}
		}
		
		return sbHead .toString() + sbTail.toString();
	}
	
	@SuppressWarnings("rawtypes")
	public static List<Map> fetchDataList(String jsonStr){
		List<Map> listMap = new ArrayList<Map>();
		Map jsonMap = (Map) JSON.parse(jsonStr);
		JSONArray jsonArray = (JSONArray) jsonMap.get("jsonData");
		
		for(int i=0;i<jsonArray.size();i++){
			JSONArray jsonList = (JSONArray) jsonArray.get(i);
			Map retMap = new LinkedHashMap();
			for(int j=0;j<jsonList.size();j++){
				Map map = (Map) jsonList.get(j);
				String key = (String) map.get("columnName");
				Object val = map.get("columnValue");
				if(StrKit.isBlank(key)){
					throw new IllegalArgumentException("columnName can not be blank");
				}
				retMap.put(key, val);
			}
			listMap.add(retMap);
		}
		
		return listMap;
	}
	
	public static String getTableName(String jsonStr){
		return (String) ((Map)JSON.parse(jsonStr)).get("tableName");
	}
	
	public static Table buildTable(String jsonStr) {
		Table table = new Table();
		List<Column> colList = new ArrayList<Column>();
		Map jsonMap = (Map) JSON.parse(jsonStr);
		table.tableName = (String) jsonMap.get("tableName");

		JSONArray jsonL = (JSONArray) jsonMap.get("jsonData");
		
		JSONArray jsonList = (JSONArray) jsonL.get(0);
		
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
			if(c.isNullable == false){
				sb.append(" not null");
			}
			sb.append(",");
		}
		if(false == StrKit.isBlank(table.primaryKeys)){
			sb.append(" PRIMARY KEY (" + table.primaryKeys +")");
		}
		sb.append(")");
		return sb.toString();
	}
	
	
	
}
