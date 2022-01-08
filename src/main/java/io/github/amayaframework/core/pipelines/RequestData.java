package io.github.amayaframework.core.pipelines;

import io.github.amayaframework.core.contexts.HttpRequest;
import io.github.amayaframework.core.methods.HttpMethod;
import io.github.amayaframework.core.routers.Route;
import io.github.amayaframework.server.interfaces.HttpExchange;

/**
 * A simple container created to transfer data between pipeline actions.
 */
public class RequestData {
    protected final HttpExchange exchange;
    protected final Route route;
    protected final String path;
    protected final HttpMethod method;
    protected HttpRequest request;

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

    public HttpRequest getRequest() {
        return request;
    }

    public void setRequest(HttpRequest request) {
        this.request = request;
    }
}
