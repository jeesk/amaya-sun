package io.github.amayaframework.core.util;

import io.github.amayaframework.core.routers.InvalidFormatException;
import io.github.amayaframework.core.routers.Route;
import io.github.amayaframework.core.scanners.FilterScanner;
import com.github.amayaframework.filters.ContentFilter;
import com.github.amayaframework.filters.StringFilter;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParseUtil {
    public static final Map<String, StringFilter> PARAM_FILTERS;
    public static final Map<String, ContentFilter> CONTENT_FILTERS;
    public static final Pattern ROUTE = Pattern.compile("(?:/[^\\s/]+)+");
    private static final String PARAM_DELIMITER = ":";
    private static final String URL_ENCODING = "utf-8";
    private static final Pattern QUERY_VALIDATOR = Pattern.compile("^(?:[^&]+=[^&]+(?:&|$))+$");
    private static final Pattern QUERY = Pattern.compile("([^&]+)=([^&]+)");

    static {
        try {
            PARAM_FILTERS = new FilterScanner<>(StringFilter.class).find();
        } catch (Exception e) {
            throw new IllegalStateException("Exception when scanning parameter filters!", e);
        }
        try {
            CONTENT_FILTERS = new FilterScanner<>(ContentFilter.class).find();
        } catch (Exception e) {
            throw new IllegalStateException("Exception when scanning content filters!", e);
        }
    }

    public static Variable<String, StringFilter> parseRouteParameter(String source) {
        Objects.requireNonNull(source);
        String[] split = source.split(PARAM_DELIMITER);
        if (split.length < 1 || split.length > 2) {
            throw new InvalidFormatException("Invalid parameter \"" + source + "\"");
        }
        if (split.length == 1) {
            return new Variable<>(split[0], null);
        }
        return new Variable<>(split[0], PARAM_FILTERS.get(split[1]));
    }

    public static Map<String, Object> extractRouteParameters(Route route, String source) {
        Map<String, Object> ret = new ConcurrentHashMap<>();
        if (!route.isRegexp()) {
            return ret;
        }
        Matcher finder = route.pattern().matcher(source);
        Iterator<Variable<String, StringFilter>> parameters = route.parameters().iterator();
        if (!finder.find()) {
            return null;
        }
        Variable<String, StringFilter> next;
        for (int i = 1; i <= finder.groupCount(); ++i) {
            next = parameters.next();
            if (next.getValue() != null) {
                ret.put(next.getKey(), next.getValue().transform(finder.group(i)));
            } else {
                ret.put(next.getKey(), finder.group(i));
            }
        }
        return ret;
    }

    public static Map<String, List<String>> parseQueryString(String source) throws UnsupportedEncodingException {
        Map<String, List<String>> ret = new ConcurrentHashMap<>();
        if (source == null || source.isEmpty()) {
            return ret;
        }
        if (!QUERY_VALIDATOR.matcher(source).matches()) {
            throw new InvalidFormatException("Invalid query string format!");
        }
        Matcher matcher = QUERY.matcher(source);
        while (matcher.find()) {
            String value = URLDecoder.decode(matcher.group(2), URL_ENCODING);
            ret.computeIfAbsent(matcher.group(1), key -> new ArrayList<>()).add(value);
        }
        return ret;
    }
}
