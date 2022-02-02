package io.github.amayaframework.core.configurators;

import com.github.romanqed.jutils.pipeline.Pipeline;
import io.github.amayaframework.core.controllers.Controller;
import io.github.amayaframework.core.handlers.IOHandler;
import io.github.amayaframework.core.pipelines.*;

/**
 * <p>A configurator that adds basic handlers to the sun server pipeline that provide routing,
 * request processing and receiving a response from the controller.</p>
 * <p>In the list of configurators, it should always go first, otherwise the server's operability is not guaranteed.</p>
 */
public class BaseSunConfigurator extends AbstractConfigurator {

    @Override
    public void accept(IOHandler handler) {
        Pipeline pipeline = handler.getPipeline();
        Controller controller = handler.getController();
        pipeline.put(Stage.FIND_ROUTE.name(), new FindRouteAction(controller.getRouter(), controller.getPath()));
        pipeline.put(Stage.PARSE_REQUEST.name(), new ParseRequestAction());
        pipeline.put(Stage.PARSE_REQUEST_BODY.name(), new ParseRequestBodyAction());
        pipeline.put(Stage.PARSE_REQUEST_COOKIES.name(), new ParseRequestCookiesAction());
        pipeline.put(Stage.INVOKE_CONTROLLER.name(), new InvokeControllerAction());
        pipeline.put(Stage.PARSE_RESPONSE_COOKIES.name(), new ParseResponseCookiesAction());
    }
}
