package io.github.amayaframework.core.handlers;

import io.github.amayaframework.core.configurators.BaseServletConfigurator;
import io.github.amayaframework.core.contexts.HttpResponse;
import io.github.amayaframework.core.controllers.Controller;
import io.github.amayaframework.core.methods.HttpMethod;
import io.github.amayaframework.core.pipelines.servlets.RequestData;
import io.github.amayaframework.core.util.AmayaConfig;
import io.github.amayaframework.server.utils.HttpCode;

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

    protected void doMethod(HttpMethod method, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        RequestData requestData = new RequestData(req, method);
        HttpResponse response = (HttpResponse) handler.process(requestData).getResult();
        String body;
        try {
            body = (String) response.getBody();
        } catch (Exception e) {
            HttpCode code = HttpCode.INTERNAL_SERVER_ERROR;
            resp.sendError(code.getCode(), code.getMessage());
            return;
        }
        response.getHeaderMap().forEach((key, value) -> value.forEach(e -> resp.addHeader(key, e)));
        resp.setStatus(response.getCode().getCode());
        BufferedWriter writer = wrapOutputStream(resp.getOutputStream());
        if (body != null) {
            writer.write(body);
        }
        writer.close();
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
}
