package io.github.amayaframework.core.handlers;

import io.github.amayaframework.core.controllers.Controller;
import io.github.amayaframework.core.util.AmayaConfig;
import io.github.amayaframework.server.interfaces.HttpExchange;
import io.github.amayaframework.server.interfaces.HttpHandler;
import io.github.amayaframework.server.utils.HttpCode;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.Objects;

public abstract class AmayaHandler implements HttpHandler {
    private final Controller controller;
    private final Charset charset = AmayaConfig.INSTANCE.getCharset();

    public AmayaHandler(Controller controller) {
        this.controller = Objects.requireNonNull(controller);
    }

    public Controller getController() {
        return controller;
    }

    protected void reject(HttpExchange exchange) throws IOException {
        sendAnswer(exchange, HttpCode.INTERNAL_SERVER_ERROR, HttpCode.INTERNAL_SERVER_ERROR.getMessage());
    }

    protected void sendAnswer(HttpExchange exchange, HttpCode code, String body) throws IOException {
        if (body == null) {
            exchange.sendResponseHeaders(code, 0);
            exchange.close();
            return;
        }
        exchange.sendResponseHeaders(code, body.getBytes(charset).length);
        BufferedWriter writer = wrapOutputStream(exchange.getResponseBody());
        writer.write(body);
        writer.flush();
        exchange.close();
    }

    protected BufferedWriter wrapOutputStream(OutputStream stream) {
        return new BufferedWriter(new OutputStreamWriter(stream, charset));
    }
}
