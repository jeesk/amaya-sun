package io.github.amayaframework.core.pipelines.servlets;

import io.github.amayaframework.core.pipelines.PipelineAction;
import io.github.amayaframework.core.routers.Route;
import io.github.amayaframework.core.routers.Router;
import io.github.amayaframework.server.utils.HttpCode;

import java.util.Objects;

public class ServletsFindRouteAction extends PipelineAction<RequestData, RequestData> {
    private final Router router;
    private final int length;

    public ServletsFindRouteAction(Router router, String path) {
        this.router = Objects.requireNonNull(router);
        this.length = Objects.requireNonNull(path).length();
    }

    @Override
    public RequestData apply(RequestData requestData) {
        String path = requestData.servletRequest.getRequestURI().substring(length);
        Route route = router.follow(requestData.getMethod(), path);
        if (route == null) {
            reject(HttpCode.NOT_FOUND);
        }
        requestData.setRoute(route);
        requestData.setPath(path);
        return requestData;
    }
}
