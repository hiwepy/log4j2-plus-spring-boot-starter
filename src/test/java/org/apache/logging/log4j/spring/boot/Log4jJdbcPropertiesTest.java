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
package org.apache.logging.log4j.spring.boot;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.spring.boot.ext.Log4jJdbcAppenderProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link Log4jJdbcProperties}.
 *
 * <p>Verifies default values, getters/setters and POJO contract.</p>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@DisplayName("Log4jJdbcProperties Tests")
class Log4jJdbcPropertiesTest {

    @Test
    @DisplayName("Default constructor creates non-null instance with expected defaults")
    void testDefaultInstance() {
        Log4jJdbcProperties props = new Log4jJdbcProperties();
        assertThat(props).isNotNull();
        assertThat(props.isCurrentContext()).isFalse();
        assertThat(props.isEnabled()).isTrue();
        assertThat(props.isShardingJdbc()).isFalse();
        assertThat(props.getAppenders()).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("Public constant 'PREFIX' has expected value")
    void testPREFIXConstant() {
        assertThat(Log4jJdbcProperties.PREFIX).isEqualTo("logging.log4j.jdbc");
    }

    @Test
    @DisplayName("currentContext getter/setter works correctly")
    void testCurrentContext() {
        Log4jJdbcProperties props = new Log4jJdbcProperties();
        assertThat(props.isCurrentContext()).isFalse();
        props.setCurrentContext(true);
        assertThat(props.isCurrentContext()).isTrue();
        props.setCurrentContext(false);
        assertThat(props.isCurrentContext()).isFalse();
    }

    @Test
    @DisplayName("enabled getter/setter works correctly")
    void testEnabled() {
        Log4jJdbcProperties props = new Log4jJdbcProperties();
        assertThat(props.isEnabled()).isTrue();
        props.setEnabled(false);
        assertThat(props.isEnabled()).isFalse();
        props.setEnabled(true);
        assertThat(props.isEnabled()).isTrue();
    }

    @Test
    @DisplayName("shardingJdbc getter/setter works correctly")
    void testShardingJdbc() {
        Log4jJdbcProperties props = new Log4jJdbcProperties();
        assertThat(props.isShardingJdbc()).isFalse();
        props.setShardingJdbc(true);
        assertThat(props.isShardingJdbc()).isTrue();
    }

    @Test
    @DisplayName("appenders getter/setter works correctly")
    void testAppenders() {
        Log4jJdbcProperties props = new Log4jJdbcProperties();
        List<Log4jJdbcAppenderProperties> appenders = new ArrayList<>();
        appenders.add(new Log4jJdbcAppenderProperties());
        props.setAppenders(appenders);
        assertThat(props.getAppenders()).hasSize(1);
    }
}
