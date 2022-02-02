package io.github.amayaframework.core.handlers;

import com.github.romanqed.jutils.http.HttpCode;
import io.github.amayaframework.core.contexts.ContentType;
import io.github.amayaframework.core.contexts.HttpResponse;
import io.github.amayaframework.core.contexts.StreamHandler;
import io.github.amayaframework.core.util.ParseUtil;
import io.github.amayaframework.server.interfaces.HttpExchange;
import org.slf4j.Logger;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;

class SunWrapper extends AbstractSourceWrapper<HttpExchange> {
    SunWrapper(Logger logger, Charset charset) {
        super(logger, charset);
    }

    private void sendResponse(HttpExchange exchange, HttpCode code, Object body) throws IOException {
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

    @Override
    public void sendStringResponse(HttpExchange source, HttpResponse response) throws IOException {
        source.getResponseHeaders().putAll(response.getHeaderMap());
        sendResponse(source, response.getCode(), response.getBody());
    }

    @Override
    public void sendStreamResponse(HttpExchange source, HttpResponse response) throws IOException {
        StreamHandler handler = response.getOutputStreamHandler();
        if (handler == null) {
            source.getResponseHeaders().putAll(response.getHeaderMap());
            source.sendResponseHeaders(response.getCode(), 0);
            source.close();
            return;
        }
        handler.accept(source.getResponseBody());
        if (!handler.isSuccessful()) {
            reject(source, handler.getException());
            return;
        }
        source.getResponseHeaders().putAll(response.getHeaderMap());
        source.sendResponseHeaders(response.getCode(), handler.getContentLength());
        handler.flush();
        source.close();
    }

    @Override
    public void reject(HttpExchange source, HttpCode code, String message) throws IOException {
        String header = ParseUtil.generateContentHeader(ContentType.PLAIN, charset);
        source.getResponseHeaders().set(ParseUtil.CONTENT_HEADER, header);
        String toSend;
        if (message != null) {
            toSend = message;
        } else {
            toSend = code.getMessage();
        }
        sendResponse(source, code, toSend);
    }
}
