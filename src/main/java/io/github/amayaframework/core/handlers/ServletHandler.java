package io.github.amayaframework.core.handlers;

import io.github.amayaframework.core.configurators.BaseServletConfigurator;
import io.github.amayaframework.core.contexts.HttpResponse;
import io.github.amayaframework.core.controllers.Controller;
import io.github.amayaframework.core.methods.HttpMethod;
import io.github.amayaframework.core.pipelines.servlets.RequestData;
import io.github.amayaframework.server.utils.HttpCode;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

public class ServletHandler extends HttpServlet {
    private final IOHandler handler;

    public ServletHandler(Controller controller) {
        handler = new BaseIOHandler(controller, Collections.singletonList(new BaseServletConfigurator()));
    }

    public IOHandler getHandler() {
        return handler;
    }

    protected void doMethod(HttpMethod method, HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
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
        resp.setStatus(HttpCode.OK.getCode());
        resp.getWriter().write(body);
        resp.getWriter().close();
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
