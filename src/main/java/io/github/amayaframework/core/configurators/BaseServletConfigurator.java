package io.github.amayaframework.core.configurators;

import com.github.romanqed.jutils.structs.pipeline.Pipeline;
import io.github.amayaframework.core.controllers.Controller;
import io.github.amayaframework.core.pipelines.Stage;
import io.github.amayaframework.core.pipelines.servlets.ServletFindRouteAction;
import io.github.amayaframework.core.pipelines.servlets.ServletParseRequestAction;
import io.github.amayaframework.core.pipelines.servlets.ServletParseRequestCookiesAction;

/**
 * <p>A configurator that adds basic handlers to the servlet pipeline that provide routing,
 * request processing and receiving a response from the controller.</p>
 * <p>In the list of configurators, it should always go first, otherwise the servlet's operability is not guaranteed.</p>
 */
public class BaseServletConfigurator extends BaseConfigurator {

    @Override
    protected Pipeline difference(Controller controller) {
        Pipeline toAdd = new Pipeline();
        toAdd.put(Stage.FIND_ROUTE.name(), new ServletFindRouteAction(controller.router(), controller.getPath()));
        toAdd.put(Stage.PARSE_REQUEST.name(), new ServletParseRequestAction());
        toAdd.put(Stage.PARSE_REQUEST_COOKIES.name(), new ServletParseRequestCookiesAction());
        return toAdd;
    }
}
