package io.github.amayaframework.core.contexts;

import io.github.amayaframework.server.utils.HeaderMap;
import io.github.amayaframework.server.utils.HttpCode;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class HttpResponse extends AbstractHttpTransaction {
    private HttpCode code;

    public HttpResponse(HttpCode code, HeaderMap headers) {
        this.code = Objects.requireNonNull(code);
        this.headers = Objects.requireNonNull(headers);
    }

    public HttpResponse(HttpCode code) {
        this(code, new HeaderMap());
    }

    protected HttpResponse() {
        this(HttpCode.OK, new HeaderMap());
    }

    public HttpCode getCode() {
        return code;
    }

    public void setCode(HttpCode code) {
        this.code = Objects.requireNonNull(code);
    }

    public void setHeader(String key, List<String> value) {
        headers.put(key, value);
    }

    public void setHeader(String key, String value) {
        setHeader(key, Collections.singletonList(value));
    }

    public List<String> removeHeader(String key) {
        return headers.remove(key);
    }

    public List<String> getHeader(String key) {
        return headers.get(key);
    }
}
