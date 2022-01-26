package io.github.amayaframework.core.configurators;

import com.github.romanqed.jutils.structs.pipeline.Pipeline;
import io.github.amayaframework.core.controllers.Controller;
import io.github.amayaframework.core.pipelines.Stage;
import io.github.amayaframework.core.pipelines.servlets.ServletsFindRouteAction;
import io.github.amayaframework.core.pipelines.servlets.ServletsParseRequestAction;
import io.github.amayaframework.core.pipelines.servlets.ServletsParseRequestCookiesAction;

public class BaseServletConfigurator extends BaseConfigurator {

    @Override
    protected Pipeline difference(Controller controller) {
        Pipeline toAdd = new Pipeline();
        toAdd.put(Stage.FIND_ROUTE.name(), new ServletsFindRouteAction(controller.router(), controller.getPath()));
        toAdd.put(Stage.PARSE_REQUEST.name(), new ServletsParseRequestAction());
        toAdd.put(Stage.PARSE_REQUEST_COOKIES.name(), new ServletsParseRequestCookiesAction());
        return toAdd;
    }
}
