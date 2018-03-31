package org.apache.logging.log4j.spring.boot;

import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.lang.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.db.ColumnMapping;
import org.apache.logging.log4j.core.appender.db.jdbc.ColumnConfig;
import org.apache.logging.log4j.core.appender.db.jdbc.ConnectionSource;
import org.apache.logging.log4j.core.appender.db.jdbc.JdbcAppender;
import org.apache.logging.log4j.core.filter.MarkerFilter;
import org.apache.logging.log4j.spring.boot.appender.db.jdbc.JDBCConnectionSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

@Configuration
@ConditionalOnClass({ Logger.class })
@ConditionalOnProperty(name = { "logging.log4j.jdbc.enabled" }, havingValue = "true", matchIfMissing = false)
@EnableConfigurationProperties({ Log4j2Properties.class })
public class Log4j2AutoConfiguration {

	@Bean
	public ConnectionSource getConnection(DataSource dataSource) throws SQLException {
		return new JDBCConnectionSource(dataSource);
	}

	@Bean
	public JdbcAppender jdbcAppender(ConnectionSource connectionSource, Log4j2Properties properties) {

		final LoggerContext ctx = (LoggerContext) LogManager.getContext(properties.isCurrentContext());
		final org.apache.logging.log4j.core.config.Configuration config = ctx.getConfiguration();
		final Logger interLogger = ctx.getLogger(properties.getLogger()); // 需要写日志到数据库的包名

		List<Log4j2ColumnMapping> columnMappingList = properties.getColumnMappings();
		ColumnMapping[] columnMappings = {};
		ColumnConfig[] columnConfigs = null;
		if (CollectionUtils.isEmpty(columnMappingList)) {
			// http://www.cnblogs.com/bigbang92/p/Log4j2.html
			columnConfigs = new ColumnConfig[] {

					ColumnConfig.newBuilder().setConfiguration(config).setName("LOG_LOGGER").setPattern("%logger")
							.setLiteral(null).setEventTimestamp(false).setUnicode(true).setClob(false).build(),
					ColumnConfig.newBuilder().setConfiguration(config).setName("LOG_THREAD").setPattern("%thread")
							.setLiteral(null).setEventTimestamp(false).setUnicode(true).setClob(false).build(),
					/*
					 * 异步日志记录时无法取到该数据
					 */
					ColumnConfig.newBuilder().setConfiguration(config).setName("LOG_CLASS").setPattern("%class")
							.setLiteral(null).setEventTimestamp(false).setUnicode(true).setClob(false).build(),
					ColumnConfig.newBuilder().setConfiguration(config).setName("LOG_FUNCTION").setPattern("%M")
							.setLiteral(null).setEventTimestamp(false).setUnicode(true).setClob(false).build(),
					ColumnConfig.newBuilder().setConfiguration(config).setName("LOG_LINE").setPattern("%line")
							.setLiteral(null).setEventTimestamp(false).setUnicode(true).setClob(false).build(),

					ColumnConfig.newBuilder().setConfiguration(config).setName("LOG_LEVEL").setPattern("%level")
							.setLiteral(null).setEventTimestamp(false).setUnicode(true).setClob(false).build(),
					ColumnConfig.newBuilder().setConfiguration(config).setName("LOG_MESSAGE").setPattern("%message")// %message
							.setLiteral(null).setEventTimestamp(false).setUnicode(true).setClob(false).build(),
					ColumnConfig.newBuilder().setConfiguration(config).setName("LOG_EXCEPTION").setPattern("%ex{full}")
							.setLiteral(null).setEventTimestamp(false).setUnicode(true).setClob(true).build(),
					ColumnConfig.newBuilder().setConfiguration(config).setName("LOG_TIMESTAMP")
							.setPattern("%d{yyyy-MM-dd HH:mm:ss.SSS}").setLiteral(null).setEventTimestamp(false)
							.setUnicode(true).setClob(false).build() };

			if (properties.isAsync()) {
				ArrayUtils.remove(columnConfigs, 2);
				ArrayUtils.remove(columnConfigs, 3);
				ArrayUtils.remove(columnConfigs, 4);
			}

		} else {

			columnConfigs = new ColumnConfig[columnMappingList.size()];
			for (int i = 0; i < columnMappingList.size(); i++) {
				Log4j2ColumnMapping column = columnMappingList.get(i);
				columnConfigs[i] = ColumnConfig.newBuilder().setConfiguration(config).setName(column.getColumn())
						.setPattern(column.getPattern()).setLiteral(column.getLiteralValue())
						.setEventTimestamp(column.isEventTimestamp()).setUnicode(column.isUnicode())
						.setClob(column.isClob()).build();

			}

		}

		// 配置Marker过滤器(标记过滤器)
		MarkerFilter filter = MarkerFilter.createFilter(properties.getFilter(), Filter.Result.ACCEPT,
				Filter.Result.DENY);

		// Appender appender = JdbcAppender.createAppender("databaseAppender", "true",
		// filter, connectionSource, "0", "logs", columnConfigs);

		JdbcAppender appender = JdbcAppender.newBuilder().withName(properties.getAppender()).setConfiguration(config)
				.setConnectionSource(connectionSource).setTableName(properties.getTableName())
				.setColumnMappings(columnMappings).setColumnConfigs(columnConfigs)
				.setBufferSize(properties.getBufferSize()).withIgnoreExceptions(properties.isIgnoreExceptions())
				.withFilter(filter).build();

		config.addAppender(appender);
		interLogger.addAppender(appender);
		appender.start();
		ctx.updateLoggers();
		return appender;
	}

}
