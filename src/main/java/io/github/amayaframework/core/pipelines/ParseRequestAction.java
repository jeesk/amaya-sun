package io.github.amayaframework.core.pipelines;

import com.github.romanqed.jutils.structs.Pair;
import io.github.amayaframework.core.contexts.HttpRequest;
import io.github.amayaframework.core.methods.HttpMethod;
import io.github.amayaframework.core.routers.InvalidFormatException;
import io.github.amayaframework.core.routers.Route;
import io.github.amayaframework.core.routers.Router;
import io.github.amayaframework.core.util.AmayaConfig;
import io.github.amayaframework.core.util.ParseUtil;
import io.github.amayaframework.server.interfaces.HttpExchange;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ParseRequestAction extends PipelineAction<HttpExchange, Pair<HttpRequest, Route>> {
    private final Router router;
    private final int length;
    private final Charset charset = AmayaConfig.INSTANCE.getCharset();

    public ParseRequestAction(Router router, String path) {
        super(ProcessStage.PARSE_REQUEST.name());
        this.router = Objects.requireNonNull(router);
        this.length = Objects.requireNonNull(path).length();
    }

    @Override
    public Pair<HttpRequest, Route> apply(HttpExchange exchange) {
        HttpMethod method;
        try {
            method = HttpMethod.valueOf(exchange.getRequestMethod());
        } catch (Exception e) {
            throw new InvalidFormatException("invalid method", e);
        }
        URI uri = exchange.getRequestURI();
        String path = uri.getPath().substring(length);
        Route route = router.follow(method, path);
        if (route == null) {
            throw new NotFoundException(path);
        }
        Map<String, List<String>> query;
        try {
            query = ParseUtil.parseQueryString(uri.getQuery());
        } catch (UnsupportedEncodingException e) {
            throw new InvalidFormatException("invalid query", e);
        }
        Map<String, Object> params = ParseUtil.extractRouteParameters(route, path);
        BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody(), charset));
        String body = reader.lines().reduce("", (left, right) -> left + right + "\n");
        HttpRequest request = new HttpRequest.Builder().
                headers(exchange.getRequestHeaders()).
                queryParameters(query).
                pathParameters(params).
                body(body).
                build();
        return new Pair<>(request, route);
    }
}
