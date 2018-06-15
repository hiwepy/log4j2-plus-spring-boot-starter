package org.apache.logging.log4j.spring.boot;

import javax.sql.DataSource;

import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.spring.boot.ext.Log4jDataSource;
import org.apache.logging.log4j.spring.boot.ext.Log4jJdbcAppenderTemplate;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

/**
 * 
 * @author Administrator
 * @see http://www.cnblogs.com/bigbang92/p/Log4j2.html
 * @see
 */
@Configuration
@ConditionalOnClass({ Logger.class })
@ConditionalOnBean(DataSource.class)
@ConditionalOnProperty(name = { "logging.log4j.jdbc.enabled" }, havingValue = "true", matchIfMissing = false)
@EnableConfigurationProperties({ Log4jJdbcProperties.class })
@AutoConfigureAfter({ DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class })
public class Log4jJdbcAutoConfiguration {
	
	@Configuration
	@ConditionalOnMissingBean(Logger.class)
	@EnableConfigurationProperties(Log4jJdbcProperties.class)
	public static class Log4jJdbcConfiguration {

		private final Log4jJdbcProperties properties;

		private final DataSourceProperties dataSourceProperties;

		private final DataSource dataSource;

		private final DataSource log4jDataSource;

		public Log4jJdbcConfiguration(Log4jJdbcProperties properties, 
				DataSourceProperties dataSourceProperties,
				ResourceLoader resourceLoader, ObjectProvider<DataSource> dataSource,
				@Log4jDataSource ObjectProvider<DataSource> log4jDataSource) {
			this.properties = properties;
			this.dataSourceProperties = dataSourceProperties;
			this.dataSource = dataSource.getIfUnique();
			this.log4jDataSource = log4jDataSource.getIfAvailable();
		}
		
		@Bean
		@ConfigurationProperties(prefix = "logging.log4j.jdbc")
		public Log4jJdbcAppenderTemplate jdbcAppenderTemplate() {
			Log4jJdbcAppenderTemplate template = new Log4jJdbcAppenderTemplate();
			if (this.properties.isCreateDataSource()) {
				template.setDataSource(properties.initializeDataSourceBuilder().build());
			} else if (this.log4jDataSource != null) {
				template.setDataSource(this.log4jDataSource);
			} else if (this.dataSource != null) {
				template.setDataSource(this.dataSource);
			} else if (this.dataSourceProperties != null) {
				template.setDataSource(this.dataSourceProperties.initializeDataSourceBuilder().build());
			}
			template.setProperties(properties);
			return template;
		}

	}

}
