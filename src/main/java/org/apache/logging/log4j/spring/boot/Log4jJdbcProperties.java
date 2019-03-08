package org.apache.logging.log4j.spring.boot;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.spring.boot.ext.Log4jJdbcAppenderProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 */
@ConfigurationProperties(Log4jJdbcProperties.PREFIX)
public class Log4jJdbcProperties {

	public static final String PREFIX = "logging.log4j.jdbc";

	/**
	 * Whether use current Context. if false the LoggerContext appropriate for the
	 * caller of this method is returned. For example, in a web application if the
	 * caller is a class in WEB-INF/lib then one LoggerContext may be returned and
	 * if the caller is a class in the container's classpath then a different
	 * LoggerContext may be returned. If true then only a single LoggerContext will
	 * be returned.
	 **/
	private boolean currentContext = false;
	/**
	 * Whether to enable log4j jdbc.
	 */
	private boolean enabled = true;
	/**
	 * Whether to use distributed database middleware .
	 */
	private boolean shardingJdbc = false;

	private List<Log4jJdbcAppenderProperties> appenders = new ArrayList<Log4jJdbcAppenderProperties>();

	public boolean isCurrentContext() {
		return currentContext;
	}

	public void setCurrentContext(boolean currentContext) {
		this.currentContext = currentContext;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isShardingJdbc() {
		return shardingJdbc;
	}

	public void setShardingJdbc(boolean shardingJdbc) {
		this.shardingJdbc = shardingJdbc;
	}

	public List<Log4jJdbcAppenderProperties> getAppenders() {
		return appenders;
	}

	public void setAppenders(List<Log4jJdbcAppenderProperties> appenders) {
		this.appenders = appenders;
	}

}
