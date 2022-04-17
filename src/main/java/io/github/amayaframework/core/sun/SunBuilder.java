package io.github.amayaframework.core.sun;

import com.github.romanqed.jutils.util.Checks;
import io.github.amayaframework.core.Amaya;
import io.github.amayaframework.core.AmayaBuilder;
import io.github.amayaframework.core.configurators.Configurator;
import io.github.amayaframework.core.controllers.Controller;
import io.github.amayaframework.core.sun.handlers.SunHandler;
import io.github.amayaframework.server.Servers;
import io.github.amayaframework.server.interfaces.HttpServer;
import io.github.amayaframework.server.interfaces.HttpsServer;
import io.github.amayaframework.server.utils.HttpsConfigurator;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.InetSocketAddress;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * A builder that helps to instantiate a properly configured Amaya Server.
 */
public class SunBuilder extends AmayaBuilder<HttpServer> {
    private static final String ACTIONS_PREFIX = "io.github.amayaframework.core.sun.actions";
    private InetSocketAddress address;
    private Executor executor;
    private HttpsConfigurator configurator;
    private int backlog;

    public SunBuilder() {
        super(ACTIONS_PREFIX);
        resetValues();
    }

    protected void resetValues() {
        super.resetValues();
        executor = Executors.newWorkStealingPool();
        address = new InetSocketAddress(8000);
        configurator = null;
        backlog = 0;
    }

    /**
     * Sets https configurator used to configuring sun https server.
     * If not specified, the http server will be created.
     *
     * @param configurator {@link HttpsConfigurator} configurator to be used. Must be not null.
     * @return {@link SunBuilder} instance
     */
    public SunBuilder httpsConfigurator(HttpsConfigurator configurator) {
        this.configurator = Objects.requireNonNull(configurator);
        return this;
    }

    /**
     * Binds server to given address.
     *
     * @param address {@link InetSocketAddress} Must be not null.
     * @return {@link SunBuilder} instance
     */
    public SunBuilder bind(InetSocketAddress address) {
        this.address = Objects.requireNonNull(address);
        if (config.isDebug()) {
            logger.debug("Bind server to " + address);
        }
        return this;
    }

    /**
     * Binds server to given address.
     *
     * @param host Host address
     * @param port Host port
     * @return {@link SunBuilder} instance
     */
    public SunBuilder bind(String host, int port) {
        return bind(new InetSocketAddress(host, port));
    }

    /**
     * Binds server to given address.
     *
     * @param port Host port
     * @return {@link SunBuilder} instance
     */
    public SunBuilder bind(int port) {
        return bind(new InetSocketAddress(port));
    }

    /**
     * Sets the executor to be used when processing http transactions.
     *
     * @param executor {@link Executor} can be easily created with {@link Executors}. Must be not null.
     * @return {@link SunBuilder} instance
     */
    public SunBuilder executor(Executor executor) {
        this.executor = Objects.requireNonNull(executor);
        if (config.isDebug()) {
            logger.debug("Set Executor to " + executor.getClass().getSimpleName());
        }
        return this;
    }

    /**
     * Adds the configurator to the end of the current list of configurators.
     *
     * @param configurator {@link Consumer} configurator to be added. Must be not null.
     * @return {@link SunBuilder} instance
     */
    @Override
    public SunBuilder addConfigurator(Configurator configurator) {
        return (SunBuilder) super.addConfigurator(configurator);
    }

    /**
     * Deletes all configurators whose class matches the specified class.
     *
     * @param clazz with which to delete
     * @return {@link SunBuilder} instance
     */
    @Override
    public SunBuilder removeConfigurator(Class<? extends Configurator> clazz) {
        return (SunBuilder) super.removeConfigurator(clazz);
    }

    /**
     * Adds the controller to the list of processed
     *
     * @param controller {@link Controller} controller to be added. Must be not null.
     * @return {@link SunBuilder} builder instance
     */
    @Override
    public SunBuilder addController(Controller controller) {
        return (SunBuilder) super.addController(controller);
    }

    /**
     * Removes the controller from the list of processed
     *
     * @param path controller path
     * @return {@link SunBuilder} instance
     */
    @Override
    public SunBuilder removeController(String path) {
        return (SunBuilder) super.removeController(path);
    }

    /**
     * Sets the annotation by which the controllers will be scanned.
     * If value will be null, the scan will not be performed.
     *
     * @param annotation {@link Class} of annotation
     * @return {@link SunBuilder} instance
     */
    @Override
    public SunBuilder controllerAnnotation(Class<? extends Annotation> annotation) {
        return (SunBuilder) super.controllerAnnotation(annotation);
    }

    public SunBuilder setBacklog(int backlog) {
        this.backlog = Checks.requireCorrectValue(backlog, e -> e >= 0);
        return this;
    }

    private HttpServer makeHttpsServer() throws IOException {
        HttpsServer ret = Servers.httpsServer(address, backlog);
        ret.setHttpsConfigurator(configurator);
        if (config.isDebug()) {
            logger.debug("Create https server");
        }
        return ret;
    }

    /**
     * Creates an Amaya Server instance corresponding to the specified parameters
     * and resets the builder to the initial parameters.
     *
     * @return {@link HttpServer} instance
     * @throws IOException in case of unsuccessful initialization of the server
     */
    @Override
    public Amaya<HttpServer> build() throws IOException {
        HttpServer server;
        if (configurator != null) {
            server = makeHttpsServer();
        } else {
            server = Servers.httpServer(address, backlog);
        }
        server.setExecutor(executor);
        findControllers();
        controllers.forEach((path, controller) -> {
            SunHandler handler = new SunHandler(controller);
            configure(handler.getHandler(), controller);
            if (path.equals("")) {
                path = "/";
            }
            server.createContext(path, handler);
        });
        resetValues();
        return new SunAmaya(server);
    }
}
