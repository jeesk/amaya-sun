package io.github.amayaframework.core.handlers;

import com.github.romanqed.jutils.http.HttpCode;
import io.github.amayaframework.core.configurators.BaseSunConfigurator;
import io.github.amayaframework.core.contexts.ContentType;
import io.github.amayaframework.core.contexts.HttpResponse;
import io.github.amayaframework.core.contexts.StreamHandler;
import io.github.amayaframework.core.controllers.Controller;
import io.github.amayaframework.core.pipelines.SunRequestData;
import io.github.amayaframework.core.util.AmayaConfig;
import io.github.amayaframework.core.util.ParseUtil;
import io.github.amayaframework.server.interfaces.HttpExchange;
import io.github.amayaframework.server.interfaces.HttpHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.Collections;

/**
 * <p>A class representing the sun handler used inside the server. Built on pipelines.</p>
 * <p>The http connection processing scheme is quite simple: first, the input pipeline is triggered,
 * resulting in an HttpResponse. Then the output pipeline is triggered, the purpose of which is to
 * process and verify the received HttpResponse. After that, the server receives a response.</p>
 */
public class SunHandler implements HttpHandler {
    private static final Logger logger = LoggerFactory.getLogger(SunHandler.class);
    private final Charset charset = AmayaConfig.INSTANCE.getCharset();
    private final IOHandler handler;

    public SunHandler(Controller controller) {
        handler = new BaseIOHandler(controller, Collections.singletonList(new BaseSunConfigurator()));
    }

    protected void reject(HttpExchange exchange, Exception e) throws IOException {
        logger.error("Internal server error", e);
        exchange.getResponseHeaders().set(ParseUtil.CONTENT_HEADER, ContentType.PLAIN.getHeader());
        sendAnswer(exchange, HttpCode.INTERNAL_SERVER_ERROR, HttpCode.INTERNAL_SERVER_ERROR.getMessage());
    }

    protected void sendAnswer(HttpExchange exchange, HttpCode code, Object body) throws IOException {
        if (body == null) {
            exchange.sendResponseHeaders(code, 0);
            exchange.close();
            return;
        }
        String stringBody = body.toString();
        exchange.sendResponseHeaders(code, stringBody.getBytes(charset).length);
        BufferedWriter writer = wrapOutputStream(exchange.getResponseBody());
        writer.write(stringBody);
        writer.flush();
        exchange.close();
    }

    protected BufferedWriter wrapOutputStream(OutputStream stream) {
        return new BufferedWriter(new OutputStreamWriter(stream, charset));
    }

    public IOHandler getHandler() {
        return handler;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        SunRequestData requestData = new SunRequestData(exchange);
        HttpResponse response;
        try {
            response = (HttpResponse) handler.process(requestData).getResult();
        } catch (Exception e) {
            reject(exchange, e);
            return;
        }
        if (response == null) {
            reject(exchange, null);
            return;
        }
        ContentType type = response.getContentType();
        if (type != null && type.isString()) {
            Object body = response.getBody();
            exchange.getResponseHeaders().putAll(response.getHeaderMap());
            sendAnswer(exchange, response.getCode(), body);
            return;
        }
        StreamHandler handler = response.getOutputStreamHandler();
        if (handler == null) {
            exchange.getResponseHeaders().putAll(response.getHeaderMap());
            sendAnswer(exchange, response.getCode(), null);
            return;
        }
        handler.accept(exchange.getResponseBody());
        if (!handler.isSuccessful()) {
            reject(exchange, handler.getException());
            return;
        }
        exchange.getResponseHeaders().putAll(response.getHeaderMap());
        exchange.sendResponseHeaders(response.getCode(), handler.getContentLength());
        handler.flush();
        exchange.close();
    }
}
