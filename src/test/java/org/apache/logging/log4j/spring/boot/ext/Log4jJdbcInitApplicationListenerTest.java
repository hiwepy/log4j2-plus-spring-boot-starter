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

import org.apache.logging.log4j.spring.boot.Log4jJdbcProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ConfigurableApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link Log4jJdbcInitApplicationListener}.
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 1.0.0
 */
@DisplayName("Log4jJdbcInitApplicationListener Tests")
class Log4jJdbcInitApplicationListenerTest {

    @Test
    @DisplayName("Instance can be created and setters/getters work")
    void testInstantiation() {
        Log4jJdbcInitApplicationListener listener = new Log4jJdbcInitApplicationListener();
        assertThat(listener).isNotNull();
        assertThat(listener.getJdbcAppenderTemplate()).isNull();
        assertThat(listener.getJdbcProperties()).isNull();
    }

    @Test
    @DisplayName("setJdbcAppenderTemplate and getJdbcAppenderTemplate work correctly")
    void testTemplateGetterSetter() {
        Log4jJdbcInitApplicationListener listener = new Log4jJdbcInitApplicationListener();
        Log4jJdbcAppenderTemplate template = new Log4jJdbcAppenderTemplate();
        listener.setJdbcAppenderTemplate(template);
        assertThat(listener.getJdbcAppenderTemplate()).isEqualTo(template);
    }

    @Test
    @DisplayName("setProperties and getJdbcProperties work correctly")
    void testPropertiesGetterSetter() {
        Log4jJdbcInitApplicationListener listener = new Log4jJdbcInitApplicationListener();
        Log4jJdbcProperties props = new Log4jJdbcProperties();
        listener.setProperties(props);
        assertThat(listener.getJdbcProperties()).isEqualTo(props);
    }

    @Test
    @DisplayName("onApplicationEvent registers appenders when properties have column mappings")
    void testOnApplicationEvent() throws SQLException {
        DataSource mockDs = mock(DataSource.class);
        Connection mockConn = mock(Connection.class);
        when(mockDs.getConnection()).thenReturn(mockConn);

        Log4jJdbcAppenderTemplate template = new Log4jJdbcAppenderTemplate();
        template.setDataSource(mockDs);

        Log4jJdbcProperties props = new Log4jJdbcProperties();
        List<Log4jJdbcAppenderProperties> appenders = new ArrayList<>();
        Log4jJdbcAppenderProperties appenderProps = new Log4jJdbcAppenderProperties();
        appenderProps.setMarker("testMarker");
        appenderProps.setTableName("TEST_TABLE");
        List<Log4jJdbcColumnConfig> columns = new ArrayList<>();
        Log4jJdbcColumnConfig col = new Log4jJdbcColumnConfig();
        col.setColumn("event_date");
        col.setEventTimestamp(true);
        columns.add(col);
        appenderProps.setColumnMappings(columns);
        appenders.add(appenderProps);
        props.setAppenders(appenders);

        Log4jJdbcInitApplicationListener listener = new Log4jJdbcInitApplicationListener();
        listener.setJdbcAppenderTemplate(template);
        listener.setProperties(props);

        ApplicationReadyEvent event = new ApplicationReadyEvent(
                mock(SpringApplication.class),
                null,
                mock(ConfigurableApplicationContext.class),
                null);

        listener.onApplicationEvent(event);

        assertThat(listener.getJdbcAppenderTemplate()).isNotNull();
        assertThat(listener.getJdbcProperties()).isNotNull();
        assertThat(listener.getJdbcProperties().getAppenders()).hasSize(1);
    }

    @Test
    @DisplayName("onApplicationEvent skips appenders with empty column mappings")
    void testOnApplicationEventSkipsEmptyColumns() throws SQLException {
        DataSource mockDs = mock(DataSource.class);
        Connection mockConn = mock(Connection.class);
        when(mockDs.getConnection()).thenReturn(mockConn);

        Log4jJdbcAppenderTemplate template = new Log4jJdbcAppenderTemplate();
        template.setDataSource(mockDs);

        Log4jJdbcProperties props = new Log4jJdbcProperties();
        List<Log4jJdbcAppenderProperties> appenders = new ArrayList<>();
        Log4jJdbcAppenderProperties appenderProps = new Log4jJdbcAppenderProperties();
        appenderProps.setMarker("testMarker");
        appenderProps.setColumnMappings(new ArrayList<>());
        appenders.add(appenderProps);
        props.setAppenders(appenders);

        Log4jJdbcInitApplicationListener listener = new Log4jJdbcInitApplicationListener();
        listener.setJdbcAppenderTemplate(template);
        listener.setProperties(props);

        ApplicationReadyEvent event = new ApplicationReadyEvent(
                mock(SpringApplication.class),
                null,
                mock(ConfigurableApplicationContext.class),
                null);

        // Should skip the appender with empty columns
        listener.onApplicationEvent(event);
        assertThat(listener.getJdbcProperties().getAppenders()).hasSize(1);
    }

    @Test
    @DisplayName("onApplicationEvent uses logger name when present")
    void testOnApplicationEventWithLoggerName() throws SQLException {
        DataSource mockDs = mock(DataSource.class);
        Connection mockConn = mock(Connection.class);
        when(mockDs.getConnection()).thenReturn(mockConn);

        Log4jJdbcAppenderTemplate template = new Log4jJdbcAppenderTemplate();
        template.setDataSource(mockDs);

        Log4jJdbcProperties props = new Log4jJdbcProperties();
        List<Log4jJdbcAppenderProperties> appenders = new ArrayList<>();
        Log4jJdbcAppenderProperties appenderProps = new Log4jJdbcAppenderProperties();
        appenderProps.setMarker("testMarker");
        appenderProps.setLogger("com.example.TestLogger");
        appenderProps.setTableName("TEST_TABLE");
        List<Log4jJdbcColumnConfig> columns = new ArrayList<>();
        Log4jJdbcColumnConfig col = new Log4jJdbcColumnConfig();
        col.setColumn("message");
        col.setPattern("%m");
        columns.add(col);
        appenderProps.setColumnMappings(columns);
        appenders.add(appenderProps);
        props.setAppenders(appenders);

        Log4jJdbcInitApplicationListener listener = new Log4jJdbcInitApplicationListener();
        listener.setJdbcAppenderTemplate(template);
        listener.setProperties(props);

        ApplicationReadyEvent event = new ApplicationReadyEvent(
                mock(SpringApplication.class),
                null,
                mock(ConfigurableApplicationContext.class),
                null);

        listener.onApplicationEvent(event);
        assertThat(listener.getJdbcProperties().getAppenders().get(0).getLogger()).isEqualTo("com.example.TestLogger");
    }

    @Test
    @DisplayName("onApplicationEvent throws when appenders list is empty")
    void testOnApplicationEventWithEmptyAppenders() throws SQLException {
        DataSource mockDs = mock(DataSource.class);
        Connection mockConn = mock(Connection.class);
        when(mockDs.getConnection()).thenReturn(mockConn);

        Log4jJdbcAppenderTemplate template = new Log4jJdbcAppenderTemplate();
        template.setDataSource(mockDs);

        Log4jJdbcProperties props = new Log4jJdbcProperties();
        props.setAppenders(new ArrayList<>());

        Log4jJdbcInitApplicationListener listener = new Log4jJdbcInitApplicationListener();
        listener.setJdbcAppenderTemplate(template);
        listener.setProperties(props);

        ApplicationReadyEvent event = new ApplicationReadyEvent(
                mock(SpringApplication.class),
                null,
                mock(ConfigurableApplicationContext.class),
                null);

        assertThatThrownBy(() -> listener.onApplicationEvent(event))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("onApplicationEvent handles multiple appenders")
    void testOnApplicationEventMultipleAppenders() throws SQLException {
        DataSource mockDs = mock(DataSource.class);
        Connection mockConn = mock(Connection.class);
        when(mockDs.getConnection()).thenReturn(mockConn);

        Log4jJdbcAppenderTemplate template = new Log4jJdbcAppenderTemplate();
        template.setDataSource(mockDs);

        Log4jJdbcProperties props = new Log4jJdbcProperties();
        List<Log4jJdbcAppenderProperties> appenders = new ArrayList<>();

        // First appender
        Log4jJdbcAppenderProperties appender1 = new Log4jJdbcAppenderProperties();
        appender1.setMarker("marker1");
        appender1.setTableName("TABLE1");
        List<Log4jJdbcColumnConfig> cols1 = new ArrayList<>();
        Log4jJdbcColumnConfig col1 = new Log4jJdbcColumnConfig();
        col1.setColumn("col1");
        col1.setEventTimestamp(true);
        cols1.add(col1);
        appender1.setColumnMappings(cols1);
        appenders.add(appender1);

        // Second appender with logger name
        Log4jJdbcAppenderProperties appender2 = new Log4jJdbcAppenderProperties();
        appender2.setMarker("marker2");
        appender2.setLogger("com.example.Logger2");
        appender2.setTableName("TABLE2");
        List<Log4jJdbcColumnConfig> cols2 = new ArrayList<>();
        Log4jJdbcColumnConfig col2 = new Log4jJdbcColumnConfig();
        col2.setColumn("col2");
        col2.setPattern("%m");
        cols2.add(col2);
        appender2.setColumnMappings(cols2);
        appenders.add(appender2);

        // Third appender - empty columns (should be skipped)
        Log4jJdbcAppenderProperties appender3 = new Log4jJdbcAppenderProperties();
        appender3.setMarker("marker3");
        appender3.setColumnMappings(new ArrayList<>());
        appenders.add(appender3);

        props.setAppenders(appenders);

        Log4jJdbcInitApplicationListener listener = new Log4jJdbcInitApplicationListener();
        listener.setJdbcAppenderTemplate(template);
        listener.setProperties(props);

        ApplicationReadyEvent event = new ApplicationReadyEvent(
                mock(SpringApplication.class),
                null,
                mock(ConfigurableApplicationContext.class),
                null);

        listener.onApplicationEvent(event);
        assertThat(listener.getJdbcProperties().getAppenders()).hasSize(3);
    }
}
