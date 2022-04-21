package io.github.amayaframework.core.sun.actions;

import com.github.romanqed.jutils.http.HttpCode;
import com.github.romanqed.jutils.lambdas.Action;
import com.github.romanqed.jutils.lambdas.MetaLambdas;
import com.github.romanqed.jutils.util.Checks;
import io.github.amayaframework.core.contexts.HttpRequest;
import io.github.amayaframework.core.pipeline.InputAction;
import io.github.amayaframework.core.scanners.RequestScanner;
import io.github.amayaframework.core.scanners.Scanner;
import io.github.amayaframework.core.sun.contexts.SunHttpRequest;
import io.github.amayaframework.core.util.ParseUtil;
import io.github.amayaframework.server.interfaces.HttpExchange;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>The action during which the basic components of the request will be checked and parsed:
 * query parameters, path parameters, headers and the request body.</p>
 * <p>Receives: {@link SunRequestData}</p>
 * <p>Returns: {@link SunRequestData}</p>
 */
public class ParseRequestAction extends InputAction<SunRequestData, SunRequestData> {
    private static final Action<HttpRequest, HttpRequest> REQUEST_PROXY;

    static {
        Scanner<Class<HttpRequest>> scanner = new RequestScanner();
        Class<HttpRequest> found = scanner.safetyFind();
        if (found == null) {
            REQUEST_PROXY = Action.identity();
        } else {
            try {
                REQUEST_PROXY = MetaLambdas.packConstructor(found, HttpRequest.class);
            } catch (Throwable e) {
                throw new IllegalStateException("Wrapper error found HttpRequest", e);
            }
        }
    }

    @Override
    public SunRequestData execute(SunRequestData data) throws Throwable {
        HttpExchange exchange = data.exchange;
        Map<String, List<String>> query = Checks.requireNonException(
                () -> ParseUtil.parseQueryString(exchange.getRequestURI().getQuery(), data.getCharset()),
                HashMap::new
        );
        Map<String, Object> params = null;
        try {
            params = ParseUtil.extractRouteParameters(data.getRoute(), data.getPath());
        } catch (Exception e) {
            reject(HttpCode.BAD_REQUEST);
        }
        SunHttpRequest request = new SunHttpRequest();
        request.setHeaders(exchange.getRequestHeaders());
        request.setQuery(query);
        request.setPathParameters(params);
        data.setRequest(REQUEST_PROXY.execute(request));
        return data;
    }
}
