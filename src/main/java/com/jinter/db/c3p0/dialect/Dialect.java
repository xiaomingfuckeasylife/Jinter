package com.jinter.db.c3p0.dialect;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

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
public abstract class Dialect {

	protected DateSource dataSource;

	protected Connection conn;

	protected boolean isMysqlDriver = true;
	
	private String driverClass;
	private String jdbcUrl;
	private String user;
	private String password;

	private Logger logger = Logger.getLogger(Dialect.class.getName());

	public abstract void putData(String jsonStr);

	public abstract boolean buildSimpleTable(String jsonStr);

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

	public void initCfg(Integer maxStatements, Integer minPoolSize, Integer acquireIncrement, Integer maxPoolSize,
			String cfgPath) {
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
		
		this.driverClass = (String) PropKit.use(fileName, "driverClass");
		this.jdbcUrl = (String) PropKit.use(fileName, "jdbcUrl");
		this.user = (String) (PropKit.use(fileName, "user") == null ? PropKit.use(fileName, "username")
				: PropKit.use(fileName, "user"));
		this.password = (String) PropKit.use(fileName, "password");
		dataSource = new C3p0DateSouce(driverClass, jdbcUrl, user, password, maxStatements, minPoolSize,
				acquireIncrement, maxPoolSize);
		conn = dataSource.getConnection();
	}
	
	
}
