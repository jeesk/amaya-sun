package io.github.amayaframework.core.sun.actions;

import io.github.amayaframework.core.actions.WithConfig;
import io.github.amayaframework.core.config.AmayaConfig;
import io.github.amayaframework.core.contexts.HttpResponse;
import io.github.amayaframework.core.pipeline.PipelineAction;
import io.github.amayaframework.core.util.IOUtil;
import io.github.amayaframework.http.ContentType;
import io.github.amayaframework.http.HeaderMap;
import io.github.amayaframework.http.HttpUtil;
import io.github.amayaframework.server.interfaces.HttpExchange;

import javax.servlet.http.Cookie;
import java.nio.charset.Charset;
import java.util.Collection;

/**
 * <p>The output action during which the response headers is sent.</p>
 * <p>Receives: {@link SunResponseData}</p>
 * <p>Returns: {@link SunResponseData}</p>
 */
@WithConfig
public class ProcessHeadersAction extends PipelineAction<SunResponseData, SunResponseData> {
    private final Charset charset;

    public ProcessHeadersAction(AmayaConfig config) {
        this.charset = config.getCharset();
    }

    @Override
    public SunResponseData execute(SunResponseData data) {
        HttpExchange exchange = data.exchange;
        HttpResponse response = data.getResponse();
        HeaderMap headers = exchange.getResponseHeaders();
        headers.putAll(response.getHeaderMap());
        ContentType type = response.getContentType();
        if (response.getBody() != null || (type != null && !type.isString())) {
            headers.set(HttpUtil.CONTENT_HEADER, HttpUtil.generateContentHeader(type, charset));
        }
        Collection<Cookie> cookies = response.getCookies();
        for (Cookie cookie : cookies) {
            headers.add(HttpUtil.SET_COOKIE_HEADER, IOUtil.cookieToHeader(cookie));
        }
        data.complete();
        return data;
    }
}
