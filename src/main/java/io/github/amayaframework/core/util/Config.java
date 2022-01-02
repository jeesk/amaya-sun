package io.github.amayaframework.core.util;

import io.github.amayaframework.core.routers.RegexpRouter;
import io.github.amayaframework.core.routers.Router;
import io.github.amayaframework.core.wrapping.InjectPacker;
import io.github.amayaframework.core.wrapping.Packer;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class Config {
    public static final Config INSTANCE = new Config();

    private final Map<Field, Object> fields;

    public Config() {
        fields = new ConcurrentHashMap<>();
        setRouteWrapper(new InjectPacker());
        setRouter(RegexpRouter.class);
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

    public enum Field {
        ROUTE_WRAPPER,
        ROUTER
    }
}
