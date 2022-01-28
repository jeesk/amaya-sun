package io.github.amayaframework.core.pipelines.sun;

import com.github.romanqed.jutils.util.Checks;
import io.github.amayaframework.core.pipelines.PipelineAction;
import io.github.amayaframework.core.util.ParseUtil;

import javax.servlet.http.Cookie;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>The input action during which the request cookies are parsed.</p>
 * <p>Receives: {@link SunRequestData}</p>
 * <p>Returns: {@link SunRequestData}</p>
 */
public class SunParseRequestCookiesAction extends PipelineAction<SunRequestData, SunRequestData> {
    private static final String COOKIE_HEADER = "Cookie";

    @Override
    public SunRequestData apply(SunRequestData requestData) {
        String header = requestData.getRequest().getHeader(COOKIE_HEADER);
        if (header == null) {
            return requestData;
        }
        Map<String, Cookie> cookies = Checks.requireNonException(
                () -> ParseUtil.parseCookieHeader(header),
                HashMap::new
        );
        requestData.getRequest().setCookies(Collections.unmodifiableMap(cookies));
        return requestData;
    }
}
