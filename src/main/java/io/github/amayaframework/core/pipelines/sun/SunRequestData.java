package io.github.amayaframework.core.pipelines.sun;

import io.github.amayaframework.core.methods.HttpMethod;
import io.github.amayaframework.core.pipelines.RequestData;
import io.github.amayaframework.core.routers.Route;
import io.github.amayaframework.server.interfaces.HttpExchange;

/**
 * A simple container created to transfer data between pipeline actions.
 */
public class SunRequestData extends RequestData {
    protected final HttpExchange exchange;

    public SunRequestData(HttpExchange exchange, Route route, String path, HttpMethod method) {
        super(route, path, method);
        this.exchange = exchange;
    }

    public SunRequestData(HttpExchange exchange) {
        this(exchange, null, null, null);
    }

    public HttpExchange getExchange() {
        return exchange;
    }
}
