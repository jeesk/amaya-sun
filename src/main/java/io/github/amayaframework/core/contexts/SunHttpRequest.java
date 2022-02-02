package io.github.amayaframework.core.contexts;

import com.github.romanqed.jutils.http.HeaderMap;

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
