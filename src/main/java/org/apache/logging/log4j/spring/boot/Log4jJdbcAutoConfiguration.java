package org.apache.logging.log4j.spring.boot;

import javax.sql.DataSource;

import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.spring.boot.ext.Log4jDataSource;
import org.apache.logging.log4j.spring.boot.ext.Log4jJdbcAppenderTemplate;
import org.apache.logging.log4j.spring.boot.ext.Log4jJdbcInitApplicationListener;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Log4j基于JDBC存储日志的配置类
 * http://www.cnblogs.com/bigbang92/p/Log4j2.html
 *
 * <p>Auto-configuration for Log4jJdbc integration.
 * Registers the necessary beans when the feature is enabled.</p>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Configuration
@ConditionalOnClass({ Logger.class })
@ConditionalOnProperty(name = { "logging.log4j.jdbc.enabled" }, havingValue = "true", matchIfMissing = false)
@EnableConfigurationProperties({ Log4jJdbcProperties.class })
public class Log4jJdbcAutoConfiguration {

	@Configuration
	@ConditionalOnMissingBean(Logger.class)
	@EnableConfigurationProperties(Log4jJdbcProperties.class)
	@ConditionalOnSingleCandidate(DataSource.class)
	public static class Log4jJdbcConfiguration {

		private final Log4jJdbcProperties jdbcProperties;

		private final DataSource dataSource;

		private final DataSource log4jDataSource;

		public Log4jJdbcConfiguration(Log4jJdbcProperties jdbcProperties,
				ObjectProvider<DataSource> dataSource,
				@Log4jDataSource ObjectProvider<DataSource> log4jDataSource) {
			this.jdbcProperties = jdbcProperties;
			this.dataSource = dataSource.getIfUnique();
			this.log4jDataSource = log4jDataSource.getIfAvailable();
		}

		@Bean
		public Log4jJdbcAppenderTemplate jdbcAppenderTemplate() {
			Log4jJdbcAppenderTemplate template = new Log4jJdbcAppenderTemplate();
			if (this.log4jDataSource != null) {
				template.setDataSource(this.log4jDataSource);
			} else if (this.dataSource != null) {
				template.setDataSource(this.dataSource);
			}
			return template;
		}

		@Bean
		public Log4jJdbcInitApplicationListener log4jJdbcInitApplicationListener(Log4jJdbcAppenderTemplate jdbcAppenderTemplate) {
			Log4jJdbcInitApplicationListener listener = new Log4jJdbcInitApplicationListener();
			listener.setJdbcAppenderTemplate(jdbcAppenderTemplate);
			listener.setProperties(jdbcProperties);
			return listener;
		}

	}

}
