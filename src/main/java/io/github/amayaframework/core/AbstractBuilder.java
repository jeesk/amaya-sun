package io.github.amayaframework.core;

import io.github.amayaframework.core.configurators.Configurator;
import io.github.amayaframework.core.controllers.Controller;
import io.github.amayaframework.core.controllers.Endpoint;
import io.github.amayaframework.core.scanners.ControllerScanner;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractBuilder {
    protected final Map<String, Controller> controllers;
    protected Collection<Configurator> configurators;
    protected Class<? extends Annotation> annotation;

    public AbstractBuilder() {
        controllers = new ConcurrentHashMap<>();
        resetValues();
    }

    protected void resetValues() {
        annotation = Endpoint.class;
        configurators = null;
        controllers.clear();
    }

    public AbstractBuilder pipelineConfigurators(Collection<Configurator> configurators) {
        Objects.requireNonNull(configurators);
        configurators.forEach(Objects::requireNonNull);
        this.configurators = configurators;
        return this;
    }

    public AbstractBuilder addConfigurator(Configurator configurator) {
        Objects.requireNonNull(configurator);
        if (configurators == null) {
            configurators = new LinkedList<>();
        }
        configurators.add(configurator);
        return this;
    }

    public AbstractBuilder addController(Controller controller) {
        Objects.requireNonNull(controller);
        String path = controller.getPath();
        Objects.requireNonNull(path);
        controllers.put(path, controller);
        return this;
    }

    public AbstractBuilder removeController(String path) {
        Objects.requireNonNull(path);
        controllers.remove(path);
        return this;
    }

    public AbstractBuilder controllerAnnotation(Class<? extends Annotation> annotation) {
        this.annotation = annotation;
        return this;
    }

    protected void findControllers() {
        if (annotation != null) {
            Set<Controller> controllers;
            try {
                controllers = new ControllerScanner(annotation).find();
            } catch (Exception e) {
                throw new IllegalStateException("Exception when scanning controllers!", e);
            }
            controllers.forEach(this::addController);
        }
    }
}
