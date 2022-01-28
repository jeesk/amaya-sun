package io.github.amayaframework.core.pipelines.servlets;

import io.github.amayaframework.core.pipelines.PipelineAction;

import javax.servlet.http.Cookie;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>The input action during which the request cookies are parsed.</p>
 * <p>Receives: {@link ServletRequestData}</p>
 * <p>Returns: {@link ServletRequestData}</p>
 */
public class ServletParseRequestCookiesAction extends PipelineAction<ServletRequestData, ServletRequestData> {
    @Override
    public ServletRequestData apply(ServletRequestData requestData) {
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
