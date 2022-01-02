package io.github.amayaframework.core.wrapping;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractViewable implements Viewable {
    private final Map<String, Object> fields;

    public AbstractViewable() {
        fields = new ConcurrentHashMap<>();
    }

    protected void put(String name, Object value) {
        this.fields.put(name, value);
    }

    @Override
    public Object get(String name) {
        return fields.get(name);
    }
}
