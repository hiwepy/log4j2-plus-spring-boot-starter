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
package org.apache.logging.log4j.spring.boot.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link Log4jUtils}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@DisplayName("Log4jUtils Tests")
class Log4jUtilsTest {

    @Test
    @DisplayName("instance(String) creates a non-null Log4jUtils")
    void testInstanceWithString() {
        Log4jUtils utils = Log4jUtils.instance("testLogger");
        assertThat(utils).isNotNull();
    }

    @Test
    @DisplayName("instance(Marker) creates a non-null Log4jUtils")
    void testInstanceWithMarker() {
        Marker marker = MarkerFactory.getMarker("testMarker");
        Log4jUtils utils = Log4jUtils.instance(marker);
        assertThat(utils).isNotNull();
    }

    @Test
    @DisplayName("isTraceEnabled returns a boolean")
    void testIsTraceEnabled() {
        Log4jUtils utils = Log4jUtils.instance("traceTestLogger");
        // Just verify it doesn't throw
        boolean result = utils.isTraceEnabled();
        assertThat(result).isInstanceOf(Boolean.class);
    }

    @Test
    @DisplayName("trace methods do not throw")
    void testTraceMethods() {
        Log4jUtils utils = Log4jUtils.instance("traceMethodLogger");
        utils.trace("test message");
        utils.trace("format {}", "arg");
        utils.trace("format {} {}", "arg1", "arg2");
        utils.trace("format {} {} {}", "arg1", "arg2", "arg3");
        utils.trace("msg", new RuntimeException("test"));
    }

    @Test
    @DisplayName("trace with marker methods do not throw")
    void testTraceWithMarker() {
        Log4jUtils utils = Log4jUtils.instance("traceMarkerLogger");
        Marker marker = MarkerFactory.getMarker("traceMarker");
        assertThat(utils.isTraceEnabled(marker)).isInstanceOf(Boolean.class);
        utils.trace(marker, "test message");
        utils.trace(marker, "format {}", "arg");
        utils.trace(marker, "format {} {}", "arg1", "arg2");
        utils.trace(marker, "format {} {} {}", "arg1", "arg2", "arg3");
        utils.trace(marker, "msg", new RuntimeException("test"));
    }

    @Test
    @DisplayName("isDebugEnabled returns a boolean")
    void testIsDebugEnabled() {
        Log4jUtils utils = Log4jUtils.instance("debugTestLogger");
        boolean result = utils.isDebugEnabled();
        assertThat(result).isInstanceOf(Boolean.class);
    }

    @Test
    @DisplayName("debug methods do not throw")
    void testDebugMethods() {
        Log4jUtils utils = Log4jUtils.instance("debugMethodLogger");
        utils.debug("test message");
        utils.debug("format {}", "arg");
        utils.debug("format {} {}", "arg1", "arg2");
        utils.debug("format {} {} {}", "arg1", "arg2", "arg3");
        utils.debug("msg", new RuntimeException("test"));
    }

    @Test
    @DisplayName("debug with marker methods do not throw")
    void testDebugWithMarker() {
        Log4jUtils utils = Log4jUtils.instance("debugMarkerLogger");
        Marker marker = MarkerFactory.getMarker("debugMarker");
        assertThat(utils.isDebugEnabled(marker)).isInstanceOf(Boolean.class);
        utils.debug(marker, "test message");
        utils.debug(marker, "format {}", "arg");
        utils.debug(marker, "format {} {}", "arg1", "arg2");
        utils.debug(marker, "format {} {} {}", "arg1", "arg2", "arg3");
        utils.debug(marker, "msg", new RuntimeException("test"));
    }

    @Test
    @DisplayName("isInfoEnabled returns a boolean")
    void testIsInfoEnabled() {
        Log4jUtils utils = Log4jUtils.instance("infoTestLogger");
        boolean result = utils.isInfoEnabled();
        assertThat(result).isInstanceOf(Boolean.class);
    }

    @Test
    @DisplayName("info methods do not throw")
    void testInfoMethods() {
        Log4jUtils utils = Log4jUtils.instance("infoMethodLogger");
        utils.info("test message");
        utils.info("format {}", "arg");
        utils.info("format {} {}", "arg1", "arg2");
        utils.info("format {} {} {}", "arg1", "arg2", "arg3");
        utils.info("msg", new RuntimeException("test"));
    }

    @Test
    @DisplayName("info with marker methods do not throw")
    void testInfoWithMarker() {
        Log4jUtils utils = Log4jUtils.instance("infoMarkerLogger");
        Marker marker = MarkerFactory.getMarker("infoMarker");
        assertThat(utils.isInfoEnabled(marker)).isInstanceOf(Boolean.class);
        utils.info(marker, "test message");
        utils.info(marker, "format {}", "arg");
        utils.info(marker, "format {} {}", "arg1", "arg2");
        utils.info(marker, "format {} {} {}", "arg1", "arg2", "arg3");
        utils.info(marker, "msg", new RuntimeException("test"));
    }

    @Test
    @DisplayName("isWarnEnabled returns a boolean")
    void testIsWarnEnabled() {
        Log4jUtils utils = Log4jUtils.instance("warnTestLogger");
        boolean result = utils.isWarnEnabled();
        assertThat(result).isInstanceOf(Boolean.class);
    }

    @Test
    @DisplayName("warn methods do not throw")
    void testWarnMethods() {
        Log4jUtils utils = Log4jUtils.instance("warnMethodLogger");
        utils.warn("test message");
        utils.warn("format {}", "arg");
        utils.warn("format {} {}", "arg1", "arg2");
        utils.warn("format {} {} {}", "arg1", "arg2", "arg3");
        utils.warn("msg", new RuntimeException("test"));
    }

    @Test
    @DisplayName("warn with marker methods do not throw")
    void testWarnWithMarker() {
        Log4jUtils utils = Log4jUtils.instance("warnMarkerLogger");
        Marker marker = MarkerFactory.getMarker("warnMarker");
        assertThat(utils.isWarnEnabled(marker)).isInstanceOf(Boolean.class);
        utils.warn(marker, "test message");
        utils.warn(marker, "format {}", "arg");
        utils.warn(marker, "format {} {}", "arg1", "arg2");
        utils.warn(marker, "format {} {} {}", "arg1", "arg2", "arg3");
        utils.warn(marker, "msg", new RuntimeException("test"));
    }

    @Test
    @DisplayName("isErrorEnabled returns a boolean")
    void testIsErrorEnabled() {
        Log4jUtils utils = Log4jUtils.instance("errorTestLogger");
        boolean result = utils.isErrorEnabled();
        assertThat(result).isInstanceOf(Boolean.class);
    }

    @Test
    @DisplayName("error methods do not throw")
    void testErrorMethods() {
        Log4jUtils utils = Log4jUtils.instance("errorMethodLogger");
        utils.error("test message");
        utils.error("format {}", "arg");
        utils.error("format {} {}", "arg1", "arg2");
        utils.error("format {} {} {}", "arg1", "arg2", "arg3");
        utils.error("msg", new RuntimeException("test"));
    }

    @Test
    @DisplayName("error with marker methods do not throw")
    void testErrorWithMarker() {
        Log4jUtils utils = Log4jUtils.instance("errorMarkerLogger");
        Marker marker = MarkerFactory.getMarker("errorMarker");
        assertThat(utils.isErrorEnabled(marker)).isInstanceOf(Boolean.class);
        utils.error(marker, "test message");
        utils.error(marker, "format {}", "arg");
        utils.error(marker, "format {} {}", "arg1", "arg2");
        utils.error(marker, "format {} {} {}", "arg1", "arg2", "arg3");
        utils.error(marker, "msg", new RuntimeException("test"));
    }
}
