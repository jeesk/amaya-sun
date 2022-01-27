package io.github.amayaframework.core.pipelines.servlets;

import com.github.romanqed.jutils.util.Checks;
import io.github.amayaframework.core.contexts.ServletHttpRequest;
import io.github.amayaframework.core.pipelines.PipelineAction;
import io.github.amayaframework.core.util.AmayaConfig;
import io.github.amayaframework.core.util.ParseUtil;
import io.github.amayaframework.server.utils.HttpCode;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
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
public class ServletParseRequestAction extends PipelineAction<RequestData, RequestData> {
    private final Charset charset = AmayaConfig.INSTANCE.getCharset();

    @Override
    public RequestData apply(RequestData requestData) {
        HttpServletRequest servletRequest = requestData.servletRequest;
        Map<String, List<String>> query = Checks.requireNonException(
                () -> ParseUtil.parseQueryString(servletRequest.getQueryString()),
                HashMap::new
        );
        Map<String, Object> params = null;
        try {
            params = ParseUtil.extractRouteParameters(requestData.getRoute(), requestData.getPath());
        } catch (Exception e) {
            reject(HttpCode.BAD_REQUEST);
        }
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(servletRequest.getInputStream(), charset));
        } catch (IOException e) {
            reject(HttpCode.INTERNAL_SERVER_ERROR);
        }
        String body = null;
        if (reader != null) {
            body = reader.lines().reduce("", (left, right) -> left + right + "\n");
        }
        ServletHttpRequest request = new ServletHttpRequest(servletRequest);
        request.setMethod(requestData.getMethod());
        request.setQueryParameters(query);
        request.setPathParameters(params);
        request.setBody(body);
        requestData.setRequest(request);
        return requestData;
    }
}
