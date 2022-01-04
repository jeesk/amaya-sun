package io.github.amayaframework.core.handlers;

import com.github.romanqed.jutils.structs.pipeline.Pipeline;
import io.github.amayaframework.core.controllers.Controller;
import io.github.amayaframework.core.pipelines.CheckResponseAction;
import io.github.amayaframework.core.pipelines.InvokeControllerAction;
import io.github.amayaframework.core.pipelines.ParseRequestAction;

import java.util.function.Consumer;

public class BaseHandlerConfigurator implements Consumer<PipelineHandler> {
    @Override
    public void accept(PipelineHandler pipelineHandler) {
        Pipeline input = pipelineHandler.input();
        Pipeline output = pipelineHandler.output();
        Controller controller = pipelineHandler.getController();
        ParseRequestAction parseAction = new ParseRequestAction(controller.router(), controller.getPath());
        input.put(parseAction.getName(), parseAction);
        InvokeControllerAction controllerAction = new InvokeControllerAction();
        input.put(controllerAction.getName(), controllerAction);
        CheckResponseAction checkAction = new CheckResponseAction();
        output.put(checkAction.getName(), checkAction);
    }
}
