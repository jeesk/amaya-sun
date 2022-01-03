package io.github.amayaframework.core.contexts;

import io.github.amayaframework.core.wrapping.AbstractViewable;
import io.github.amayaframework.server.utils.HeaderMap;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class HttpRequest extends AbstractViewable implements HttpTransaction {
    private HeaderMap headers;
    private Map<String, List<String>> queryParameters;
    private Map<String, Object> pathParameters;
    private Object body;

    public Map<String, List<String>> getQueryParameters() {
        return queryParameters;
    }

    public List<String> getQueryParameter(String name) {
        return queryParameters.get(name);
    }

    public String getFirstQueryParameter(String name) {
        List<String> parameter = queryParameters.get(name);
        if (parameter == null || parameter.size() == 0) {
            return null;
        }
        return parameter.get(0);
    }

    public Map<String, Object> getPathParameters() {
        return pathParameters;
    }

    @SuppressWarnings("unchecked")
    public <T> T getPathParameter(String name) {
        try {
            return (T) pathParameters.get(name);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Object getBody() {
        return body;
    }

    @Override
    public HeaderMap getHeaders() {
        return headers;
    }

    public static class Builder {
        private HttpRequest request;

        public Builder() {
            request = new HttpRequest();
        }

        public Builder headers(HeaderMap headers) {
            request.headers = Objects.requireNonNull(headers);
            return this;
        }

        public Builder queryParameters(Map<String, List<String>> queryParameters) {
            request.queryParameters = Objects.requireNonNull(queryParameters);
            request.put("query", request.queryParameters);
            return this;
        }

        public Builder pathParameters(Map<String, Object> pathParameters) {
            request.pathParameters = Objects.requireNonNull(pathParameters);
            request.put("path", request.pathParameters);
            return this;
        }

        public Builder body(Object body) {
            request.body = Objects.requireNonNull(body);
            request.put("body", request.body);
            return this;
        }

        public HttpRequest build() {
            HttpRequest ret = request;
            request = null;
            return ret;
        }
    }
}
