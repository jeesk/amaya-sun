package io.github.amayaframework.core;

import io.github.amayaframework.core.configurators.Configurator;
import io.github.amayaframework.core.controllers.Controller;
import io.github.amayaframework.core.controllers.Endpoint;
import io.github.amayaframework.core.scanners.ControllerScanner;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractBuilder {
    protected final Configurator defaultConfigurator;
    protected final Map<String, Controller> controllers;
    protected final List<Configurator> configurators;
    protected Class<? extends Annotation> annotation;

    public AbstractBuilder(Configurator defaultConfigurator) {
        controllers = new ConcurrentHashMap<>();
        configurators = new LinkedList<>();
        this.defaultConfigurator = defaultConfigurator;
        resetValues();
    }

    protected void resetValues() {
        annotation = Endpoint.class;
        configurators.clear();
        configurators.add(defaultConfigurator);
        controllers.clear();
    }

    public AbstractBuilder pipelineConfigurators(Collection<Configurator> configurators) {
        Objects.requireNonNull(configurators);
        configurators.forEach(Objects::requireNonNull);
        this.configurators.clear();
        this.configurators.add(defaultConfigurator);
        this.configurators.addAll(configurators);
        return this;
    }

    public AbstractBuilder addConfigurator(Configurator configurator) {
        Objects.requireNonNull(configurator);
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
