package io.github.amayaframework.core.sun.actions;

import com.github.romanqed.jutils.http.HeaderMap;
import io.github.amayaframework.core.config.ConfigProvider;
import io.github.amayaframework.core.contexts.ContentType;
import io.github.amayaframework.core.contexts.HttpResponse;
import io.github.amayaframework.core.pipeline.PipelineAction;
import io.github.amayaframework.core.util.ParseUtil;
import io.github.amayaframework.server.interfaces.HttpExchange;

import javax.servlet.http.Cookie;
import java.nio.charset.Charset;
import java.util.Collection;

/**
 * <p>The output action during which the response headers is sent.</p>
 * <p>Receives: {@link SunResponseData}</p>
 * <p>Returns: {@link SunResponseData}</p>
 */
public class ProcessHeadersAction extends PipelineAction<SunResponseData, SunResponseData> {
    private final Charset charset = ConfigProvider.getConfig().getCharset();

    @Override
    public SunResponseData execute(SunResponseData data) {
        HttpExchange exchange = data.exchange;
        HttpResponse response = data.getResponse();
        HeaderMap headers = exchange.getResponseHeaders();
        headers.putAll(response.getHeaderMap());
        ContentType type = response.getContentType();
        if (response.getBody() != null || (type != null && !type.isString())) {
            headers.set(ParseUtil.CONTENT_HEADER, ParseUtil.generateContentHeader(type, charset));
        }
        Collection<Cookie> cookies = response.getCookies();
        for (Cookie cookie : cookies) {
            headers.add(ParseUtil.SET_COOKIE_HEADER, ParseUtil.cookieToHeader(cookie));
        }
        return data;
    }
}
