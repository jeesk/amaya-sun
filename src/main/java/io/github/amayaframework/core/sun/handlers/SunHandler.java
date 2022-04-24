package io.github.amayaframework.core.sun.handlers;

import io.github.amayaframework.core.ConfigProvider;
import io.github.amayaframework.core.config.AmayaConfig;
import io.github.amayaframework.core.controllers.Controller;
import io.github.amayaframework.core.handlers.PipelineHandler;
import io.github.amayaframework.core.handlers.Session;
import io.github.amayaframework.server.interfaces.HttpExchange;
import io.github.amayaframework.server.interfaces.HttpHandler;

import java.io.IOException;

/**
 * <p>A class representing the sun handler used inside the server. Built on pipelines.</p>
 * <p>The http connection processing scheme is quite simple: first, the input pipeline is triggered,
 * resulting in an HttpResponse. Then the output pipeline is triggered, the purpose of which is to
 * process and verify the received HttpResponse. After that, the server receives a response.</p>
 */
public class SunHandler implements HttpHandler {
    private final PipelineHandler handler;
    private final Controller controller;
    private final AmayaConfig config;

    public SunHandler(Controller controller, PipelineHandler handler) {
        this.controller = controller;
        this.handler = handler;
        config = ConfigProvider.getConfig();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Session session = new SunSession(exchange, controller, config);
        handler.handle(session);
        exchange.close();
    }
}
