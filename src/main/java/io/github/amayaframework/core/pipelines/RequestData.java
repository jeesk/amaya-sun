package io.github.amayaframework.core.pipelines;

import io.github.amayaframework.core.methods.HttpMethod;
import io.github.amayaframework.core.routers.Route;
import io.github.amayaframework.server.interfaces.HttpExchange;

/**
 * A simple container created to transfer data between FindRouteAction and ParseRequestAction.
 */
public class RequestData {
    protected final HttpExchange exchange;
    protected final Route route;
    protected final String path;
    protected final HttpMethod method;

    protected RequestData(HttpExchange exchange, Route route, String path, HttpMethod method) {
        this.exchange = exchange;
        this.route = route;
        this.path = path;
        this.method = method;
    }

    public HttpExchange getExchange() {
        return exchange;
    }

    public Route getRoute() {
        return route;
    }

    public String getPath() {
        return path;
    }

    public HttpMethod getMethod() {
        return method;
    }
}
