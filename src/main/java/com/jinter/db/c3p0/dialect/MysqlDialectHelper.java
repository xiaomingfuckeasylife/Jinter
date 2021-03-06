package com.jinter.db.c3p0.dialect;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.jinter.core.Column;
import com.jinter.core.Table;
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
public class MysqlDialectHelper extends Helper{
	
	public static MysqlDialectHelper me = new MysqlDialectHelper();
	
	public static MysqlDialectHelper me(){
		
		return me;
	}
	private MysqlDialectHelper() {
		
	}
	
	
	@Override
	public String genCreateTableSql(Table table) {
		StringBuilder sb = new StringBuilder();
		sb.append(" create table ").append(table.tableName).append("(");
		List<Column> cols = table.cols;
		Map<String,String> fkTableAndColumnMap = new HashMap<String,String>();
		for (int i = 0; i < cols.size(); i++) {
			Column c = cols.get(i);
			if(c.type.equalsIgnoreCase("char") || c.type.equalsIgnoreCase("varchar") || c.type.equalsIgnoreCase("NUMBER")){
				sb.append(c.name +"  " + c.type +  "(" +c.length + ")"); 
			}else{
				sb.append(c.name + "  " + c.type);
			}
			if(c.isNullable == false){
				sb.append(" not null");
			}
			if(c.fkTableAndColumn != null){
				fkTableAndColumnMap.put(c.name,c.fkTableAndColumn);
			}
			sb.append(",");
		}
		
		if(false == StrKit.isBlank(table.primaryKeys)){
			sb.append(" PRIMARY KEY (" + table.primaryKeys +")");
			Set<String> keySet = fkTableAndColumnMap.keySet();
			Iterator<String> it = keySet.iterator();
			while(it.hasNext()){
				String name = it.next();
				String fkTableAndColumn = fkTableAndColumnMap.get(name);
;				if(fkTableAndColumn != null){
					String[] tableAndColumn = fkTableAndColumn.split(";");
					sb.append(", CONSTRAINT s_" + name + "_" +tableAndColumn[0] +"_"+tableAndColumn[1] + "_fk " + "foreign key ("+tableAndColumn[2]+") REFERENCES " + tableAndColumn[0] + "(" + tableAndColumn[1]
							+ ")");
				}
			}
		}
		sb.append(")");
		return sb.toString();
	}
	
	@Override
	@SuppressWarnings("rawtypes")
	public String genInsertSql(List<Map> listMap , String tableName){
		
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

	
}
