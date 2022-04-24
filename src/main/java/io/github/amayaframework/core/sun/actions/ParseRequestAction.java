package io.github.amayaframework.core.sun.actions;

import com.github.romanqed.util.Checks;
import io.github.amayaframework.core.pipeline.InputAction;
import io.github.amayaframework.core.sun.contexts.SunHttpRequest;
import io.github.amayaframework.core.util.ParseUtil;
import io.github.amayaframework.http.HttpCode;
import io.github.amayaframework.server.interfaces.HttpExchange;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>The action during which the basic components of the request will be checked and parsed:
 * query parameters, path parameters, headers and the request body.</p>
 * <p>Receives: {@link SunRequestData}</p>
 * <p>Returns: {@link SunRequestData}</p>
 */
public class ParseRequestAction extends InputAction<SunRequestData, SunRequestData> {

    @Override
    public SunRequestData execute(SunRequestData data) throws Throwable {
        HttpExchange exchange = data.exchange;
        Map<String, List<String>> query = Checks.requireNonException(
                () -> ParseUtil.parseQueryString(exchange.getRequestURI().getQuery(), data.getCharset()),
                HashMap::new
        );
        Map<String, Object> params = null;
        try {
            params = ParseUtil.extractRouteParameters(data.getRoute(), data.getPath());
        } catch (Exception e) {
            reject(HttpCode.BAD_REQUEST);
        }
        SunHttpRequest request = new SunHttpRequest();
        request.setHeaders(exchange.getRequestHeaders());
        request.setQuery(query);
        request.setPathParameters(params);
        data.setRequest(request);
        return data;
    }
}
