package io.github.amayaframework.core.pipelines.servlets;

import io.github.amayaframework.core.pipelines.PipelineAction;

import javax.servlet.http.Cookie;
import java.util.HashMap;
import java.util.Map;

public class ServletsParseRequestCookiesAction extends PipelineAction<RequestData, RequestData> {
    @Override
    public RequestData apply(RequestData requestData) {
        Cookie[] cookies = requestData.servletRequest.getCookies();
        Map<String, Cookie> toAdd = new HashMap<>();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                toAdd.put(cookie.getName(), cookie);
            }
        }
        requestData.getRequest().setCookies(toAdd);
        return requestData;
    }
}
