package io.github.amayaframework.core;

import io.github.amayaframework.core.configurators.Configurator;
import io.github.amayaframework.core.controllers.Controller;
import io.github.amayaframework.core.controllers.Endpoint;
import io.github.amayaframework.core.handlers.PipelineHandler;
import io.github.amayaframework.core.scanners.ControllerScanner;
import io.github.amayaframework.core.util.AmayaConfig;
import io.github.amayaframework.server.interfaces.HttpServer;
import io.github.amayaframework.server.interfaces.HttpsServer;
import io.github.amayaframework.server.utils.HttpsConfigurator;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * A builder that helps to instantiate a properly configured Amaya Server.
 */
public class AmayaBuilder {
    private final Map<String, Controller> controllers;
    private Collection<Configurator> configurators;
    private Class<? extends Annotation> annotation;
    private InetSocketAddress address;
    private Executor executor;
    private HttpsConfigurator configurator;

    public AmayaBuilder() {
        controllers = new ConcurrentHashMap<>();
        resetValues();
    }

    private void resetValues() {
        executor = Executors.newWorkStealingPool();
        address = new InetSocketAddress(8000);
        configurator = null;
        annotation = Endpoint.class;
        configurators = null;
        controllers.clear();
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
    public AmayaBuilder pipelineConfigurators(Collection<Configurator> configurators) {
        Objects.requireNonNull(configurators);
        configurators.forEach(Objects::requireNonNull);
        this.configurators = configurators;
        return this;
    }

    /**
     * Adds the configurator to the end of the current list of configurators.
     *
     * @param configurator {@link Consumer} configurator to be added. Must be not null.
     * @return {@link AmayaBuilder} instance
     */
    public AmayaBuilder addConfigurator(Configurator configurator) {
        Objects.requireNonNull(configurator);
        if (configurators == null) {
            configurators = new LinkedList<>();
        }
        configurators.add(configurator);
        return this;
    }

    /**
     * Adds the controller to the list of processed
     *
     * @param controller {@link Controller} controller to be added. Must be not null.
     * @return {@link AmayaBuilder} builder instance
     */
    public AmayaBuilder addController(Controller controller) {
        Objects.requireNonNull(controller);
        String path = controller.getPath();
        Objects.requireNonNull(path);
        controllers.put(path, controller);
        return this;
    }

    public AmayaBuilder removeController(String path) {
        Objects.requireNonNull(path);
        controllers.remove(path);
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
        if (annotation != null) {
            Set<Controller> controllers;
            try {
                controllers = new ControllerScanner(annotation).find();
            } catch (Exception e) {
                throw new IllegalStateException("Exception when scanning controllers!", e);
            }
            controllers.forEach(this::addController);
        }
        controllers.forEach((path, controller) -> {
            PipelineHandler handler = new PipelineHandler(controller);
            handler.configure(configurators);
            server.createContext(path, handler);
        });
        resetValues();
        return ret;
    }
}
