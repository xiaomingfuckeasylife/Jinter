package com.jinter.core;

import com.jinter.db.c3p0.dialect.Dialect;
import com.jinter.db.c3p0.dialect.Helper;
import com.jinter.db.c3p0.dialect.MysqlDialect;
import com.jinter.db.c3p0.dialect.Oracle.OracleDialect;
import com.jinter.exception.JinterException;

/**
 * 
 * @author clark
 * 
 *         Dec 30, 2016
 * <p>
 *         core class. 
 *         <br/>
 *  <code>
 * 		Jinter jinter = new Jinter();
 * 		jinter.goFetch(jsonStr);
 * </code>
 * </p>
 */
public class Jinter {

	private Dialect dialect;

	public void setDialect(Dialect dialect) {
		if (dialect == null) {
			throw new IllegalArgumentException("dialect can not be set to null");
		}
		this.dialect = dialect;
	}

	public void goFetch(String jsonStr) {
		dialect.putData(jsonStr);
	}

	public Jinter() {
		if (Helper.isOracleDriver(null)) {
			this.dialect = new OracleDialect();
		} else {
			this.dialect = new MysqlDialect(); // default dialect
		}
	}

	public Jinter(String configPath) {
		if (Helper.isOracleDriver(configPath)) {
			this.dialect = new OracleDialect();
		} else {
			this.dialect = new MysqlDialect(); // default dialect
		}
	}
	
}
