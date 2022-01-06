package io.github.amayaframework.core.handlers;

import com.github.romanqed.jutils.structs.pipeline.Pipeline;
import io.github.amayaframework.core.controllers.Controller;
import io.github.amayaframework.core.pipelines.CheckResponseAction;
import io.github.amayaframework.core.pipelines.FindRouteAction;
import io.github.amayaframework.core.pipelines.InvokeControllerAction;
import io.github.amayaframework.core.pipelines.ParseRequestAction;

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
        FindRouteAction findAction = new FindRouteAction(controller.router(), controller.getPath());
        input.put(findAction.getName(), findAction);
        ParseRequestAction requestAction = new ParseRequestAction();
        input.put(requestAction.getName(), requestAction);
        InvokeControllerAction controllerAction = new InvokeControllerAction();
        input.put(controllerAction.getName(), controllerAction);
        CheckResponseAction checkAction = new CheckResponseAction();
        output.put(checkAction.getName(), checkAction);
    }
}
