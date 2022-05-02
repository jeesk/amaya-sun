package io.github.amayaframework.core.sun.actions;

import com.github.romanqed.util.Checks;
import io.github.amayaframework.core.pipeline.InputAction;
import io.github.amayaframework.core.util.IOUtil;
import io.github.amayaframework.http.HttpUtil;

import javax.servlet.http.Cookie;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>The action during which the request cookies are parsed.</p>
 * <p>Receives: {@link SunRequestData}</p>
 * <p>Returns: {@link SunRequestData}</p>
 */
public class ParseRequestCookiesAction extends InputAction<SunRequestData, SunRequestData> {
    @Override
    public SunRequestData execute(SunRequestData data) {
        String header = data.getRequest().getHeader(HttpUtil.COOKIE_HEADER);
        if (header == null) {
            data.getRequest().setCookies(Collections.unmodifiableMap(new HashMap<>()));
            return data;
        }
        Map<String, Cookie> cookies = Checks.safetyCall(
                () -> IOUtil.readCookieHeader(header),
                () -> new HashMap<>()
        );
        data.getRequest().setCookies(Collections.unmodifiableMap(cookies));
        return data;
    }
}
