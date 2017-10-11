package org.apache.logging.log4j.spring.boot.appender.db.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.logging.log4j.core.appender.db.jdbc.ConnectionSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 使用已有数据源作为连接获取基础
 */
public class JDBCConnectionSource implements ConnectionSource {

	private static Logger LOG = LoggerFactory.getLogger(JDBCConnectionSource.class);
	private DataSource dataSource;
	
	public JDBCConnectionSource(DataSource dataSource){
		this.dataSource = dataSource;
	}
	
	@Override
	public Connection getConnection() throws SQLException {
		LOG.debug("Get ");
		return dataSource.getConnection();
	}
	
}