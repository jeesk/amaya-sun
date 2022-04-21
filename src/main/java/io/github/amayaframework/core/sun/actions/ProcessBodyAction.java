package io.github.amayaframework.core.sun.actions;

import com.github.romanqed.jutils.lambdas.Handler;
import io.github.amayaframework.core.ConfigProvider;
import io.github.amayaframework.core.contexts.ContentType;
import io.github.amayaframework.core.contexts.FixedOutputStream;
import io.github.amayaframework.core.contexts.HttpResponse;
import io.github.amayaframework.core.pipeline.PipelineAction;
import io.github.amayaframework.core.sun.handlers.SunSession;
import io.github.amayaframework.server.interfaces.HttpExchange;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * <p>The output action during which the response body is sent.</p>
 * <p>Receives: {@link SunResponseData}</p>
 * <p>Returns: {@link Void}</p>
 */
public class ProcessBodyAction extends PipelineAction<SunResponseData, Void> {
    private final Charset charset = ConfigProvider.getConfig().getCharset();

    @Override
    public Void execute(SunResponseData responseData) throws Throwable {
        HttpExchange exchange = responseData.exchange;
        HttpResponse response = responseData.getResponse();
        ContentType type = response.getContentType();
        Handler<FixedOutputStream> handler = response.getOutputStreamHandler();
        if (handler != null) {
            FixedOutputStream outputStream = new FixedOutputStream(exchange.getResponseBody()) {
                @Override
                public void specifyLength(long length) throws IOException {
                    exchange.sendResponseHeaders(response.getCode(), length);
                }
            };
            handler.handle(outputStream);
            outputStream.flush();
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
