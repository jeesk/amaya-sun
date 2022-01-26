package io.github.amayaframework.core.pipelines.sun;

import io.github.amayaframework.core.methods.HttpMethod;
import io.github.amayaframework.core.pipelines.AbstractRequestData;
import io.github.amayaframework.core.routers.Route;
import io.github.amayaframework.server.interfaces.HttpExchange;

/**
 * A simple container created to transfer data between pipeline actions.
 */
public class RequestData extends AbstractRequestData {
    protected final HttpExchange exchange;

    protected RequestData(HttpExchange exchange, Route route, String path, HttpMethod method) {
        super(route, path, method);
        this.exchange = exchange;
    }

    public HttpExchange getExchange() {
        return exchange;
    }
}
