package io.github.amayaframework.core.pipelines;

import com.github.romanqed.jutils.util.Checks;
import io.github.amayaframework.core.util.ParseUtil;

import java.net.HttpCookie;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>The input action during which the request cookies are parsed.</p>
 * <p>Receives: {@link RequestData}</p>
 * <p>Returns: {@link RequestData}</p>
 */
public class ParseRequestCookiesAction extends PipelineAction<RequestData, RequestData> {
    private static final String COOKIE_HEADER = "Cookie";

    @Override
    public RequestData apply(RequestData requestData) {
        String header = requestData.request.getHeaders().getFirst(COOKIE_HEADER);
        if (header == null) {
            return requestData;
        }
        Map<String, HttpCookie> cookies = Checks.requireNonException(
                () -> ParseUtil.parseCookieHeader(header),
                HashMap::new
        );
        requestData.request.setCookies(Collections.unmodifiableMap(cookies));
        return requestData;
    }
}
