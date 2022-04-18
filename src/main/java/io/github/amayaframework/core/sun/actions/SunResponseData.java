package io.github.amayaframework.core.sun.actions;

import io.github.amayaframework.core.contexts.HttpResponse;
import io.github.amayaframework.core.pipeline.AbstractResponseData;
import io.github.amayaframework.server.interfaces.HttpExchange;

/**
 * A simple container created to transfer data between output pipeline actions.
 */
public class SunResponseData extends AbstractResponseData {
    protected final HttpExchange exchange;

    public SunResponseData(HttpExchange exchange, HttpResponse response) {
        super(response);
        this.exchange = exchange;
    }

    public HttpExchange getExchange() {
        return exchange;
    }
}
