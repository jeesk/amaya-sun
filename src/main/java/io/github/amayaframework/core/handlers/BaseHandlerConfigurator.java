package io.github.amayaframework.core.handlers;

import com.github.romanqed.jutils.structs.pipeline.Pipeline;
import io.github.amayaframework.core.controllers.Controller;
import io.github.amayaframework.core.pipelines.*;

import java.util.function.Consumer;

/**
 * <p>A configurator that adds basic handlers to the server pipeline that provide routing,
 * request processing and receiving a response from the controller.</p>
 * <p>In the list of configurators, it should always go first, otherwise the server's operability is not guaranteed.</p>
 */
public class BaseHandlerConfigurator implements Consumer<PipelineHandler> {
    @Override
    public void accept(PipelineHandler pipelineHandler) {
        Pipeline input = pipelineHandler.input();
        Pipeline output = pipelineHandler.output();
        Controller controller = pipelineHandler.getController();
        input.put(Stage.FIND_ROUTE.name(), new FindRouteAction(controller.router(), controller.getPath()));
        input.put(Stage.PARSE_REQUEST.name(), new ParseRequestAction());
        input.put(Stage.PARSE_REQUEST_COOKIES.name(), new ParseRequestCookiesAction());
        input.put(Stage.INVOKE_CONTROLLER.name(), new InvokeControllerAction());
        output.put(Stage.CHECK_RESPONSE.name(), new CheckResponseAction());
        output.put(Stage.PARSE_RESPONSE_COOKIES.name(), new ParseResponseCookiesAction());
    }
}
