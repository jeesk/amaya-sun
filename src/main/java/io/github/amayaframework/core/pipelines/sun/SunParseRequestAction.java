package io.github.amayaframework.core.pipelines.sun;

import com.github.romanqed.jutils.util.Checks;
import io.github.amayaframework.core.contexts.SunHttpRequest;
import io.github.amayaframework.core.pipelines.PipelineAction;
import io.github.amayaframework.core.util.ParseUtil;
import io.github.amayaframework.server.interfaces.HttpExchange;
import io.github.amayaframework.server.utils.HttpCode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>An input action during which the basic components of the request will be checked and parsed:
 * query parameters, path parameters, headers and the request body.</p>
 * <p>Receives: {@link SunRequestData}</p>
 * <p>Returns: {@link SunRequestData}</p>
 */
public class SunParseRequestAction extends PipelineAction<SunRequestData, SunRequestData> {

    @Override
    public SunRequestData apply(SunRequestData requestData) {
        HttpExchange exchange = requestData.exchange;
        Map<String, List<String>> query = Checks.requireNonException(
                () -> ParseUtil.parseQueryString(exchange.getRequestURI().getQuery()),
                HashMap::new
        );
        Map<String, Object> params = null;
        try {
            params = ParseUtil.extractRouteParameters(requestData.getRoute(), requestData.getPath());
        } catch (Exception e) {
            reject(HttpCode.BAD_REQUEST);
        }
        SunHttpRequest request = new SunHttpRequest();
        request.setHeaders(exchange.getRequestHeaders());
        request.setMethod(requestData.getMethod());
        request.setQueryParameters(query);
        request.setPathParameters(params);
        requestData.setRequest(request);
        return requestData;
    }
}
