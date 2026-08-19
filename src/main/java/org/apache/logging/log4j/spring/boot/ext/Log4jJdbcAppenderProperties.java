/*
 * Copyright (c) 2017, hiwepy (https://github.com/hiwepy).
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

import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 * 
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
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
	/** Gets the logger. */

	public String getLogger() {
		return logger;
	}
	/** Sets the logger. */

	public void setLogger(String logger) {
		this.logger = logger;
	}
	/**
	 * <p>Is async.</p>
	 * @return the boolean
	 */

	public boolean isAsync() {
		return async;
	}
	/** Sets the async. */

	public void setAsync(boolean async) {
		this.async = async;
	}
	/**
	 * <p>Is ignore exceptions.</p>
	 * @return the boolean
	 */

	public boolean isIgnoreExceptions() {
		return ignoreExceptions;
	}
	/** Sets the ignore exceptions. */

	public void setIgnoreExceptions(boolean ignoreExceptions) {
		this.ignoreExceptions = ignoreExceptions;
	}
	/** Gets the table name. */

	public String getTableName() {
		return tableName;
	}
	/** Sets the table name. */

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	/** Gets the column mappings. */

	public List<Log4jJdbcColumnConfig> getColumnMappings() {
		return columnMappings;
	}
	/** Sets the column mappings. */

	public void setColumnMappings(List<Log4jJdbcColumnConfig> columnMappings) {
		this.columnMappings = columnMappings;
	}
	/** Gets the marker. */

	public String getMarker() {
		return marker;
	}
	/** Sets the marker. */

	public void setMarker(String marker) {
		this.marker = marker;
	}
	/** Gets the buffer size. */

	public int getBufferSize() {
		return bufferSize;
	}
	/** Sets the buffer size. */

	public void setBufferSize(int bufferSize) {
		this.bufferSize = bufferSize;
	}

}
