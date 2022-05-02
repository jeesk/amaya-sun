package io.github.amayaframework.core.sun.actions;

import io.github.amayaframework.core.pipeline.AbstractRequestData;
import io.github.amayaframework.core.pipeline.RouteData;
import io.github.amayaframework.http.HttpUtil;
import io.github.amayaframework.server.interfaces.HttpExchange;

import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * A simple container created to transfer data between input pipeline actions.
 */
public class SunRequestData extends AbstractRequestData {
    protected final HttpExchange exchange;
    private final String contentHeader;
    private final Charset charset;

    public SunRequestData(HttpExchange exchange, RouteData data, Charset charset) {
        super(data);
        this.exchange = exchange;
        this.contentHeader = exchange.getRequestHeaders().getFirst(HttpUtil.CONTENT_HEADER);
        this.charset = charset;
    }

    public HttpExchange getExchange() {
        return exchange;
    }

    @Override
    public InputStream getInputStream() {
        return exchange.getRequestBody();
    }

    @Override
    public String getContentType() {
        if (contentHeader == null) {
            return null;
        }
        int position = contentHeader.indexOf(';');
        if (position < 0) {
            return contentHeader;
        }
        return contentHeader.substring(0, position);
    }

    @Override
    public Charset getCharset() {
        if (contentHeader == null) {
            return charset;
        }
        int position = contentHeader.indexOf(';');
        if (position < 0) {
            return charset;
        }
        return HttpUtil.parseCharsetHeader(contentHeader.substring(position + 1), charset);
    }
}
