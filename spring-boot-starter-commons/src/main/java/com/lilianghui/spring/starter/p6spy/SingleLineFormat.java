/*
 * #%L
 * P6Spy
 * %%
 * Copyright (C) 2013 P6Spy
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package com.lilianghui.spring.starter.p6spy;

import com.lilianghui.spring.starter.P6spyAutoConfiguration;
import com.lilianghui.spring.starter.utils.WebUtils;
import com.p6spy.engine.common.P6Util;
import com.p6spy.engine.spy.appender.MessageFormattingStrategy;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class SingleLineFormat implements MessageFormattingStrategy {
    private static final String SEPARATE = "|";

    private static final Map<String, Field> STRING_METHOD_MAP = new ConcurrentHashMap<>();

    @Override
    public String formatMessage(final int connectionId, final String now, final long elapsed, final String category, final String prepared, String sql) {
        if (StringUtils.isBlank(sql)) {
            return null;
        }
        String date = "";
        try {
            date = DateFormatUtils.format(Long.valueOf(now), "yyyy-MM-dd HH:mm:ss");
        } catch (Exception e) {
            date = now;
        }
        sql = P6Util.singleLine(sql).replaceAll("[\\s]+", " ");
        String ip = "";
        Serializable id = "";
        String name = "";
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String requestURL = "";
        if (attrs != null) {
            HttpServletRequest request = attrs.getRequest();
            if (request != null) {
                requestURL = request.getRequestURI();
                ip = WebUtils.getRemoteHost(request);
                Object user = request.getSession().getAttribute(P6spyAutoConfiguration.P_6_SPY_PROPERTIES.getSessionName());
                if (user != null) {
                    id = getValue(P6spyAutoConfiguration.P_6_SPY_PROPERTIES.getIdField(), user);
                    name = getValue(P6spyAutoConfiguration.P_6_SPY_PROPERTIES.getNameField(), user);
                }
            }
        }
        return date + SEPARATE + ip + SEPARATE + requestURL + SEPARATE + id + SEPARATE + name + SEPARATE + sql;
    }

    private String getValue(String field, Object object) {
        Object value = null;
        try {
            Field f = STRING_METHOD_MAP.get(field);
            if (f == null) {
                f = object.getClass().getDeclaredField(field);
                STRING_METHOD_MAP.put(field, f);
            }
            f.setAccessible(true);
            value = f.get(object);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        return value == null ? "" : value.toString();
    }


}
