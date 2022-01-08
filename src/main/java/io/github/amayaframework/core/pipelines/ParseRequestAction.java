package io.github.amayaframework.core.pipelines;

import com.github.romanqed.jutils.util.Checks;
import io.github.amayaframework.core.contexts.HttpRequest;
import io.github.amayaframework.core.util.AmayaConfig;
import io.github.amayaframework.core.util.ParseUtil;
import io.github.amayaframework.server.interfaces.HttpExchange;
import io.github.amayaframework.server.utils.HttpCode;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>An input action during which the basic components of the request will be checked and parsed:
 * query parameters, path parameters, headers and the request body.</p>
 * <p>Receives: {@link RequestData}</p>
 * <p>Returns: {@link RequestData}</p>
 */
public class ParseRequestAction extends PipelineAction<RequestData, RequestData> {
    private final Charset charset = AmayaConfig.INSTANCE.getCharset();

    @Override
    public RequestData apply(RequestData requestData) {
        HttpExchange exchange = requestData.exchange;
        Map<String, List<String>> query = Checks.requireNonException(
                () -> ParseUtil.parseQueryString(exchange.getRequestURI().getQuery()),
                HashMap::new
        );
        Map<String, Object> params = null;
        try {
            params = ParseUtil.extractRouteParameters(requestData.route, requestData.path);
        } catch (Exception e) {
            reject(HttpCode.BAD_REQUEST);
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody(), charset));
        String body = reader.lines().reduce("", (left, right) -> left + right + "\n");
        requestData.request = new HttpRequest.Builder().
                method(requestData.method).
                headers(exchange.getRequestHeaders()).
                queryParameters(query).
                pathParameters(params).
                body(body).
                build();
        return requestData;
    }
}
