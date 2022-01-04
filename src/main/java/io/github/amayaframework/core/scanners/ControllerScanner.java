package io.github.amayaframework.core.scanners;

import io.github.amayaframework.core.controllers.Controller;
import io.github.amayaframework.core.routers.InvalidRouteFormatException;
import io.github.amayaframework.core.util.ParseUtil;
import io.github.amayaframework.core.util.ReflectUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class ControllerScanner implements Scanner<Set<Controller>> {
    private final Class<? extends Annotation> annotationClass;

    public ControllerScanner(Class<? extends Annotation> annotationClass) {
        this.annotationClass = Objects.requireNonNull(annotationClass);
    }

    @Override
    public Set<Controller> find()
            throws InvocationTargetException, InstantiationException, IllegalAccessException {
        Map<String, Controller> found =
                ReflectUtils.foundAnnotatedWithValue(annotationClass, Controller.class, String.class);
        for (String path : found.keySet()) {
            if (path.equals("/")) {
                path = "";
            }
            if (!path.isEmpty() && !ParseUtil.ROUTE.matcher(path).matches()) {
                throw new InvalidRouteFormatException(path);
            }
        }
        found.forEach((path, controller) -> controller.setPath(path));
        return new HashSet<>(found.values());
    }
}
