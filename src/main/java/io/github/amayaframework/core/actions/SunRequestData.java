package io.github.amayaframework.core.actions;

import io.github.amayaframework.core.config.ConfigProvider;
import io.github.amayaframework.core.methods.HttpMethod;
import io.github.amayaframework.core.pipeline.AbstractRequestData;
import io.github.amayaframework.core.routes.MethodRoute;
import io.github.amayaframework.core.util.ParseUtil;
import io.github.amayaframework.server.interfaces.HttpExchange;

import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * A simple container created to transfer data between input pipeline actions.
 */
public class SunRequestData extends AbstractRequestData {
    protected final HttpExchange exchange;
    private final String contentHeader;
    private final Charset charset = ConfigProvider.getConfig().getCharset();

    public SunRequestData(HttpExchange exchange, HttpMethod method, String path, MethodRoute route) {
        super(method, path, route);
        this.exchange = exchange;
        this.contentHeader = exchange.getRequestHeaders().getFirst(ParseUtil.CONTENT_HEADER);
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
        return ParseUtil.parseCharsetHeader(contentHeader.substring(position + 1), charset);
    }
}
