package io.github.amayaframework.core.pipelines;

import com.github.romanqed.jutils.structs.Pair;
import io.github.amayaframework.core.contexts.HttpRequest;
import io.github.amayaframework.core.routers.Route;
import io.github.amayaframework.core.util.AmayaConfig;
import io.github.amayaframework.core.util.ParseUtil;
import io.github.amayaframework.server.interfaces.HttpExchange;
import io.github.amayaframework.server.utils.HttpCode;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

public class ParseRequestAction extends PipelineAction<RequestData, Pair<HttpRequest, Route>> {
    private final Charset charset = AmayaConfig.INSTANCE.getCharset();

    public ParseRequestAction() {
        super(ProcessStage.PARSE_REQUEST.name());
    }

    @Override
    public Pair<HttpRequest, Route> apply(RequestData requestData) {
        HttpExchange exchange = requestData.exchange;
        Map<String, List<String>> query = null;
        try {
            query = ParseUtil.parseQueryString(exchange.getRequestURI().getQuery());
        } catch (Exception e) {
            reject(HttpCode.BAD_REQUEST);
        }
        Map<String, Object> params = ParseUtil.extractRouteParameters(requestData.route, requestData.path);
        BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody(), charset));
        String body = reader.lines().reduce("", (left, right) -> left + right + "\n");
        HttpRequest request = new HttpRequest.Builder().
                method(requestData.method).
                headers(exchange.getRequestHeaders()).
                queryParameters(query).
                pathParameters(params).
                body(body).
                build();
        return new Pair<>(request, requestData.route);
    }
}
