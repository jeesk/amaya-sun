package io.github.amayaframework.core.configurators;

import com.github.romanqed.jutils.structs.pipeline.Pipeline;
import io.github.amayaframework.core.controllers.Controller;
import io.github.amayaframework.core.handlers.IOHandler;
import io.github.amayaframework.core.pipelines.*;
import io.github.amayaframework.core.pipelines.debug.*;
import io.github.amayaframework.core.util.AmayaConfig;

abstract class BaseConfigurator implements Configurator {
    @Override
    public void accept(IOHandler handler) {
        Pipeline input = handler.getInput();
        Pipeline output = handler.getOutput();
        input.put(Stage.INVOKE_CONTROLLER.name(), new InvokeControllerAction());
        input.insertBefore(Stage.INVOKE_CONTROLLER.name(), difference(handler.getController()));
        input.insertAfter(Stage.PARSE_REQUEST.name(), Stage.PARSE_REQUEST_BODY.name(), new ParseRequestBodyAction());
        output.put(Stage.CHECK_RESPONSE.name(), new CheckResponseAction());
        output.put(Stage.PARSE_RESPONSE_COOKIES.name(), new ParseResponseCookiesAction());
        if (AmayaConfig.INSTANCE.getDebug()) {
            addDebugActions(input, output);
        }
    }

    protected void addDebugActions(Pipeline input, Pipeline output) {
        input.insertAfter(Stage.FIND_ROUTE.name(), DebugStage.ROUTE_DEBUG.name(), new RouteDebugAction());
        input.insertAfter(Stage.PARSE_REQUEST.name(), DebugStage.REQUEST_DEBUG.name(), new RequestDebugAction());
        input.insertAfter(Stage.INVOKE_CONTROLLER.name(), DebugStage.RESPONSE_DEBUG.name(), new ResponseDebugAction());
        output.insertBefore(Stage.CHECK_RESPONSE.name(), DebugStage.INPUT_RESULT_DEBUG.name(),
                new InputResultDebugAction());
    }

    protected abstract Pipeline difference(Controller controller);
}
