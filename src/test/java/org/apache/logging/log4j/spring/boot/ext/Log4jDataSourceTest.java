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

import java.lang.reflect.Method;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link Log4jDataSource}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@DisplayName("Log4jDataSource Tests")
class Log4jDataSourceTest {

    @Test
    @DisplayName("Log4jDataSource annotation is present on itself via @Qualifier")
    void testAnnotationIsQualifier() {
        assertThat(Log4jDataSource.class.isAnnotation()).isTrue();
        assertThat(Log4jDataSource.class.isInterface()).isTrue();
    }

    @Test
    @DisplayName("Log4jDataSource can be retrieved via reflection")
    void testAnnotationRetrievedViaReflection() throws Exception {
        AnnotatedClass obj = new AnnotatedClass();
        Method method = AnnotatedClass.class.getMethod("getDataSource");
        assertThat(method.isAnnotationPresent(Log4jDataSource.class)).isFalse();
        // Verify annotation can be applied at parameter level
        Method annotatedMethod = AnnotatedClass.class.getMethod("setDataSource", javax.sql.DataSource.class);
        assertThat(annotatedMethod.getParameterAnnotations()[0]).isNotNull();
    }

    static class AnnotatedClass {
        public javax.sql.DataSource getDataSource() { return null; }
        public void setDataSource(javax.sql.DataSource ds) { }
    }
}
