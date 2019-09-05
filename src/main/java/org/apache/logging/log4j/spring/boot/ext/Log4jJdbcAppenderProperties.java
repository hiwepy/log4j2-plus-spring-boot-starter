/*
 * Copyright (c) 2017, vindell (https://github.com/vindell).
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
/**
 * 
 */
package org.apache.logging.log4j.spring.boot.ext;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 * 
 * @author <a href="https://github.com/vindell">vindell</a>
 */
public class Log4jJdbcAppenderProperties {

	/** The Marker name to match. **/
	protected String marker = "dblog";
	/** The name of the Logger **/
	protected String logger = "";
	/** 是否异步记录日志 **/
	protected boolean async = false;
	/** 是否忽略异常信息 **/
	protected boolean ignoreExceptions = true;
	/** 日志表名称 **/
	protected String tableName = "LOG4j_BIZ";
	/** 数据库列与表达式对应关系 **/
	private List<Log4jJdbcColumnConfig> columnMappings = new ArrayList<Log4jJdbcColumnConfig>();
	
	/** 日志批量缓冲大小；小于0，则批量提交无效 **/
	protected int bufferSize = -1;

	public String getLogger() {
		return logger;
	}

	public void setLogger(String logger) {
		this.logger = logger;
	}

	public boolean isAsync() {
		return async;
	}

	public void setAsync(boolean async) {
		this.async = async;
	}

	public boolean isIgnoreExceptions() {
		return ignoreExceptions;
	}

	public void setIgnoreExceptions(boolean ignoreExceptions) {
		this.ignoreExceptions = ignoreExceptions;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public List<Log4jJdbcColumnConfig> getColumnMappings() {
		return columnMappings;
	}

	public void setColumnMappings(List<Log4jJdbcColumnConfig> columnMappings) {
		this.columnMappings = columnMappings;
	}

	public String getMarker() {
		return marker;
	}

	public void setMarker(String marker) {
		this.marker = marker;
	}

	public int getBufferSize() {
		return bufferSize;
	}

	public void setBufferSize(int bufferSize) {
		this.bufferSize = bufferSize;
	}

}
