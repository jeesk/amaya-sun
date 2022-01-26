package io.github.amayaframework.core.pipelines.servlets;

import io.github.amayaframework.core.methods.HttpMethod;
import io.github.amayaframework.core.pipelines.AbstractRequestData;
import io.github.amayaframework.core.routers.Route;

import javax.servlet.http.HttpServletRequest;

public class RequestData extends AbstractRequestData {
    protected final HttpServletRequest servletRequest;

    public RequestData(HttpServletRequest request, HttpMethod method, Route route, String path) {
        super(route, path, method);
        this.servletRequest = request;
    }

    public RequestData(HttpServletRequest request, HttpMethod method) {
        this(request, method, null, null);
    }

    public HttpServletRequest getServletRequest() {
        return servletRequest;
    }
}
