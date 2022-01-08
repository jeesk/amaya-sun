package io.github.amayaframework.core.pipelines;

import io.github.amayaframework.core.contexts.HttpResponse;
import io.github.amayaframework.core.util.ParseUtil;
import io.github.amayaframework.server.utils.HeaderMap;

import java.net.HttpCookie;
import java.util.Collection;

/**
 * <p>An output action during which all cookies added by the user are converted into headers.</p>
 * <p>Receives: {@link HttpResponse}</p>
 * <p>Returns: {@link HttpResponse}</p>
 */
public class ParseResponseCookiesAction extends PipelineAction<HttpResponse, HttpResponse> {
    private static final String COOKIE_HEADER = "Set-Cookie";

    @Override
    public HttpResponse apply(HttpResponse response) {
        Collection<HttpCookie> cookies = response.getCookies();
        if (cookies.isEmpty()) {
            return response;
        }
        HeaderMap headers = response.getHeaders();
        for (HttpCookie cookie : cookies) {
            headers.add(COOKIE_HEADER, ParseUtil.cookieToHeader(cookie));
        }
        return response;
    }
}
