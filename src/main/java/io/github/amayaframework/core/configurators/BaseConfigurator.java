package io.github.amayaframework.core.configurators;

import com.github.romanqed.jutils.structs.pipeline.Pipeline;
import io.github.amayaframework.core.controllers.Controller;
import io.github.amayaframework.core.handlers.IOHandler;
import io.github.amayaframework.core.pipelines.CheckResponseAction;
import io.github.amayaframework.core.pipelines.InvokeControllerAction;
import io.github.amayaframework.core.pipelines.ParseResponseCookiesAction;
import io.github.amayaframework.core.pipelines.Stage;

abstract class BaseConfigurator implements Configurator {
    @Override
    public void accept(IOHandler handler) {
        Pipeline input = handler.getInput();
        Pipeline output = handler.getOutput();
        input.put(Stage.INVOKE_CONTROLLER.name(), new InvokeControllerAction());
        input.insertBefore(Stage.INVOKE_CONTROLLER.name(), difference(handler.getController()));
        output.put(Stage.CHECK_RESPONSE.name(), new CheckResponseAction());
        output.put(Stage.PARSE_RESPONSE_COOKIES.name(), new ParseResponseCookiesAction());
    }

    protected abstract Pipeline difference(Controller controller);
}
