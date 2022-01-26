package io.github.amayaframework.core.pipelines.servlets;

import com.github.romanqed.jutils.util.Checks;
import io.github.amayaframework.core.contexts.ServletHttpRequest;
import io.github.amayaframework.core.pipelines.PipelineAction;
import io.github.amayaframework.core.util.ParseUtil;
import io.github.amayaframework.server.utils.HttpCode;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServletsParseRequestAction extends PipelineAction<RequestData, RequestData> {
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
        String body = null;
        try {
            body = servletRequest.getReader().lines().reduce("", (left, right) -> left + right + "\n");
        } catch (IOException e) {
            reject(HttpCode.INTERNAL_SERVER_ERROR);
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
