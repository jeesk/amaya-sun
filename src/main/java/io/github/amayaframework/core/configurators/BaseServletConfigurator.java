package io.github.amayaframework.core.configurators;

import com.github.romanqed.jutils.structs.pipeline.Pipeline;
import io.github.amayaframework.core.controllers.Controller;
import io.github.amayaframework.core.pipelines.Stage;
import io.github.amayaframework.core.pipelines.servlets.ServletFindRouteAction;
import io.github.amayaframework.core.pipelines.servlets.ServletParseRequestAction;
import io.github.amayaframework.core.pipelines.servlets.ServletParseRequestCookiesAction;

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
