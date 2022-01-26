package io.github.amayaframework.core.configurators;

import com.github.romanqed.jutils.structs.pipeline.Pipeline;
import io.github.amayaframework.core.controllers.Controller;
import io.github.amayaframework.core.pipelines.Stage;
import io.github.amayaframework.core.pipelines.sun.SunFindRouteAction;
import io.github.amayaframework.core.pipelines.sun.SunParseRequestAction;
import io.github.amayaframework.core.pipelines.sun.SunParseRequestCookiesAction;

/**
 * <p>A configurator that adds basic handlers to the server pipeline that provide routing,
 * request processing and receiving a response from the controller.</p>
 * <p>In the list of configurators, it should always go first, otherwise the server's operability is not guaranteed.</p>
 */
public class BaseSunConfigurator extends BaseConfigurator {
    @Override
    protected Pipeline difference(Controller controller) {
        Pipeline toAdd = new Pipeline();
        toAdd.put(Stage.FIND_ROUTE.name(), new SunFindRouteAction(controller.router(), controller.getPath()));
        toAdd.put(Stage.PARSE_REQUEST.name(), new SunParseRequestAction());
        toAdd.put(Stage.PARSE_REQUEST_COOKIES.name(), new SunParseRequestCookiesAction());
        return toAdd;
    }
}
