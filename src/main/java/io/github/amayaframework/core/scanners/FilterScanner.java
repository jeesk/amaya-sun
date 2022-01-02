package io.github.amayaframework.core.scanners;

import io.github.amayaframework.core.util.ReflectUtils;
import io.github.amayaframework.filters.Filter;
import io.github.amayaframework.filters.NamedFilter;

import java.util.Map;
import java.util.Objects;

public class FilterScanner<T extends Filter> implements Scanner<Map<String, T>> {
    private final Class<T> clazz;

    public FilterScanner(Class<T> clazz) {
        this.clazz = Objects.requireNonNull(clazz);
    }

    @Override
    public Map<String, T> find() throws Exception {
        return ReflectUtils.foundAnnotatedWithValue(NamedFilter.class, clazz, String.class);
    }
}
