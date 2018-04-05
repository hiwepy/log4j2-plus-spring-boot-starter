package org.apache.logging.log4j.spring.boot.ext;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.logging.log4j.core.appender.db.jdbc.AbstractConnectionSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 使用已有数据源作为连接获取基础
 */
public class Log4jJdbcConnectionSource extends AbstractConnectionSource {

	private static Logger LOG = LoggerFactory.getLogger(Log4jJdbcConnectionSource.class);
	private DataSource dataSource;
	
	public Log4jJdbcConnectionSource(DataSource dataSource){
		this.dataSource = dataSource;
	}
	
	@Override
	public Connection getConnection() throws SQLException {
		LOG.debug("Get ");
		return dataSource.getConnection();
	}
	
}