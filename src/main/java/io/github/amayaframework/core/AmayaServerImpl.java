package io.github.amayaframework.core;

import io.github.amayaframework.core.controllers.Controller;
import io.github.amayaframework.core.handlers.PipelineHandler;
import io.github.amayaframework.server.interfaces.HttpContext;
import io.github.amayaframework.server.interfaces.HttpServer;

import java.net.InetSocketAddress;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

public class AmayaServerImpl implements AmayaServer {
    private final List<Consumer<PipelineHandler>> configurators;
    protected HttpServer server;

    protected AmayaServerImpl(HttpServer server) {
        this.server = Objects.requireNonNull(server);
        this.configurators = new LinkedList<>();
    }

    @Override
    public void start() {
        server.start();
    }

    @Override
    public void stop(int delay) {
        server.stop(delay);
    }

    @Override
    public void stop() {
        server.stop(0);
    }

    @Override
    public Executor getExecutor() {
        return server.getExecutor();
    }

    @Override
    public HttpContext addController(Controller controller) {
        Objects.requireNonNull(controller);
        String path = controller.getPath();
        Objects.requireNonNull(path);
        PipelineHandler handler = new PipelineHandler(controller);
        configurators.forEach(e -> e.accept(handler));
        return server.createContext(path, handler);
    }

    @Override
    public void setPipelineConfigurators(List<Consumer<PipelineHandler>> configurators) {
        Objects.requireNonNull(configurators);
        configurators.forEach(Objects::requireNonNull);
        this.configurators.clear();
        this.configurators.addAll(configurators);
    }

    @Override
    public InetSocketAddress getAddress() {
        return server.getAddress();
    }
}
