package io.github.amayaframework.core.handlers;

import io.github.amayaframework.core.configurators.BaseServletConfigurator;
import io.github.amayaframework.core.contexts.ContentType;
import io.github.amayaframework.core.contexts.HttpResponse;
import io.github.amayaframework.core.contexts.StreamHandler;
import io.github.amayaframework.core.controllers.Controller;
import io.github.amayaframework.core.methods.HttpMethod;
import io.github.amayaframework.core.pipelines.servlets.ServletRequestData;
import io.github.amayaframework.core.util.AmayaConfig;
import io.github.amayaframework.server.utils.HttpCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.Collections;

/**
 * <p>A class representing the servlet handler used inside the server. Built on pipelines.</p>
 * <p>The http connection processing scheme is quite simple: first, the input pipeline is triggered,
 * resulting in an HttpResponse. Then the output pipeline is triggered, the purpose of which is to
 * process and verify the received HttpResponse. After that, the server receives a response.</p>
 */
public class ServletHandler extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(ServletHandler.class);
    private final Charset charset = AmayaConfig.INSTANCE.getCharset();
    private final IOHandler handler;

    public ServletHandler(Controller controller) {
        handler = new BaseIOHandler(controller, Collections.singletonList(new BaseServletConfigurator()));
    }

    public IOHandler getHandler() {
        return handler;
    }

    protected BufferedWriter wrapOutputStream(OutputStream stream) {
        return new BufferedWriter(new OutputStreamWriter(stream, charset));
    }

    protected void reject(HttpServletResponse response, Exception e) throws IOException {
        logger.error("Internal server error", e);
        response.setContentType(ContentType.PLAIN.getHeader());
        response.sendError(HttpCode.INTERNAL_SERVER_ERROR.getCode(), HttpCode.INTERNAL_SERVER_ERROR.getMessage());
    }

    protected void doMethod(HttpMethod method, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ServletRequestData requestData = new ServletRequestData(req, method);
        HttpResponse response;
        try {
            response = (HttpResponse) handler.process(requestData).getResult();
        } catch (Exception e) {
            reject(resp, e);
            return;
        }
        if (response == null) {
            reject(resp, null);
            return;
        }
        response.getHeaderMap().forEach((key, value) -> value.forEach(e -> resp.addHeader(key, e)));
        resp.setStatus(response.getCode().getCode());
        ContentType type = response.getContentType();
        if (type != null && type.isString()) {
            BufferedWriter writer = wrapOutputStream(resp.getOutputStream());
            Object body = response.getBody();
            if (body != null) {
                writer.write(body.toString());
            }
            writer.close();
            return;
        }
        StreamHandler handler = response.getOutputStreamHandler();
        if (handler == null) {
            return;
        }
        handler.accept(resp.getOutputStream());
        if (!handler.isSuccessful()) {
            reject(resp, handler.getException());
            return;
        }
        resp.setContentLength(handler.getContentLength());
        handler.flush();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        doMethod(HttpMethod.GET, req, resp);
    }

    @Override
    protected void doHead(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        doMethod(HttpMethod.HEAD, req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        doMethod(HttpMethod.POST, req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        doMethod(HttpMethod.PUT, req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        doMethod(HttpMethod.DELETE, req, resp);
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        doMethod(HttpMethod.OPTIONS, req, resp);
    }
}
