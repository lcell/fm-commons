package com.github.saleson.fm.http.domain;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.LinkedMultiValueMap;

import java.util.*;

/**
 * @author saleson
 * @date 2020-02-04 22:17
 */
public class HttpHeaders {

    private Map<String, List<String>> headers = new LinkedMultiValueMap<>();


    public HttpHeaders addHeader(String name, String value) {
        List<String> values = headers.computeIfAbsent(name, (k) -> new LinkedList());
        values.add(value);
        return this;
    }

    public HttpHeaders setHeader(String name, String... values) {
        headers.put(name, new LinkedList(Arrays.asList(values)));
        return this;
    }


    public Map<String, List<String>> toMap() {
        return Collections.unmodifiableMap(headers);
    }


    public Set<String> getHeaderNames() {
        return headers.keySet();
    }

    public String getHaderValue(String name) {
        List<String> values = headers.get(name);
        if (Objects.isNull(values) || values.isEmpty()) {
            return "";
        }
        return StringUtils.join(values, ";");
    }

    @Override
    public String toString() {
        return headers.toString();
    }
}
