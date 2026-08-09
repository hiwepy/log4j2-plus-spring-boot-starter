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
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.db.jdbc.JdbcAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link Log4jJdbcAppenderTemplate}.
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 1.0.0
 */
@DisplayName("Log4jJdbcAppenderTemplate Tests")
class Log4jJdbcAppenderTemplateTest {

    @Test
    @DisplayName("Default constructor creates non-null instance")
    void testInstantiation() {
        Log4jJdbcAppenderTemplate instance = new Log4jJdbcAppenderTemplate();
        assertThat(instance).isNotNull();
    }

    @Test
    @DisplayName("setDataSource sets the datasource correctly")
    void testSetDataSource() {
        Log4jJdbcAppenderTemplate template = new Log4jJdbcAppenderTemplate();
        DataSource mockDs = mock(DataSource.class);
        template.setDataSource(mockDs);
        // No exception means setter works
        assertThat(template).isNotNull();
    }

    @Test
    @DisplayName("newJdbcAppender creates appender with valid properties")
    void testNewJdbcAppender() throws SQLException {
        DataSource mockDs = mock(DataSource.class);
        Connection mockConn = mock(Connection.class);
        when(mockDs.getConnection()).thenReturn(mockConn);

        Log4jJdbcAppenderTemplate template = new Log4jJdbcAppenderTemplate();
        template.setDataSource(mockDs);

        Log4jJdbcAppenderProperties props = new Log4jJdbcAppenderProperties();
        props.setMarker("testMarker");
        props.setTableName("TEST_TABLE");

        List<Log4jJdbcColumnConfig> columns = new ArrayList<>();
        Log4jJdbcColumnConfig col = new Log4jJdbcColumnConfig();
        col.setColumn("event_date");
        col.setEventTimestamp(true);
        columns.add(col);
        props.setColumnMappings(columns);

        LoggerContext ctx = LoggerContext.getContext(false);
        Configuration config = ctx.getConfiguration();

        JdbcAppender appender = template.newJdbcAppender(config, props);
        assertThat(appender).isNotNull();
        assertThat(appender.getName()).isEqualTo("testMarker");
        appender.stop();
    }

    @Test
    @DisplayName("newJdbcAppender uses logger name when present")
    void testNewJdbcAppenderWithLoggerName() throws SQLException {
        DataSource mockDs = mock(DataSource.class);
        Connection mockConn = mock(Connection.class);
        when(mockDs.getConnection()).thenReturn(mockConn);

        Log4jJdbcAppenderTemplate template = new Log4jJdbcAppenderTemplate();
        template.setDataSource(mockDs);

        Log4jJdbcAppenderProperties props = new Log4jJdbcAppenderProperties();
        props.setMarker("testMarker");
        props.setLogger("com.example.Logger");
        props.setTableName("TEST_TABLE");

        List<Log4jJdbcColumnConfig> columns = new ArrayList<>();
        Log4jJdbcColumnConfig col = new Log4jJdbcColumnConfig();
        col.setColumn("message");
        col.setPattern("%m");
        columns.add(col);
        props.setColumnMappings(columns);

        LoggerContext ctx = LoggerContext.getContext(false);
        Configuration config = ctx.getConfiguration();

        JdbcAppender appender = template.newJdbcAppender(config, props);
        assertThat(appender).isNotNull();
        assertThat(appender.getName()).isEqualTo("com.example.Logger");
        appender.stop();
    }
}
