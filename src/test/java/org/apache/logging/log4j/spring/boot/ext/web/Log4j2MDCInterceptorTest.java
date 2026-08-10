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
package org.apache.logging.log4j.spring.boot.ext.web;

import java.util.Collections;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.ThreadContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link Log4j2MDCInterceptor}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@DisplayName("Log4j2MDCInterceptor Tests")
class Log4j2MDCInterceptorTest {

    @AfterEach
    void cleanup() {
        ThreadContext.clearMap();
    }

    @Test
    @DisplayName("preHandle populates ThreadContext with request info")
    void testPreHandle() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        Object handler = new Object();

        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost/test"));
        when(request.getRequestURI()).thenReturn("/test");
        when(request.getQueryString()).thenReturn("key=value");
        when(request.getRemoteAddr()).thenReturn("127.0.0.1");
        when(request.getRemoteHost()).thenReturn("localhost");
        when(request.getRemotePort()).thenReturn(8080);
        when(request.getLocalAddr()).thenReturn("127.0.0.1");
        when(request.getLocalName()).thenReturn("localhost");
        when(request.getHeaderNames()).thenReturn(Collections.enumeration(List.of()));
        when(request.getParameterNames()).thenReturn(Collections.enumeration(List.of()));

        Log4j2MDCInterceptor interceptor = new Log4j2MDCInterceptor();
        boolean result = interceptor.preHandle(request, response, handler);

        assertThat(result).isTrue();
        assertThat(ThreadContext.get("requestURL")).isEqualTo("http://localhost/test");
        assertThat(ThreadContext.get("requestURI")).isEqualTo("/test");
        assertThat(ThreadContext.get("queryString")).isEqualTo("key=value");
        assertThat(ThreadContext.get("remoteAddr")).isEqualTo("127.0.0.1");
        assertThat(ThreadContext.get("remoteHost")).isEqualTo("localhost");
        assertThat(ThreadContext.get("remotePort")).isEqualTo("8080");
        assertThat(ThreadContext.get("localAddr")).isEqualTo("127.0.0.1");
        assertThat(ThreadContext.get("localName")).isEqualTo("localhost");
        assertThat(ThreadContext.get("uuid")).isNotNull();
    }

    @Test
    @DisplayName("preHandle populates headers and params in ThreadContext")
    void testPreHandleWithHeadersAndParams() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        Object handler = new Object();

        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost/test"));
        when(request.getRequestURI()).thenReturn("/test");
        when(request.getQueryString()).thenReturn(null);
        when(request.getRemoteAddr()).thenReturn("127.0.0.1");
        when(request.getRemoteHost()).thenReturn("localhost");
        when(request.getRemotePort()).thenReturn(8080);
        when(request.getLocalAddr()).thenReturn("127.0.0.1");
        when(request.getLocalName()).thenReturn("localhost");
        when(request.getHeaderNames()).thenReturn(Collections.enumeration(List.of("Accept")));
        when(request.getHeader("Accept")).thenReturn("application/json");
        when(request.getParameterNames()).thenReturn(Collections.enumeration(List.of("page")));
        when(request.getParameter("page")).thenReturn("1");

        Log4j2MDCInterceptor interceptor = new Log4j2MDCInterceptor();
        interceptor.preHandle(request, response, handler);

        assertThat(ThreadContext.get("header.Accept")).isEqualTo("application/json");
        assertThat(ThreadContext.get("param.page")).isEqualTo("1");
    }

    @Test
    @DisplayName("postHandle does not throw")
    void testPostHandle() throws Exception {
        Log4j2MDCInterceptor interceptor = new Log4j2MDCInterceptor();
        interceptor.postHandle(mock(HttpServletRequest.class), mock(HttpServletResponse.class), new Object(), null);
    }

    @Test
    @DisplayName("afterCompletion clears ThreadContext")
    void testAfterCompletion() throws Exception {
        ThreadContext.put("key", "value");
        Log4j2MDCInterceptor interceptor = new Log4j2MDCInterceptor();
        interceptor.afterCompletion(mock(HttpServletRequest.class), mock(HttpServletResponse.class), new Object(), null);
        assertThat(ThreadContext.get("key")).isNull();
    }
}
