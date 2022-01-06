package io.github.amayaframework.core.contexts;

import io.github.amayaframework.server.utils.HeaderMap;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractHttpTransaction implements HttpTransaction {
    private final Map<String, Object> attachments;
    protected HeaderMap headers;
    protected Object body;

    protected AbstractHttpTransaction() {
        attachments = new ConcurrentHashMap<>();
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
}
