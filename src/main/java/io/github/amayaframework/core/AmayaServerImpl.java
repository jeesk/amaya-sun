package io.github.amayaframework.core;

import io.github.amayaframework.server.interfaces.HttpServer;

import java.net.InetSocketAddress;
import java.util.Objects;
import java.util.concurrent.Executor;

public class AmayaServerImpl implements AmayaServer {
    protected HttpServer server;

    protected AmayaServerImpl(HttpServer server) {
        this.server = Objects.requireNonNull(server);
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

//    @Override
//    public HttpContext addController(Controller controller) {
//        Objects.requireNonNull(controller);
//        String path = controller.getPath();
//        Objects.requireNonNull(path);
//        PipelineHandler handler = new PipelineHandler(controller);
//        configurators.forEach(e -> e.accept(handler));
//        return server.createContext(path, handler);
//    }

    @Override
    public InetSocketAddress getAddress() {
        return server.getAddress();
    }
}
