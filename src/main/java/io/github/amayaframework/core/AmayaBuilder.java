package io.github.amayaframework.core;

import io.github.amayaframework.core.configurators.Configurator;
import io.github.amayaframework.core.controllers.Controller;
import io.github.amayaframework.core.handlers.SunHandler;
import io.github.amayaframework.core.util.AmayaConfig;
import io.github.amayaframework.server.interfaces.HttpServer;
import io.github.amayaframework.server.interfaces.HttpsServer;
import io.github.amayaframework.server.utils.HttpsConfigurator;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * A builder that helps to instantiate a properly configured Amaya Server.
 */
public class AmayaBuilder extends AbstractBuilder {
    private InetSocketAddress address;
    private Executor executor;
    private HttpsConfigurator configurator;

    public AmayaBuilder() {
        resetValues();
    }

    protected void resetValues() {
        super.resetValues();
        executor = Executors.newWorkStealingPool();
        address = new InetSocketAddress(8000);
        configurator = null;
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
        return (AmayaBuilder) super.pipelineConfigurators(configurators);
    }

    /**
     * Adds the configurator to the end of the current list of configurators.
     *
     * @param configurator {@link Consumer} configurator to be added. Must be not null.
     * @return {@link AmayaBuilder} instance
     */
    public AmayaBuilder addConfigurator(Configurator configurator) {
        return (AmayaBuilder) super.addConfigurator(configurator);
    }

    /**
     * Adds the controller to the list of processed
     *
     * @param controller {@link Controller} controller to be added. Must be not null.
     * @return {@link AmayaBuilder} builder instance
     */
    public AmayaBuilder addController(Controller controller) {
        return (AmayaBuilder) super.addController(controller);
    }

    public AmayaBuilder removeController(String path) {
        return (AmayaBuilder) super.removeController(path);
    }

    /**
     * Sets the annotation by which the controllers will be scanned.
     * If value will be null, the scan will not be performed.
     *
     * @param annotation {@link Class} of annotation
     * @return {@link AmayaBuilder} instance
     */
    public AmayaBuilder controllerAnnotation(Class<? extends Annotation> annotation) {
        return (AmayaBuilder) super.controllerAnnotation(annotation);
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
        findControllers();
        controllers.forEach((path, controller) -> {
            SunHandler handler = new SunHandler(controller);
            handler.getHandler().configure(configurators);
            server.createContext(path, handler);
        });
        resetValues();
        return ret;
    }
}
