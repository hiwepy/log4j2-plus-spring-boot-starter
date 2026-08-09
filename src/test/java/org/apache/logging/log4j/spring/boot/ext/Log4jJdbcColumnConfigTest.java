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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link Log4jJdbcColumnConfig}.
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 1.0.0
 */
@DisplayName("Log4jJdbcColumnConfig Tests")
class Log4jJdbcColumnConfigTest {

    @Test
    @DisplayName("Default constructor creates non-null instance with expected defaults")
    void testDefaultInstance() {
        Log4jJdbcColumnConfig config = new Log4jJdbcColumnConfig();
        assertThat(config).isNotNull();
        assertThat(config.getColumn()).isNull();
        assertThat(config.getPattern()).isNull();
        assertThat(config.getLiteralValue()).isNull();
        assertThat(config.isEventTimestamp()).isFalse();
        assertThat(config.isUnicode()).isTrue();
        assertThat(config.isClob()).isFalse();
    }

    @Test
    @DisplayName("column getter/setter works correctly")
    void testColumn() {
        Log4jJdbcColumnConfig config = new Log4jJdbcColumnConfig();
        config.setColumn("event_date");
        assertThat(config.getColumn()).isEqualTo("event_date");
    }

    @Test
    @DisplayName("pattern getter/setter works correctly")
    void testPattern() {
        Log4jJdbcColumnConfig config = new Log4jJdbcColumnConfig();
        config.setPattern("%d{yyyy-MM-dd}");
        assertThat(config.getPattern()).isEqualTo("%d{yyyy-MM-dd}");
    }

    @Test
    @DisplayName("literalValue getter/setter works correctly")
    void testLiteralValue() {
        Log4jJdbcColumnConfig config = new Log4jJdbcColumnConfig();
        config.setLiteralValue("DEFAULT");
        assertThat(config.getLiteralValue()).isEqualTo("DEFAULT");
    }

    @Test
    @DisplayName("eventTimestamp getter/setter works correctly")
    void testEventTimestamp() {
        Log4jJdbcColumnConfig config = new Log4jJdbcColumnConfig();
        assertThat(config.isEventTimestamp()).isFalse();
        config.setEventTimestamp(true);
        assertThat(config.isEventTimestamp()).isTrue();
    }

    @Test
    @DisplayName("unicode getter/setter works correctly")
    void testUnicode() {
        Log4jJdbcColumnConfig config = new Log4jJdbcColumnConfig();
        assertThat(config.isUnicode()).isTrue();
        config.setUnicode(false);
        assertThat(config.isUnicode()).isFalse();
    }

    @Test
    @DisplayName("clob getter/setter works correctly")
    void testClob() {
        Log4jJdbcColumnConfig config = new Log4jJdbcColumnConfig();
        assertThat(config.isClob()).isFalse();
        config.setClob(true);
        assertThat(config.isClob()).isTrue();
    }

    @Test
    @DisplayName("toString returns expected format")
    void testToString() {
        Log4jJdbcColumnConfig config = new Log4jJdbcColumnConfig();
        config.setColumn("myCol");
        config.setLiteralValue("lit");
        config.setEventTimestamp(true);
        String str = config.toString();
        assertThat(str).contains("myCol");
        assertThat(str).contains("lit");
        assertThat(str).contains("true");
    }
}
