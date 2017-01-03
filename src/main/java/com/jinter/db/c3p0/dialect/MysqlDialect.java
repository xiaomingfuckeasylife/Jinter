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
import com.jinter.exception.JinterException;
import com.jinter.kit.ConstKit;
import com.jinter.kit.PathKit;
import com.jinter.kit.PropKit;

/**
 * 
 * @author clark
 * 
 *         Dec 29, 2016
 */
public class MysqlDialect extends Dialect {
	
	private Logger logger = Logger.getLogger("MysqlDialect");
	/**
	 * init dialect by default configure file path
	 */
	public MysqlDialect() {
		this(null, null, null, null, null);
	}
	
	/**
	 * init dialect by the specific configure file path.
	 * @param cfgPath
	 */
	public MysqlDialect(String cfgPath) {
		this(null, null, null, null, cfgPath);
	}

	/**
	 * init mysql dialect . first we will check if specific configure path has been set . if not .then we will try to find Jinter.properties file .
	 * if not exist . then searching for application.properties.
	 * 
	 * @param maxStatements
	 * @param minPoolSize
	 * @param acquireIncrement
	 * @param maxPoolSize
	 */
	public MysqlDialect(Integer maxStatements, Integer minPoolSize, Integer acquireIncrement, Integer maxPoolSize,
			String cfgPath) {
		initCfg(maxStatements, minPoolSize, acquireIncrement, maxPoolSize, cfgPath);
	}

	/**
	 * @param jsonStr
	 * @return
	 */
	public boolean buildSimpleTable(String jsonStr) {
		Statement statement = null;
		try {
			if (isTableExist(jsonStr)) {
				return true;
			}
			Table table = MysqlDialectHelper.buildTable(jsonStr);

			String sql = MysqlDialectHelper.me().genCreateTableSql(table);
			logger.info(ConstKit.SQL_FORMAT + sql);
			statement = conn.createStatement();
			statement.executeUpdate(sql);
			conn.commit();
		} catch (SQLException e) {
			throw new JinterException(e.getMessage());
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {

				}
			}
		}

		return true;
	}


	/**
	 * put json data into database
	 * 
	 * @param jsonStr
	 */
	@SuppressWarnings("rawtypes")
	public void putData(String jsonStr) {
		PreparedStatement ps = null;
		try {
			if (false == isTableExist(jsonStr)) {
				buildSimpleTable(jsonStr);
			}
			List<Map> listMap = MysqlDialectHelper.fetchDataList(jsonStr);
			String sql = MysqlDialectHelper.me().genInsertSql(listMap, MysqlDialectHelper.getTableName(jsonStr));
			logger.info(ConstKit.SQL_FORMAT + sql);
			ps = conn.prepareStatement(sql);
			ps.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			throw new JinterException(e.getMessage());
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
	
}
