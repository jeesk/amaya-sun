package io.github.amayaframework.core.sun.handlers;

import com.github.romanqed.util.Action;
import com.github.romanqed.util.http.HttpCode;
import io.github.amayaframework.core.config.AmayaConfig;
import io.github.amayaframework.core.contexts.ContentType;
import io.github.amayaframework.core.contexts.HttpResponse;
import io.github.amayaframework.core.contexts.Responses;
import io.github.amayaframework.core.controllers.Controller;
import io.github.amayaframework.core.handlers.Session;
import io.github.amayaframework.core.methods.HttpMethod;
import io.github.amayaframework.core.routers.MethodRouter;
import io.github.amayaframework.core.routes.MethodRoute;
import io.github.amayaframework.core.sun.actions.SunRequestData;
import io.github.amayaframework.core.sun.actions.SunResponseData;
import io.github.amayaframework.core.util.IOUtil;
import io.github.amayaframework.core.util.ParseUtil;
import io.github.amayaframework.server.interfaces.HttpExchange;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.nio.charset.Charset;

public class SunSession implements Session {
    private final HttpExchange exchange;
    private final MethodRouter router;
    private final int length;
    private final AmayaConfig config;

    public SunSession(HttpExchange exchange, Controller controller, AmayaConfig config) {
        this.exchange = exchange;
        router = controller.getRouter();
        length = controller.getRoute().length();
        this.config = config;
    }

    public static void send(HttpExchange exchange, Charset charset, HttpCode code, Object body) throws IOException {
        if (body == null) {
            exchange.sendResponseHeaders(code, 0);
            return;
        }
        String stringBody = body.toString();
        exchange.sendResponseHeaders(code, stringBody.getBytes(charset).length);
        OutputStreamWriter streamWriter = new OutputStreamWriter(exchange.getResponseBody(), charset);
        BufferedWriter writer = new BufferedWriter(streamWriter);
        writer.write(stringBody);
        writer.flush();
    }

    @Override
    public HttpResponse handleInput(Action<Object, Object> handler) throws Throwable {
        HttpMethod method = HttpMethod.fromName(exchange.getRequestMethod());
        if (method == null) {
            HttpCode code = HttpCode.NOT_IMPLEMENTED;
            return Responses.responseWithCode(code, code.getMessage());
        }
        URI uri = exchange.getRequestURI();
        String path = uri.getPath().substring(length);
        path = ParseUtil.normalizeRoute(path);
        MethodRoute route = router.follow(method, path);
        if (route == null) {
            HttpCode code = HttpCode.NOT_FOUND;
            return Responses.responseWithCode(code, code.getMessage());
        }
        SunRequestData requestData = new SunRequestData(exchange, method, path, route);
        return (HttpResponse) handler.execute(requestData);
    }

    @Override
    public void handleOutput(Action<Object, Object> handler, HttpResponse response) throws Throwable {
        SunResponseData responseData = new SunResponseData(exchange, response);
        handler.execute(responseData);
    }

    @Override
    public void reject(Throwable e) throws IOException {
        HttpCode code = HttpCode.INTERNAL_SERVER_ERROR;
        String message = code.getMessage() + "\n";
        if (e != null && config.isDebug()) {
            message += IOUtil.throwableToString(e) + "\n";
            Throwable caused = e.getCause();
            if (caused != null) {
                message += "Caused by: \n" + IOUtil.throwableToString(caused);
            }
        }
        reject(code, message);
    }

    public void reject(HttpCode code) throws IOException {
        reject(code, code.getMessage());
    }

    @Override
    public void reject(HttpCode code, String message) throws IOException {
        Charset charset = config.getCharset();
        String header = IOUtil.generateContentHeader(ContentType.PLAIN, charset);
        exchange.getResponseHeaders().set(ParseUtil.CONTENT_HEADER, header);
        String toSend;
        if (message != null) {
            toSend = message;
        } else {
            toSend = code.getMessage();
        }
        send(exchange, charset, code, toSend);
    }
}
