package com.jinter.db.c3p0.dialect;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

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

	public MysqlDialect(Integer maxStatements, Integer minPoolSize, Integer acquireIncrement, Integer maxPoolSize) {

		String driverClass = "com.mysql.jdbc.Driver";
		String fileName = null;
		if (PathKit.hasFile("Jinter.properties")) {
			fileName = "Jinter.properties";
		} else if (PathKit.hasFile("application.properties")) {
			fileName = "application.properties";
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
	 * jsonStr : {\
	 * "tableName\":\"test\",\"jsonData\":[{\"isNullable\":false,\"columnName\":\"id\",\"columnType\":\"int\",\"columnLength\":11,\"isPrimaryKey\":1,\"columnValue\":1},{\"isNullable\":true,\"columnName\":\"name\",\"columnType\":\"varchar\",\"columnLength\":256,\"isPrimaryKey\":0,\"columnValue\":\"xiaoming\"
	 * }]}
	 * 
	 * @param jsonStr
	 * @return
	 */
	@SuppressWarnings({ "unused", "rawtypes" })
	public boolean buildSimpleTable(String jsonStr) {

		Table table = MysqlDialectHelper.buildTable(jsonStr);

		String sql = MysqlDialectHelper.createTableSql(table);
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
			System.err.println(e.getMessage());
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
	
	public static void main(String[] args) {

		String jsonStr = "{\"tableName\":\"test\",\"jsonData\":[{\"isNullable\":false,\"columnName\":\"id\",\"columnType\":\"int\",\"columnLength\":11,\"isPrimaryKey\":1,\"columnValue\":1},{\"isNullable\":true,\"columnName\":\"name\",\"columnType\":\"varchar\",\"columnLength\":256,\"isPrimaryKey\":0,\"columnValue\":\"xiaoming\"}]}";
		MysqlDialect mysqlDialect = new MysqlDialect();
		if(mysqlDialect.isTableExist(jsonStr)){
			System.out.println("table already exist");
		}else{
			mysqlDialect.buildSimpleTable(jsonStr);
		}
		
	}
}