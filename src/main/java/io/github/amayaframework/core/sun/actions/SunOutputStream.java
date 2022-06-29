package io.github.amayaframework.core.sun.actions;

import io.github.amayaframework.core.contexts.FixedOutputStream;
import io.github.amayaframework.http.HttpCode;
import io.github.amayaframework.server.interfaces.HttpExchange;

import java.io.IOException;

public class SunOutputStream extends FixedOutputStream {
    private final HttpExchange exchange;
    private final HttpCode code;

    public SunOutputStream(HttpExchange exchange, HttpCode code) {
        super(exchange.getResponseBody());
        this.exchange = exchange;
        this.code = code;
    }

    @Override
    public void specifyLength(long length) throws IOException {
        super.specifyLength(length);
        exchange.sendResponseHeaders(code, length);
    }
}
