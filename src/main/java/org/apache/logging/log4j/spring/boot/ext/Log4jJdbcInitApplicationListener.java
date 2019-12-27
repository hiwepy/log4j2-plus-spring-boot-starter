/*
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.apache.logging.log4j.spring.boot.ext;

import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.db.jdbc.JdbcAppender;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.spring.boot.Log4jJdbcProperties;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

public class Log4jJdbcInitApplicationListener implements ApplicationListener<ApplicationReadyEvent> {

	private Log4jJdbcAppenderTemplate jdbcAppenderTemplate;
	private Log4jJdbcProperties jdbcProperties;
	
	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		
		List<Log4jJdbcAppenderProperties> jdbcAppenders = jdbcProperties.getAppenders();
		Assert.notEmpty(jdbcAppenders, "Need to specify at least one JdbcAppender Properties.");
		
		final LoggerContext context = (LoggerContext) LogManager.getContext(jdbcProperties.isCurrentContext());
		final org.apache.logging.log4j.core.config.Configuration configuration = context.getConfiguration();
		
		for (Log4jJdbcAppenderProperties properties : jdbcAppenders) {
			
			if (CollectionUtils.isEmpty(properties.getColumnMappings())) {
				continue;
			}
				
			// 创建数据源日志输出 JdbcAppender
			JdbcAppender appender = getJdbcAppenderTemplate().newJdbcAppender(configuration, properties);
			configuration.addAppender(appender);
			
			// 创建日志对象和指定输出
			String loggerName = StringUtils.hasText(properties.getLogger()) ? properties.getLogger() : properties.getMarker();
			Logger logger = context.getLogger(loggerName);
			configuration.addLoggerAppender(logger, appender);
			context.updateLoggers();
			
			if(context.hasLogger(loggerName)) {
				
				LoggerConfig loggerConfig = configuration.getLoggerConfig(loggerName);
				
				logger.addAppender(appender);
				
				configuration.removeLogger(loggerName);
				configuration.addLogger(loggerName, loggerConfig);
				
			}
			
		}
		
		context.updateLoggers();
		
		
	}
	
	public void setJdbcAppenderTemplate(Log4jJdbcAppenderTemplate jdbcAppenderTemplate) {
		this.jdbcAppenderTemplate = jdbcAppenderTemplate;
	}

	public Log4jJdbcAppenderTemplate getJdbcAppenderTemplate() {
		return jdbcAppenderTemplate;
	}

	/**
	 * Sets the properties to use.
	 * @param jdbcProperties The properties to use.
	 */
	public void setProperties(Log4jJdbcProperties jdbcProperties) {
		this.jdbcProperties = jdbcProperties;
	}
	
	public Log4jJdbcProperties getJdbcProperties() {
		return jdbcProperties;
	}
	
	

}
