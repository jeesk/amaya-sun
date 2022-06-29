package io.github.amayaframework.core.sun.actions;

import com.github.romanqed.util.Handler;
import io.github.amayaframework.core.actions.WithConfig;
import io.github.amayaframework.core.config.AmayaConfig;
import io.github.amayaframework.core.contexts.FixedOutputStream;
import io.github.amayaframework.core.contexts.HttpResponse;
import io.github.amayaframework.core.pipeline.PipelineAction;
import io.github.amayaframework.core.sun.handlers.SunSession;
import io.github.amayaframework.http.ContentType;
import io.github.amayaframework.server.interfaces.HttpExchange;

import java.nio.charset.Charset;

/**
 * <p>The output action during which the response body is sent.</p>
 * <p>Receives: {@link SunResponseData}</p>
 * <p>Returns: {@link Void}</p>
 */
@WithConfig
public class ProcessBodyAction extends PipelineAction<SunResponseData, Void> {
    private final Charset charset;

    public ProcessBodyAction(AmayaConfig config) {
        this.charset = config.getCharset();
    }

    @Override
    public Void execute(SunResponseData data) throws Throwable {
        HttpExchange exchange = data.exchange;
        HttpResponse response = data.getResponse();
        ContentType type = response.getContentType();
        Handler<FixedOutputStream> handler = response.getOutputStreamHandler();
        if (handler != null) {
            FixedOutputStream stream = new SunOutputStream(exchange, response.getCode());
            handler.handle(stream);
            long remaining = stream.getRemainingLength();
            if (remaining != 0) {
                throw new IllegalStateException("Not all data has been sent, " + remaining + " bytes are left");
            }
            stream.flush();
            return null;
        }
        if (type != null && type.isString()) {
            Charset charset = response.getCharset();
            SunSession.send(exchange, charset == null ? this.charset : charset, response.getCode(), response.getBody());
            return null;
        }
        exchange.sendResponseHeaders(response.getCode(), 0);
        return null;
    }
}
