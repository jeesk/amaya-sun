package io.github.amayaframework.core.pipelines.sun;

import io.github.amayaframework.core.methods.HttpMethod;
import io.github.amayaframework.core.pipelines.PipelineAction;
import io.github.amayaframework.core.routers.Route;
import io.github.amayaframework.core.routers.Router;
import io.github.amayaframework.server.interfaces.HttpExchange;
import io.github.amayaframework.server.utils.HttpCode;

import java.net.URI;
import java.util.Objects;

/**
 * <p>An input action during which the requested method will be checked and the requested route will be found.</p>
 * <p>Receives: {@link HttpExchange}</p>
 * <p>Returns: {@link RequestData}</p>
 */
public class SunFindRouteAction extends PipelineAction<HttpExchange, RequestData> {
    private final Router router;
    private final int length;

    public SunFindRouteAction(Router router, String path) {
        this.router = Objects.requireNonNull(router);
        this.length = Objects.requireNonNull(path).length();
    }

    @Override
    public RequestData apply(HttpExchange exchange) {
        HttpMethod method = null;
        try {
            method = HttpMethod.valueOf(exchange.getRequestMethod());
        } catch (Exception e) {
            reject(HttpCode.NOT_IMPLEMENTED);
        }
        URI uri = exchange.getRequestURI();
        String path = uri.getPath().substring(length);
        Route route = router.follow(method, path);
        System.out.println(path);
        if (route == null) {
            reject(HttpCode.NOT_FOUND);
        }
        return new RequestData(exchange, route, path, method);
    }
}
