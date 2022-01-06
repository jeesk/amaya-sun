package io.github.amayaframework.core.contexts;

import io.github.amayaframework.server.utils.HeaderMap;
import io.github.amayaframework.server.utils.HttpCode;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * A class representing a http response. Inherited from {@link HttpTransaction}.
 */
public class HttpResponse extends AbstractHttpTransaction {
    private HttpCode code;

    /**
     * Creates HttpResponse with code and header map
     *
     * @param code    {@link HttpCode} will be set
     * @param headers {@link HeaderMap} map will be set as internal
     */
    public HttpResponse(HttpCode code, HeaderMap headers) {
        this.code = Objects.requireNonNull(code);
        this.headers = Objects.requireNonNull(headers);
    }

    /**
     * Creates HttpResponse with code and empty header map
     *
     * @param code {@link HttpCode} will be set
     */
    public HttpResponse(HttpCode code) {
        this(code, new HeaderMap());
    }

    /**
     * Creates HttpResponse with {@link HttpCode#OK} code and empty header map
     */
    public HttpResponse() {
        this(HttpCode.OK, new HeaderMap());
    }

    /**
     * Returns response code
     *
     * @return {@link HttpCode}
     */
    public HttpCode getCode() {
        return code;
    }

    /**
     * Set response code, if null is passed instead of the code, it throws {@link NullPointerException}
     *
     * @param code {@link HttpCode} will be set
     */
    public void setCode(HttpCode code) {
        this.code = Objects.requireNonNull(code);
    }

    /**
     * Set header in internal header map, if null is passed instead of the value, it throws {@link NullPointerException}
     *
     * @param key   {@link String} header name
     * @param value {@link List} header values
     */
    public void setHeader(String key, List<String> value) {
        Objects.requireNonNull(value);
        headers.put(key, value);
    }

    /**
     * Set header in internal header map, if null is passed instead of the value, it throws {@link NullPointerException}
     *
     * @param key   {@link String} header name
     * @param value {@link String} header value
     */
    public void setHeader(String key, String value) {
        Objects.requireNonNull(value);
        setHeader(key, Collections.singletonList(value));
    }

    /**
     * Remove header from internal header map
     *
     * @param key {@link String} header name
     */
    public List<String> removeHeader(String key) {
        return headers.remove(key);
    }
}
