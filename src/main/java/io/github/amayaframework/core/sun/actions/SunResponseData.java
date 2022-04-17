package io.github.amayaframework.core.sun.actions;

import io.github.amayaframework.core.contexts.HttpResponse;
import io.github.amayaframework.core.pipeline.ResponseData;
import io.github.amayaframework.server.interfaces.HttpExchange;

/**
 * A simple container created to transfer data between output pipeline actions.
 */
public class SunResponseData implements ResponseData {
    protected final HttpExchange exchange;
    protected final HttpResponse response;

    public SunResponseData(HttpExchange exchange, HttpResponse response) {
        this.exchange = exchange;
        this.response = response;
    }

    public HttpExchange getExchange() {
        return exchange;
    }

    @Override
    public HttpResponse getResponse() {
        return response;
    }
}
