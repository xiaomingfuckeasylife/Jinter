package com.jinter.db.c3p0;

import java.beans.PropertyVetoException;
import java.sql.Connection;

import javax.sql.DataSource;

import com.jinter.kit.StrKit;
import com.mchange.v2.c3p0.ComboPooledDataSource;
/**
 * 
 * @author clark
 * 
 * Dec 29, 2016
 */
public class C3p0DateSouce implements DateSource {
	
	private String driverClass;
	private String jdbcUrl;
	private String user;
	private String password;
	private Integer maxStatements =50;
	private Integer minPoolSize = 10;
	private Integer acquireIncrement = 5;
	private Integer maxPoolSize = 20;
	
	private ComboPooledDataSource dataSource ; 
	private Connection conn;
	
	public Connection getConnection() {
		dataSource = new ComboPooledDataSource();
		try {
			dataSource.setDriverClass(driverClass);
			dataSource.setJdbcUrl(jdbcUrl);
			dataSource.setUser(user);
			dataSource.setPassword(password);
			dataSource.setMaxStatements(maxStatements);
			dataSource.setMinPoolSize(minPoolSize);
			dataSource.setAcquireIncrement(acquireIncrement);
			dataSource.setMaxPoolSize(maxPoolSize);
			conn = dataSource.getConnection();
		} catch (Exception e) {
			throw new IllegalArgumentException(e.getMessage());
		}
		return conn;
	}
	
	public C3p0DateSouce(String driverClass,String jdbcUrl,String user,String password){
		this(driverClass,jdbcUrl,user,password,null,null,null,null);
	}
	
	public C3p0DateSouce(String driverClass,String jdbcUrl,String user,String password,
			Integer maxStatements , Integer minPoolSize , Integer acquireIncrement , Integer maxPoolSize){
		if(StrKit.isBlank(driverClass)){
			throw new IllegalArgumentException("driverClass can not be blank");
		}
		if(StrKit.isBlank(jdbcUrl)){
			throw new IllegalArgumentException("jdbcUrl can not be blank");
		}
		if(StrKit.isBlank(user)){
			throw new IllegalArgumentException("username can not be blank");
		}
		if(StrKit.isBlank(password)){
			throw new IllegalArgumentException("password can not be blank");
		}
		
		this.driverClass = driverClass;
		this.jdbcUrl = jdbcUrl;
		this.user = user;
		this.password = password;
		
		if(false == StrKit.isBlank(maxStatements)){
			this.maxStatements = maxStatements;
		}
		if(false == StrKit.isBlank(minPoolSize)){
			this.minPoolSize = minPoolSize;
		}
		if(false == StrKit.isBlank(acquireIncrement)){
			this.acquireIncrement = acquireIncrement;
		}
		if(false == StrKit.isBlank(maxPoolSize)){
			this.maxPoolSize = maxPoolSize;
		}
	}
	

	public void setMaxStatements(Integer maxStatements) {
		this.maxStatements = maxStatements;
	}

	public void setMinPoolSize(Integer minPoolSize) {
		this.minPoolSize = minPoolSize;
	}


	public void setAcquireIncrement(Integer acquireIncrement) {
		this.acquireIncrement = acquireIncrement;
	}

	public void setMaxPoolSize(Integer maxPoolSize) {
		this.maxPoolSize = maxPoolSize;
	}
	
	public boolean close() {
		dataSource.close();
		return true;
	}
	
	
}
