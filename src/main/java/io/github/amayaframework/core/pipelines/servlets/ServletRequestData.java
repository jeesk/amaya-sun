package io.github.amayaframework.core.pipelines.servlets;

import io.github.amayaframework.core.methods.HttpMethod;
import io.github.amayaframework.core.pipelines.RequestData;
import io.github.amayaframework.core.routers.Route;

import javax.servlet.http.HttpServletRequest;

/**
 * A simple container created to transfer data between pipeline actions.
 */
public class ServletRequestData extends RequestData {
    protected final HttpServletRequest servletRequest;

    public ServletRequestData(HttpServletRequest request, HttpMethod method, Route route, String path) {
        super(route, path, method);
        this.servletRequest = request;
    }

    public ServletRequestData(HttpServletRequest request, HttpMethod method) {
        this(request, method, null, null);
    }

    public HttpServletRequest getServletRequest() {
        return servletRequest;
    }
}
