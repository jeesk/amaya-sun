package io.github.amayaframework.core.contexts;

import java.util.List;
import java.util.Map;

public interface HttpTransaction {
    Object getBody();

    Map<String, List<String>> getHeaders();

    Map<String, Object> getAttachments();

    Object getAttachment(String key);

    void setAttachment(String key, Object value);
}
