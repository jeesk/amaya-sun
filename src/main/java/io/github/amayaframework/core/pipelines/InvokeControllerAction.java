package io.github.amayaframework.core.pipelines;

import com.github.romanqed.jutils.structs.Pair;
import io.github.amayaframework.core.contexts.HttpRequest;
import io.github.amayaframework.core.contexts.HttpResponse;
import io.github.amayaframework.core.routers.Route;

public class InvokeControllerAction extends PipelineAction<Pair<HttpRequest, Route>, HttpResponse> {
    public InvokeControllerAction() {
        super(ProcessStage.INVOKE_CONTROLLER.name());
    }

    @Override
    public HttpResponse apply(Pair<HttpRequest, Route> pair) {
        return pair.getValue().apply(pair.getKey());
    }
}
