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

import jakarta.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link RemoteAddrUtils}.
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 1.0.0
 */
@DisplayName("RemoteAddrUtils Tests")
class RemoteAddrUtilsTest {

    @Test
    @DisplayName("getRemoteAddr returns X-Real-IP header value when present")
    void testXRealIpHeader() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Cdn-Src-Ip")).thenReturn(null);
        when(request.getHeader("X-Real-IP")).thenReturn("192.168.1.100");
        String addr = RemoteAddrUtils.getRemoteAddr(request);
        assertThat(addr).isEqualTo("192.168.1.100");
    }

    @Test
    @DisplayName("getRemoteAddr returns Cdn-Src-Ip header value when present")
    void testCdnSrcIpHeader() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Cdn-Src-Ip")).thenReturn("10.0.0.1");
        String addr = RemoteAddrUtils.getRemoteAddr(request);
        assertThat(addr).isEqualTo("10.0.0.1");
    }

    @Test
    @DisplayName("getRemoteAddr falls back to getRemoteAddr when no headers set")
    void testFallbackToRemoteAddr() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Cdn-Src-Ip")).thenReturn(null);
        when(request.getHeader("X-Real-IP")).thenReturn(null);
        when(request.getHeader("X-Forwarded-For")).thenReturn(null);
        when(request.getHeader("Proxy-Client-IP")).thenReturn(null);
        when(request.getHeader("WL-Proxy-Client-IP")).thenReturn(null);
        when(request.getHeader("HTTP_CLIENT_IP")).thenReturn(null);
        when(request.getHeader("HTTP_X_FORWARDED_FOR")).thenReturn(null);
        when(request.getRemoteAddr()).thenReturn("127.0.0.1");
        String addr = RemoteAddrUtils.getRemoteAddr(request);
        assertThat(addr).isEqualTo("127.0.0.1");
    }

    @Test
    @DisplayName("getRemoteAddr converts localhost to 127.0.0.1")
    void testLocalhostConversion() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Cdn-Src-Ip")).thenReturn(null);
        when(request.getHeader("X-Real-IP")).thenReturn(null);
        when(request.getHeader("X-Forwarded-For")).thenReturn(null);
        when(request.getHeader("Proxy-Client-IP")).thenReturn(null);
        when(request.getHeader("WL-Proxy-Client-IP")).thenReturn(null);
        when(request.getHeader("HTTP_CLIENT_IP")).thenReturn(null);
        when(request.getHeader("HTTP_X_FORWARDED_FOR")).thenReturn(null);
        when(request.getRemoteAddr()).thenReturn("localhost");
        String addr = RemoteAddrUtils.getRemoteAddr(request);
        assertThat(addr).isEqualTo("127.0.0.1");
    }

    @Test
    @DisplayName("getRemoteAddr skips 'unknown' header values")
    void testSkipsUnknownValues() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Cdn-Src-Ip")).thenReturn("unknown");
        when(request.getHeader("X-Real-IP")).thenReturn("10.0.0.5");
        String addr = RemoteAddrUtils.getRemoteAddr(request);
        assertThat(addr).isEqualTo("10.0.0.5");
    }

    @Test
    @DisplayName("getRemoteAddr returns X-Forwarded-For header value")
    void testXForwardedForHeader() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Cdn-Src-Ip")).thenReturn(null);
        when(request.getHeader("X-Real-IP")).thenReturn(null);
        when(request.getHeader("X-Forwarded-For")).thenReturn("172.16.0.1");
        String addr = RemoteAddrUtils.getRemoteAddr(request);
        assertThat(addr).isEqualTo("172.16.0.1");
    }
}
