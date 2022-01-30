package io.github.amayaframework.core.configurators;

import com.github.romanqed.jutils.structs.pipeline.Pipeline;
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
        Pipeline input = handler.getInput();
        Pipeline output = handler.getOutput();
        Controller controller = handler.getController();
        input.put(Stage.FIND_ROUTE.name(), new FindRouteAction(controller.router(), controller.getPath()));
        input.put(Stage.PARSE_REQUEST.name(), new ParseRequestAction());
        input.put(Stage.PARSE_REQUEST_BODY.name(), new ParseRequestBodyAction());
        input.put(Stage.PARSE_REQUEST_COOKIES.name(), new ParseRequestCookiesAction());
        input.put(Stage.INVOKE_CONTROLLER.name(), new InvokeControllerAction());
        output.put(Stage.CHECK_RESPONSE.name(), new CheckResponseAction());
        output.put(Stage.PARSE_RESPONSE_COOKIES.name(), new ParseResponseCookiesAction());
    }
}
