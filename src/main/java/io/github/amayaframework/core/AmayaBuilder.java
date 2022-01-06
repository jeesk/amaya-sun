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

/**
 * A builder that helps to instantiate a properly configured Amaya Server.
 */
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

    /**
     * Sets https configurator used to configuring sun https server.
     * If not specified, the http server will be created.
     *
     * @param configurator {@link HttpsConfigurator} configurator to be used. Must be not null.
     * @return {@link AmayaBuilder} instance
     */
    public AmayaBuilder httpsConfigurator(HttpsConfigurator configurator) {
        this.configurator = Objects.requireNonNull(configurator);
        return this;
    }

    /**
     * Binds server to given address.
     *
     * @param address {@link InetSocketAddress} Must be not null.
     * @return {@link AmayaBuilder} instance
     */
    public AmayaBuilder bind(InetSocketAddress address) {
        this.address = Objects.requireNonNull(address);
        return this;
    }

    /**
     * Sets the executor to be used when processing http transactions.
     *
     * @param executor {@link Executor} can be easily created with {@link Executors}. Must be not null.
     * @return {@link AmayaBuilder} instance
     */
    public AmayaBuilder executor(Executor executor) {
        this.executor = Objects.requireNonNull(executor);
        return this;
    }

    /**
     * Sets a list of configurators that will be used when adding each controller.
     *
     * @param configurators {@link List} configurators to be set. Must be not null.
     * @return {@link AmayaBuilder} instance
     */
    public AmayaBuilder pipelineConfigurators(List<Consumer<PipelineHandler>> configurators) {
        Objects.requireNonNull(configurators);
        configurators.forEach(Objects::requireNonNull);
        this.configurators.clear();
        this.configurators.addAll(configurators);
        return this;
    }

    /**
     * Adds the configurator to the end of the current list of configurators.
     *
     * @param configurator {@link Consumer} configurator to be added. Must be not null.
     * @return {@link AmayaBuilder} instance
     */
    public AmayaBuilder addConfigurator(Consumer<PipelineHandler> configurator) {
        Objects.requireNonNull(configurator);
        configurators.add(configurator);
        return this;
    }

    /**
     * Sets the annotation by which the controllers will be scanned.
     * If value will be null, the scan will not be performed.
     *
     * @param annotation {@link Class} of annotation
     * @return {@link AmayaBuilder} instance
     */
    public AmayaBuilder controllerAnnotation(Class<? extends Annotation> annotation) {
        this.annotation = annotation;
        return this;
    }

    private HttpServer makeHttpsServer() throws IOException {
        HttpsServer ret = HttpsServer.create(address, AmayaConfig.INSTANCE.getBacklog());
        ret.setHttpsConfigurator(configurator);
        return ret;
    }

    /**
     * Creates an Amaya Server instance corresponding to the specified parameters
     * and resets the builder to the initial parameters.
     *
     * @return {@link AmayaServer} instance
     * @throws IOException
     */
    public AmayaServer build() throws IOException {
        HttpServer server;
        if (configurator != null) {
            server = makeHttpsServer();
        } else {
            server = HttpServer.create(address, AmayaConfig.INSTANCE.getBacklog());
        }
        server.setExecutor(executor);
        AmayaServer ret = new AmayaServerImpl(server);
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
