package io.github.amayaframework.core.sun.contexts;

import io.github.amayaframework.core.contexts.AbstractHttpRequest;
import io.github.amayaframework.http.HeaderMap;

import java.util.List;
import java.util.Objects;

public class SunHttpRequest extends AbstractHttpRequest {
    protected HeaderMap headers;

    @Override
    public List<String> getHeaders(String key) {
        return headers.get(key);
    }

    public void setHeaders(HeaderMap headers) {
        this.headers = Objects.requireNonNull(headers);
    }
}
