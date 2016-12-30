package com.jinter.db.c3p0.dialect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.alibaba.fastjson.JSON;
import com.jinter.core.Table;
import com.jinter.db.c3p0.C3p0DateSouce;
import com.jinter.db.c3p0.DateSource;
import com.jinter.kit.ConstKit;
import com.jinter.kit.PathKit;
import com.jinter.kit.PropKit;

/**
 * 
 * @author clark
 * 
 *         Dec 29, 2016
 */
public class MysqlDialect implements Dialect {

	private Logger logger = Logger.getLogger("MysqlDialect");

	private DateSource dataSource;

	private Connection conn;

	public MysqlDialect() {
		this(null, null, null, null);
	}

	/**
	 * init mysql dialect . first we will try to find Jinter.properties file .
	 * if not exist . then searching for application.properties.
	 * 
	 * @param maxStatements
	 * @param minPoolSize
	 * @param acquireIncrement
	 * @param maxPoolSize
	 */
	public MysqlDialect(Integer maxStatements, Integer minPoolSize, Integer acquireIncrement, Integer maxPoolSize) {

		String driverClass = "com.mysql.jdbc.Driver";
		String fileName = null;
		if (PathKit.hasFile("Jinter.properties")) {
			fileName = "Jinter.properties";
		} else if (PathKit.hasFile("application.properties")) {
			fileName = "application.properties";
		} else {
			throw new IllegalArgumentException(
					"can not find file Jinter.properties or application.properties. please add config file first ");
		}
		String jdbcUrl = (String) PropKit.use(fileName, "jdbcUrl");
		String user = (String) (PropKit.use(fileName, "user") == null ? PropKit.use(fileName, "username")
				: PropKit.use(fileName, "user"));
		String password = (String) PropKit.use(fileName, "password");

		dataSource = new C3p0DateSouce(driverClass, jdbcUrl, user, password, maxStatements, minPoolSize,
				acquireIncrement, maxPoolSize);
		this.conn = dataSource.getConnection();
	}

	/**
	 * @param jsonStr
	 * @return
	 */
	@SuppressWarnings({ "unused", "rawtypes" })
	public boolean buildSimpleTable(String jsonStr) {
		
		if(isTableExist(jsonStr)){
			return true;
		}
		Table table = MysqlDialectHelper.buildTable(jsonStr);

		String sql = MysqlDialectHelper.genCreateTableSql(table);
		logger.info(ConstKit.SQL_FORMAT + sql);
		Statement statement = null;
		try {
			statement = conn.createStatement();
			statement.executeUpdate(sql);
			conn.commit();
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage());
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					throw new RuntimeException(e.getMessage());
				}
			}
		}

		return true;
	}
	
	public boolean isTableExist(String jsonStr) {
		Statement st = null;
		try {
			st = conn.createStatement();
			String sql = "select * from " + MysqlDialectHelper.getTableName(jsonStr) + " where 1 = 2";
			logger.info(ConstKit.SQL_FORMAT + sql);
			st.executeQuery(sql);

		} catch (SQLException e) {
			System.err.println(e.getMessage() + "starting to create");
			return false;
		} finally {
			if (st != null) {
				try {
					st.close();
				} catch (SQLException e) {
				}
			}
		}
		return true;
	}
	
	/**
	 * put json data into database
	 * @param jsonStr
	 */
	@SuppressWarnings("rawtypes")
	public void putData(String jsonStr) {
		if(false == isTableExist(jsonStr)){
			buildSimpleTable(jsonStr);
		}
		List<Map> listMap = MysqlDialectHelper.fetchDataList(jsonStr);
		String sql = MysqlDialectHelper.genInsertSql(listMap, MysqlDialectHelper.getTableName(jsonStr));
		logger.info(ConstKit.SQL_FORMAT + sql);
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			ps.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (ps != null && conn != null) {
				try {
					ps.close();
					conn.close();
				} catch (SQLException e) {
				}
			}
		}
	}
	
	public static void main(String[] args) {
		String jsonStr = "{\"tableName\":\"test\","
				+ "\"jsonDataType\":"
				+ "["
				+ "{\"isNullable\":false,\"columnName\":\"id\",\"columnType\":\"int\",\"columnLength\":11,\"isPrimaryKey\":1},"
				+ "{\"isNullable\":true,\"columnName\":\"name\",\"columnType\":\"varchar\",\"columnLength\":256,\"isPrimaryKey\":0},"
				+ "{\"isNullable\":true,\"columnName\":\"time\",\"columnType\":\"datetime\",\"columnLength\":0,\"isPrimaryKey\":0},"
				+ "{\"isNullable\":true,\"columnName\":\"remark\",\"columnType\":\"varchar\",\"columnLength\":256,\"isPrimaryKey\":0}"
				+ "],"
				+ "\"jsonDataVal\":"
				+ "["
				+ "[{\"id\":1} ,{ \"name\":\"xiaoming\"},{\"time\": \"2016-07-09 00:00:00\"},{\"remark\":\"hello test\"}],"
				+ "[{\"id\":2} ,{ \"name\":\"xiaoming\"},{\"time\": \"2016-07-10 00:00:00\"},{\"remark\":\"ello test\"}]"
				+ "]"
				+ "}";
//		System.out.println(JSON.toJSONString(jsonStr));
		Dialect mysqlDialect = new MysqlDialect();
		mysqlDialect.putData(jsonStr);
	}
}
