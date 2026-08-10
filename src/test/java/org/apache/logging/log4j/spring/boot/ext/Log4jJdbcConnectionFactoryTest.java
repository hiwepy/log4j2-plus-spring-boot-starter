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

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link Log4jJdbcConnectionFactory}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@DisplayName("Log4jJdbcConnectionFactory Tests")
class Log4jJdbcConnectionFactoryTest {

    @Test
    @DisplayName("setDataSource and getDatabaseConnection work correctly")
    void testSetAndGetConnection() throws SQLException {
        DataSource mockDs = mock(DataSource.class);
        Connection mockConn = mock(Connection.class);
        when(mockDs.getConnection()).thenReturn(mockConn);

        Log4jJdbcConnectionFactory.setDataSource(mockDs);
        Connection result = Log4jJdbcConnectionFactory.getDatabaseConnection();
        assertThat(result).isEqualTo(mockConn);
    }

    @Test
    @DisplayName("getDatabaseConnection throws when no datasource set")
    void testGetConnectionWithoutDataSource() {
        // After setting null datasource, getConnection should throw
        assertThatThrownBy(() -> {
            Log4jJdbcConnectionFactory.setDataSource(null);
            Log4jJdbcConnectionFactory.getDatabaseConnection();
        }).isInstanceOf(NullPointerException.class);
    }
}
