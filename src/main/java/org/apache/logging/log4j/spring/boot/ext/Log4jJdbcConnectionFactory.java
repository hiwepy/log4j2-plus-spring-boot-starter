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

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

public class Log4jJdbcConnectionFactory {

	/**
	 * <p>Singleton.</p>
	 *
	 * @author <a href="https://github.com/loong10k">Loong Wan</a>
	 * @since 1.0.0
	 */
	private static interface Singleton {
		final Log4jJdbcConnectionFactory INSTANCE = new Log4jJdbcConnectionFactory();
	}

	private DataSource dataSource;

	private Log4jJdbcConnectionFactory() {
	}
	/** Sets the data source. */

	public static void setDataSource(DataSource dataSource) throws SQLException {
		Singleton.INSTANCE.dataSource = dataSource;
	}
	/** Gets the database connection. */

	public static Connection getDatabaseConnection() throws SQLException {
		return Singleton.INSTANCE.dataSource.getConnection();
	}
	
}
