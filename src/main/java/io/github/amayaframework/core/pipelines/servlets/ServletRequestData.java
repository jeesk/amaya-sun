package io.github.amayaframework.core.pipelines.servlets;

import io.github.amayaframework.core.methods.HttpMethod;
import io.github.amayaframework.core.pipelines.RequestData;
import io.github.amayaframework.core.routers.Route;
import io.github.amayaframework.core.util.AmayaConfig;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * A simple container created to transfer data between pipeline actions.
 */
public class ServletRequestData extends RequestData {
    protected final HttpServletRequest servletRequest;
    private final InputStream inputStream;
    private final Charset charset = AmayaConfig.INSTANCE.getCharset();

    public ServletRequestData(HttpServletRequest request, HttpMethod method, Route route, String path)
            throws IOException {
        super(route, path, method);
        this.servletRequest = request;
        this.inputStream = servletRequest.getInputStream();
    }

    public ServletRequestData(HttpServletRequest request, HttpMethod method) throws IOException {
        this(request, method, null, null);
    }

    public HttpServletRequest getServletRequest() {
        return servletRequest;
    }

    @Override
    public InputStream getInputStream() {
        return inputStream;
    }

    @Override
    public String getContentType() {
        return servletRequest.getContentType();
    }

    @Override
    public Charset getCharset() {
        try {
            return Charset.forName(servletRequest.getCharacterEncoding());
        } catch (Exception e) {
            return charset;
        }
    }
}
