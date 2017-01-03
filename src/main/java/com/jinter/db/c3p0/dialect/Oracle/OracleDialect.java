package com.jinter.db.c3p0.dialect.Oracle;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.jinter.core.Table;
import com.jinter.db.c3p0.dialect.Dialect;
import com.jinter.db.c3p0.dialect.MysqlDialectHelper;
import com.jinter.exception.JinterException;
import com.jinter.kit.ConstKit;

public class OracleDialect extends Dialect{
	
	private Logger logger = Logger.getLogger(OracleDialect.class.getName());
	
	/**
	 * init dialect by default configure file path
	 */
	public OracleDialect() {
		this(null, null, null, null, null);
	}
	
	/**
	 * init dialect by the specific configure file path.
	 * @param cfgPath
	 */
	public OracleDialect(String cfgPath) {
		this(null, null, null, null, cfgPath);
	}

	/**
	 * init oracle dialect . first we will check if specific configure path has been set . if not then try to find Jinter.properties file .
	 * if not exist . then searching for application.properties. 
	 * 
	 * @param maxStatements
	 * @param minPoolSize
	 * @param acquireIncrement
	 * @param maxPoolSize
	 */
	public OracleDialect(Integer maxStatements, Integer minPoolSize, Integer acquireIncrement, Integer maxPoolSize,
			String cfgPath){
		isMysqlDriver = false;
		initCfg(maxStatements, minPoolSize, acquireIncrement, maxPoolSize, cfgPath);
	}
	
	
	@SuppressWarnings("rawtypes")
	public void putData(String jsonStr) {
		PreparedStatement ps = null;
		try {
			if (false == isTableExist(jsonStr)) {
				buildSimpleTable(jsonStr);
			}
			List<Map> listMap = OracleDialectHelper.fetchDataList(jsonStr);
			String sql = OracleDialectHelper.me().genInsertSql(listMap, MysqlDialectHelper.getTableName(jsonStr));
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
	
	@Override
	public boolean buildSimpleTable(String jsonStr) {
		
		Table table = OracleDialectHelper.buildTable(jsonStr);
		String sql = OracleDialectHelper.me().genCreateTableSql(table);
		logger.info(ConstKit.SQL_FORMAT + sql);
		Statement st = null;
		try {
			st = conn.createStatement();
			st.executeUpdate(sql);
			conn.commit();
		} catch (SQLException e) {
			throw new JinterException(e.getMessage());
		}finally{
			if(st != null){
				try {
					st.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		return true;
	}
	
}
