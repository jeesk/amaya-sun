package io.github.amayaframework.core.pipelines;

import com.github.romanqed.jutils.http.HttpCode;
import io.github.amayaframework.core.methods.HttpMethod;
import io.github.amayaframework.core.routers.MethodRouter;
import io.github.amayaframework.core.routes.MethodRoute;
import io.github.amayaframework.core.util.ParseUtil;
import io.github.amayaframework.server.interfaces.HttpExchange;

import java.net.URI;
import java.util.Objects;

/**
 * <p>The action that queries the request body according to the passed headers.</p>
 * <p>Receives: {@link SunRequestData}</p>
 * <p>Returns: {@link SunRequestData}</p>
 */
public class FindRouteAction extends PipelineAction<SunRequestData, SunRequestData> {
    private final MethodRouter router;
    private final int length;

    public FindRouteAction(MethodRouter router, String path) {
        this.router = Objects.requireNonNull(router);
        this.length = Objects.requireNonNull(path).length();
    }

    @Override
    public SunRequestData execute(SunRequestData requestData) {
        HttpExchange exchange = requestData.exchange;
        HttpMethod method = HttpMethod.fromName(exchange.getRequestMethod());
        if (method == null) {
            reject(HttpCode.NOT_IMPLEMENTED);
        }
        URI uri = exchange.getRequestURI();
        String path = uri.getPath().substring(length);
        path = ParseUtil.normalizePath(path);
        MethodRoute route = router.follow(method, path);
        if (route == null) {
            reject(HttpCode.NOT_FOUND);
        }
        requestData.setRoute(route);
        requestData.setPath(path);
        requestData.setMethod(method);
        return requestData;
    }
}
