package org.apache.logging.log4j.spring.boot;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.spring.boot.ext.Log4jJdbcAppenderProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
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
	/**
	 * <p>Is current context.</p>
	 * @return the boolean
	 */

	public boolean isCurrentContext() {
		return currentContext;
	}
	/** Sets the current context. */

	public void setCurrentContext(boolean currentContext) {
		this.currentContext = currentContext;
	}
	/**
	 * <p>Is enabled.</p>
	 * @return the boolean
	 */

	public boolean isEnabled() {
		return enabled;
	}
	/** Sets the enabled. */

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	/**
	 * <p>Is sharding jdbc.</p>
	 * @return the boolean
	 */

	public boolean isShardingJdbc() {
		return shardingJdbc;
	}
	/** Sets the sharding jdbc. */

	public void setShardingJdbc(boolean shardingJdbc) {
		this.shardingJdbc = shardingJdbc;
	}
	/** Gets the appenders. */

	public List<Log4jJdbcAppenderProperties> getAppenders() {
		return appenders;
	}
	/** Sets the appenders. */

	public void setAppenders(List<Log4jJdbcAppenderProperties> appenders) {
		this.appenders = appenders;
	}

}
