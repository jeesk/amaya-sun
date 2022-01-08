package io.github.amayaframework.core.contexts;

import io.github.amayaframework.server.utils.HeaderMap;

import java.net.HttpCookie;
import java.util.*;

public abstract class AbstractHttpTransaction implements HttpTransaction {
    private final Map<String, Object> attachments;
    protected Map<String, HttpCookie> cookies;
    protected HeaderMap headers;
    protected Object body;

    protected AbstractHttpTransaction() {
        attachments = new HashMap<>();
    }

    @Override
    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    @Override
    public HeaderMap getHeaders() {
        return headers;
    }

    public List<String> getHeader(String key) {
        return headers.get(key);
    }

    @Override
    public Map<String, Object> getAttachments() {
        return attachments;
    }

    @Override
    public Object getAttachment(String key) {
        return attachments.get(key);
    }

    @Override
    public void setAttachment(String key, Object value) {
        attachments.put(key, value);
    }

    @Override
    public Collection<HttpCookie> getCookies() {
        return cookies.values();
    }

    @Override
    public void setCookie(HttpCookie cookie) {
        Objects.requireNonNull(cookie);
        cookies.put(cookie.getName(), cookie);
    }

    @Override
    public HttpCookie getCookie(String name) {
        return cookies.get(name);
    }
}
