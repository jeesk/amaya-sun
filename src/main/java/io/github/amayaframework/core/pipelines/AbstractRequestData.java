package io.github.amayaframework.core.pipelines;

import io.github.amayaframework.core.contexts.HttpRequest;
import io.github.amayaframework.core.methods.HttpMethod;
import io.github.amayaframework.core.routers.Route;

public abstract class AbstractRequestData {
    private Route route;
    private String path;
    private HttpMethod method;
    private HttpRequest request;

    protected AbstractRequestData(Route route, String path, HttpMethod method) {
        this.route = route;
        this.path = path;
        this.method = method;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    public HttpRequest getRequest() {
        return request;
    }

    public void setRequest(HttpRequest request) {
        this.request = request;
    }
}