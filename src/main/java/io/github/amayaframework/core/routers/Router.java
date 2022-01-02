package io.github.amayaframework.core.routers;

import io.github.amayaframework.core.methods.HttpMethod;

public interface Router {
    Route follow(HttpMethod method, String route);

    void addRoute(HttpMethod method, Route route);

    Route getRoute(HttpMethod method, String pattern);

    Route removeRoute(HttpMethod method, String pattern);
}
