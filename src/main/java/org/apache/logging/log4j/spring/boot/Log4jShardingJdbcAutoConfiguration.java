package org.apache.logging.log4j.spring.boot;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.spring.boot.ext.Log4jDataSource;
import org.apache.logging.log4j.spring.boot.ext.Log4jJdbcAppenderTemplate;
import org.apache.logging.log4j.spring.boot.shardingjdbc.common.SpringBootConfigMapConfigurationProperties;
import org.apache.logging.log4j.spring.boot.shardingjdbc.common.SpringBootPropertiesConfigurationProperties;
import org.apache.logging.log4j.spring.boot.shardingjdbc.masterslave.SpringBootMasterSlaveRuleConfigurationProperties;
import org.apache.logging.log4j.spring.boot.shardingjdbc.sharding.SpringBootShardingRuleConfigurationProperties;
import org.apache.logging.log4j.spring.boot.shardingjdbc.util.PropertyUtil;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;

import com.google.common.base.Preconditions;

import io.shardingsphere.core.exception.ShardingException;
import io.shardingsphere.shardingjdbc.api.MasterSlaveDataSourceFactory;
import io.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;
import io.shardingsphere.shardingjdbc.jdbc.core.datasource.ShardingDataSource;
import io.shardingsphere.shardingjdbc.util.DataSourceUtil;

/**
 * Log4j基于JDBC存储日志的配置类 http://www.cnblogs.com/bigbang92/p/Log4j2.html
 * 
 * @author ： <a href="https://github.com/vindell">vindell</a>
 */
@Configuration
@ConditionalOnClass({ Logger.class, ShardingDataSource.class })
@ConditionalOnProperty(name = { "logging.log4j.jdbc.enabled" }, havingValue = "true", matchIfMissing = false)
@EnableConfigurationProperties({ Log4jJdbcProperties.class, SpringBootShardingRuleConfigurationProperties.class,
		SpringBootMasterSlaveRuleConfigurationProperties.class, SpringBootConfigMapConfigurationProperties.class,
		SpringBootPropertiesConfigurationProperties.class })
@AutoConfigureAfter({ DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class })
public class Log4jShardingJdbcAutoConfiguration {

	@Configuration
	@ConditionalOnMissingBean(Logger.class)
	@EnableConfigurationProperties(Log4jJdbcProperties.class)
	public static class Log4jJdbcConfiguration implements EnvironmentAware {

		private final Map<String, DataSource> dataSourceMap = new LinkedHashMap<>();

		private final Log4jJdbcProperties jdbcProperties;

		private final SpringBootShardingRuleConfigurationProperties shardingProperties;

		private final SpringBootMasterSlaveRuleConfigurationProperties masterSlaveProperties;

		private final SpringBootConfigMapConfigurationProperties configMapProperties;

		private final SpringBootPropertiesConfigurationProperties propMapProperties;

		private final DataSourceProperties dataSourceProperties;

		private final DataSource dataSource;

		private final DataSource log4jDataSource;

		public Log4jJdbcConfiguration(Log4jJdbcProperties jdbcProperties,
				SpringBootShardingRuleConfigurationProperties shardingProperties,
				SpringBootMasterSlaveRuleConfigurationProperties masterSlaveProperties,
				SpringBootConfigMapConfigurationProperties configMapProperties,
				SpringBootPropertiesConfigurationProperties propMapProperties,
				DataSourceProperties dataSourceProperties, 
				ResourceLoader resourceLoader,
				ObjectProvider<DataSource> dataSource, 
				@Log4jDataSource ObjectProvider<DataSource> log4jDataSource) {
			this.jdbcProperties = jdbcProperties;
			this.shardingProperties = shardingProperties;
			this.masterSlaveProperties = masterSlaveProperties;
			this.configMapProperties = configMapProperties;
			this.propMapProperties = propMapProperties;
			this.dataSourceProperties = dataSourceProperties;
			this.dataSource = dataSource.getIfUnique();
			this.log4jDataSource = log4jDataSource.getIfAvailable();
		}

		@Override
		public final void setEnvironment(final Environment environment) {
			setDataSourceMap(environment);
		}
		
		@SuppressWarnings("unchecked")
		private void setDataSourceMap(final Environment environment) {
			String prefix = "sharding.jdbc.datasource.";
			String dataSources = environment.getProperty(prefix + "names");
			for (String each : dataSources.split(",")) {
				try {
					Map<String, Object> dataSourceProps = PropertyUtil.handle(environment, prefix + each.trim(), Map.class);
					Preconditions.checkState(!dataSourceProps.isEmpty(), "Wrong datasource properties!");
					DataSource dataSource = DataSourceUtil.getDataSource(dataSourceProps.get("type").toString(), dataSourceProps);
					dataSourceMap.put(each, dataSource);
				} catch (final ReflectiveOperationException ex) {
					throw new ShardingException("Can't find datasource type!", ex);
				}
			}
		}

		@Bean
		public Log4jJdbcAppenderTemplate jdbcAppenderTemplate() throws Exception {
			Log4jJdbcAppenderTemplate template = new Log4jJdbcAppenderTemplate();
			if (this.jdbcProperties.isShardingJdbc()) {
				template.setDataSource(this.shardingDataSource());
			} else if (this.log4jDataSource != null) {
				template.setDataSource(this.log4jDataSource);
			} else if (this.dataSource != null) {
				template.setDataSource(this.dataSource);
			} else if (this.dataSourceProperties != null) {
				template.setDataSource(this.dataSourceProperties.initializeDataSourceBuilder().build());
			}
			template.setProperties(jdbcProperties);
			return template;
		}

		/**
		 * Get data source bean.
		 * 
		 * @return data source bean
		 * @throws SQLException
		 *             SQL exception
		 */
		public DataSource shardingDataSource() throws SQLException {
			return null == masterSlaveProperties.getMasterDataSourceName()
					? ShardingDataSourceFactory.createDataSource(dataSourceMap,
							shardingProperties.getShardingRuleConfiguration(), configMapProperties.getConfigMap(),
							propMapProperties.getProps())
					: MasterSlaveDataSourceFactory.createDataSource(dataSourceMap,
							masterSlaveProperties.getMasterSlaveRuleConfiguration(), configMapProperties.getConfigMap(),
							propMapProperties.getProps());
		}

	}

}
