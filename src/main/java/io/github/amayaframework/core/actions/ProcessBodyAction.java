package io.github.amayaframework.core.actions;

import io.github.amayaframework.core.config.ConfigProvider;
import io.github.amayaframework.core.contexts.ContentType;
import io.github.amayaframework.core.contexts.HttpResponse;
import io.github.amayaframework.core.contexts.StreamHandler;
import io.github.amayaframework.core.handlers.SunSession;
import io.github.amayaframework.core.pipeline.PipelineAction;
import io.github.amayaframework.server.interfaces.HttpExchange;

import java.nio.charset.Charset;

/**
 * <p>The output action during which the response body is sent.</p>
 * <p>Receives: {@link SunResponseData}</p>
 * <p>Returns: {@link Void}</p>
 */
public class ProcessBodyAction extends PipelineAction<SunResponseData, Void> {
    private final Charset charset = ConfigProvider.getConfig().getCharset();

    @Override
    public Void execute(SunResponseData responseData) throws Exception {
        HttpExchange exchange = responseData.exchange;
        HttpResponse response = responseData.response;
        ContentType type = response.getContentType();
        StreamHandler handler = response.getOutputStreamHandler();
        if (handler != null) {
            handler.handle(exchange.getResponseBody());
            exchange.sendResponseHeaders(response.getCode(), handler.getContentLength());
            handler.flush();
            return null;
        }
        if (type != null && type.isString()) {
            SunSession.send(exchange, charset, response.getCode(), response.getBody());
            return null;
        }
        exchange.sendResponseHeaders(response.getCode(), 0);
        return null;
    }
}
