package io.github.amayaframework.core.pipelines;

import io.github.amayaframework.core.methods.HttpMethod;
import io.github.amayaframework.core.routes.MethodRoute;
import io.github.amayaframework.core.util.AmayaConfig;
import io.github.amayaframework.core.util.ParseUtil;
import io.github.amayaframework.server.interfaces.HttpExchange;

import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * A simple container created to transfer data between pipeline actions.
 */
public class SunRequestData extends RequestData {
    protected final HttpExchange exchange;
    private final String contentHeader;
    private final Charset charset = AmayaConfig.INSTANCE.getCharset();

    public SunRequestData(HttpExchange exchange, MethodRoute route, String path, HttpMethod method) {
        super(route, path, method);
        this.exchange = exchange;
        this.contentHeader = exchange.getRequestHeaders().getFirst(ParseUtil.CONTENT_HEADER);
    }

    public SunRequestData(HttpExchange exchange) {
        this(exchange, null, null, null);
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
        int position = contentHeader.indexOf(';');
        if (position < 0) {
            return charset;
        }
        return ParseUtil.parseCharsetHeader(contentHeader.substring(position + 1), charset);
    }
}
