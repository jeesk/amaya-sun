package io.github.amayaframework.core;

import io.github.amayaframework.core.controllers.Controller;
import io.github.amayaframework.core.controllers.Endpoint;
import io.github.amayaframework.core.handlers.BaseHandlerConfigurator;
import io.github.amayaframework.core.handlers.PipelineHandler;
import io.github.amayaframework.core.scanners.ControllerScanner;
import io.github.amayaframework.core.util.AmayaConfig;
import io.github.amayaframework.server.interfaces.HttpServer;
import io.github.amayaframework.server.interfaces.HttpsServer;
import io.github.amayaframework.server.utils.HttpsConfigurator;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.InetSocketAddress;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class AmayaBuilder {
    private final List<Consumer<PipelineHandler>> configurators;
    private Class<? extends Annotation> annotation;
    private InetSocketAddress address;
    private Executor executor;
    private HttpsConfigurator configurator;

    public AmayaBuilder() {
        configurators = new LinkedList<>();
        resetValues();
    }

    private void resetValues() {
        executor = Executors.newWorkStealingPool();
        address = new InetSocketAddress(8000);
        configurator = null;
        annotation = Endpoint.class;
        configurators.clear();
        configurators.add(new BaseHandlerConfigurator());
    }

    public AmayaBuilder httpsConfigurator(HttpsConfigurator configurator) {
        this.configurator = Objects.requireNonNull(configurator);
        return this;
    }

    public AmayaBuilder bind(InetSocketAddress address) {
        this.address = Objects.requireNonNull(address);
        return this;
    }

    public AmayaBuilder executor(Executor executor) {
        this.executor = Objects.requireNonNull(executor);
        return this;
    }

    public AmayaBuilder pipelineConfigurators(List<Consumer<PipelineHandler>> configurators) {
        Objects.requireNonNull(configurators);
        this.configurators.clear();
        this.configurators.addAll(configurators);
        return this;
    }

    public AmayaBuilder addConfigurator(Consumer<PipelineHandler> configurator) {
        Objects.requireNonNull(configurator);
        configurators.add(configurator);
        return this;
    }

    public AmayaBuilder controllerAnnotation(Class<? extends Annotation> annotation) {
        this.annotation = Objects.requireNonNull(annotation);
        return this;
    }

    private HttpServer makeHttpsServer() throws IOException {
        HttpsServer ret = HttpsServer.create(address, AmayaConfig.INSTANCE.getBacklog());
        ret.setHttpsConfigurator(configurator);
        return ret;
    }

    public AmayaServer build() throws IOException {
        HttpServer server;
        if (configurator != null) {
            server = makeHttpsServer();
        } else {
            server = HttpServer.create(address, AmayaConfig.INSTANCE.getBacklog());
        }
        AmayaServer ret = new AmayaServerImpl(server);
        ret.setExecutor(executor);
        ret.setPipelineConfigurators(configurators);
        if (annotation != null) {
            Set<Controller> controllers;
            try {
                controllers = new ControllerScanner(annotation).find();
            } catch (Exception e) {
                throw new IllegalStateException("Exception when scanning controllers!", e);
            }
            controllers.forEach(ret::addController);
        }
        resetValues();
        return ret;
    }
}
