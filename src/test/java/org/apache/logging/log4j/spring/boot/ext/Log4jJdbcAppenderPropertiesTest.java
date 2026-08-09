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

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link Log4jJdbcAppenderProperties}.
 *
 * <p>Verifies default values, getters/setters and POJO contract.</p>
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 1.0.0
 */
@DisplayName("Log4jJdbcAppenderProperties Tests")
class Log4jJdbcAppenderPropertiesTest {

    @Test
    @DisplayName("Default constructor creates non-null instance with expected defaults")
    void testDefaultInstance() {
        Log4jJdbcAppenderProperties props = new Log4jJdbcAppenderProperties();
        assertThat(props).isNotNull();
        assertThat(props.getMarker()).isEqualTo("dblog");
        assertThat(props.getLogger()).isEmpty();
        assertThat(props.isAsync()).isFalse();
        assertThat(props.isIgnoreExceptions()).isTrue();
        assertThat(props.getTableName()).isEqualTo("LOG4j_BIZ");
        assertThat(props.getColumnMappings()).isNotNull().isEmpty();
        assertThat(props.getBufferSize()).isEqualTo(-1);
    }

    @Test
    @DisplayName("marker getter/setter works correctly")
    void testMarker() {
        Log4jJdbcAppenderProperties props = new Log4jJdbcAppenderProperties();
        props.setMarker("customMarker");
        assertThat(props.getMarker()).isEqualTo("customMarker");
    }

    @Test
    @DisplayName("logger getter/setter works correctly")
    void testLogger() {
        Log4jJdbcAppenderProperties props = new Log4jJdbcAppenderProperties();
        props.setLogger("com.example.MyLogger");
        assertThat(props.getLogger()).isEqualTo("com.example.MyLogger");
    }

    @Test
    @DisplayName("async getter/setter works correctly")
    void testAsync() {
        Log4jJdbcAppenderProperties props = new Log4jJdbcAppenderProperties();
        assertThat(props.isAsync()).isFalse();
        props.setAsync(true);
        assertThat(props.isAsync()).isTrue();
    }

    @Test
    @DisplayName("ignoreExceptions getter/setter works correctly")
    void testIgnoreExceptions() {
        Log4jJdbcAppenderProperties props = new Log4jJdbcAppenderProperties();
        assertThat(props.isIgnoreExceptions()).isTrue();
        props.setIgnoreExceptions(false);
        assertThat(props.isIgnoreExceptions()).isFalse();
    }

    @Test
    @DisplayName("tableName getter/setter works correctly")
    void testTableName() {
        Log4jJdbcAppenderProperties props = new Log4jJdbcAppenderProperties();
        props.setTableName("MY_TABLE");
        assertThat(props.getTableName()).isEqualTo("MY_TABLE");
    }

    @Test
    @DisplayName("columnMappings getter/setter works correctly")
    void testColumnMappings() {
        Log4jJdbcAppenderProperties props = new Log4jJdbcAppenderProperties();
        List<Log4jJdbcColumnConfig> mappings = new ArrayList<>();
        mappings.add(new Log4jJdbcColumnConfig());
        props.setColumnMappings(mappings);
        assertThat(props.getColumnMappings()).hasSize(1);
    }

    @Test
    @DisplayName("bufferSize getter/setter works correctly")
    void testBufferSize() {
        Log4jJdbcAppenderProperties props = new Log4jJdbcAppenderProperties();
        props.setBufferSize(100);
        assertThat(props.getBufferSize()).isEqualTo(100);
    }
}
