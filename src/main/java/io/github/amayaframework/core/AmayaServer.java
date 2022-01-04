package io.github.amayaframework.core;

import io.github.amayaframework.core.controllers.Controller;
import io.github.amayaframework.core.handlers.PipelineHandler;
import io.github.amayaframework.server.interfaces.HttpContext;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

public interface AmayaServer {
    void start();

    void stop(int delay);

    void stop();

    Executor getExecutor();

    void setExecutor(Executor executor);

    HttpContext addController(Controller controller);

    void setPipelineConfigurators(List<Consumer<PipelineHandler>> configurators);

    InetSocketAddress getAddress();
}
