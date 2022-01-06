package io.github.amayaframework.core.controllers;

import io.github.amayaframework.core.methods.HttpMethod;
import io.github.amayaframework.core.routers.DuplicateException;
import io.github.amayaframework.core.routers.Route;
import io.github.amayaframework.core.routers.Router;
import io.github.amayaframework.core.scanners.RouteScanner;
import io.github.amayaframework.core.util.AmayaConfig;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * <p>An abstract class that describes some necessary implementations for the
 * correct functioning of user controllers.</p>
 * <p>Automatically creates an internal router and scans the methods.</p>
 * <p>All user controllers should inherit from it.</p>
 */
public abstract class AbstractController implements Controller {
    private static final String DUPLICATE_PATTERN = "Method %s with path \"%s\" at controller %s";
    private final Router router;
    private String route;

    public AbstractController() {
        router = AmayaConfig.INSTANCE.getRouter();
        RouteScanner scanner = new RouteScanner(this, AmayaConfig.INSTANCE.getRoutePacker());
        Map<HttpMethod, List<Route>> found;
        try {
            found = scanner.find();
        } catch (Exception e) {
            throw new IllegalStateException("Exception when scanning routes at " + getClass().getSimpleName(), e);
        }
        found.forEach((method, routes) -> routes.forEach(route -> {
            try {
                router.addRoute(method, route);
            } catch (DuplicateException e) {
                throw new DuplicateException(String.format(
                        DUPLICATE_PATTERN,
                        method,
                        route.route(),
                        getClass().getSimpleName()
                ));
            }
        }));
    }

    @Override
    public Router router() {
        return router;
    }

    @Override
    public String getPath() {
        return route;
    }

    @Override
    public void setPath(String route) {
        this.route = Objects.requireNonNull(route);
    }
}
