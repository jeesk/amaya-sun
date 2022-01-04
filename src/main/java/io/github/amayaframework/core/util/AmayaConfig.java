package io.github.amayaframework.core.util;

import io.github.amayaframework.core.routers.RegexpRouter;
import io.github.amayaframework.core.routers.Router;
import io.github.amayaframework.core.wrapping.InjectPacker;
import io.github.amayaframework.core.wrapping.Packer;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class AmayaConfig {
    public static final AmayaConfig INSTANCE = new AmayaConfig();

    private final Map<Field, Object> fields;

    public AmayaConfig() {
        fields = new ConcurrentHashMap<>();
        setRouteWrapper(new InjectPacker());
        setRouter(RegexpRouter.class);
        setCharset(StandardCharsets.UTF_8);
        setBacklog(0);
    }

    public void setField(Field field, Object value) {
        Objects.requireNonNull(field);
        fields.put(field, value);
    }

    public Object getField(Field field) {
        Objects.requireNonNull(field);
        return fields.get(field);
    }

    public Packer getRouteWrapper() {
        return (Packer) fields.get(Field.ROUTE_WRAPPER);
    }

    public void setRouteWrapper(Packer packer) {
        Objects.requireNonNull(packer);
        fields.put(Field.ROUTE_WRAPPER, packer);
    }

    public Router getRouter() {
        try {
            Class<?> routerClass = (Class<?>) fields.get(Field.ROUTER);
            return (Router) routerClass.newInstance();
        } catch (Exception e) {
            throw new IllegalStateException("Can not instantiate Router!", e);
        }
    }

    public void setRouter(Class<? extends Router> routerClass) {
        Objects.requireNonNull(routerClass);
        this.setField(Field.ROUTER, routerClass);
    }

    public Charset getCharset() {
        return (Charset) fields.get(Field.CHARSET);
    }

    public void setCharset(Charset charset) {
        Objects.requireNonNull(charset);
        this.setField(Field.CHARSET, charset);
    }

    public Integer getBacklog() {
        return (Integer) fields.get(Field.BACKLOG);
    }

    public void setBacklog(int backlog) {
        this.setField(Field.BACKLOG, backlog);
    }

    public enum Field {
        ROUTE_WRAPPER,
        ROUTER,
        CHARSET,
        BACKLOG
    }
}
