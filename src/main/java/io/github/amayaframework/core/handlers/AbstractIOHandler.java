package io.github.amayaframework.core.handlers;

import com.github.romanqed.jutils.structs.pipeline.Pipeline;
import com.github.romanqed.jutils.structs.pipeline.PipelineResult;
import com.github.romanqed.jutils.util.Checks;
import io.github.amayaframework.core.configurators.Configurator;
import io.github.amayaframework.core.controllers.Controller;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

public abstract class AbstractIOHandler implements IOHandler {
    private final Pipeline input;
    private final Pipeline output;
    private final Controller controller;
    private final Collection<Configurator> defaultConfigurators;

    public AbstractIOHandler(Controller controller, Collection<Configurator> defaultConfigurators) {
        input = new Pipeline();
        output = new Pipeline();
        this.controller = Objects.requireNonNull(controller);
        Objects.requireNonNull(defaultConfigurators);
        defaultConfigurators.forEach(Objects::requireNonNull);
        this.defaultConfigurators = Collections.unmodifiableCollection(defaultConfigurators);
    }

    @Override
    public Pipeline getInput() {
        return input;
    }

    @Override
    public Pipeline getOutput() {
        return output;
    }

    @Override
    public void configure(Collection<Configurator> configurators) {
        configurators = Checks.requireNonNullElse(configurators, defaultConfigurators);
        configurators.forEach(e -> e.configure(this));
    }

    @Override
    public Controller getController() {
        return controller;
    }

    @Override
    public PipelineResult process(Object data) {
        return output.process(input.process(data));
    }
}
