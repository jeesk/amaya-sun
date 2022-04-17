package io.github.amayaframework.core.sun.contexts;

import com.github.romanqed.jutils.http.HeaderMap;
import io.github.amayaframework.core.contexts.AbstractHttpRequest;

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
