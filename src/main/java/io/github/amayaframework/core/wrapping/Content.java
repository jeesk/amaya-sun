package io.github.amayaframework.core.wrapping;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public enum Content {
    QUERY(Query.class, "query"),
    PATH(Path.class, "path"),
    BODY(Body.class, "body"),
    HEADER(Header.class, "header"),
    COOKIE(Cookie.class, "cookie");

    private static final Map<Class<Annotation>, Content> children = toMap();

    private final Class<Annotation> annotationClass;
    private final String filter;

    @SuppressWarnings("unchecked")
    Content(Class<?> annotationClass, String filter) {
        Objects.requireNonNull(annotationClass);
        if (!annotationClass.isAnnotation()) {
            throw new IllegalArgumentException("The provided class is not an annotation");
        }
        this.annotationClass = (Class<Annotation>) annotationClass;
        this.filter = filter;
    }

    private static Map<Class<Annotation>, Content> toMap() {
        Map<Class<Annotation>, Content> ret = new ConcurrentHashMap<>();
        for (Content content : Content.values()) {
            ret.put(content.annotationClass, content);
        }
        return ret;
    }

    public static Content fromAnnotation(Annotation annotation) {
        if (annotation == null) {
            return null;
        }
        return children.get(annotation.annotationType());
    }

    public String getFilter() {
        return filter;
    }
}
