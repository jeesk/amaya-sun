package io.github.amayaframework.core.pipelines;

import com.github.romanqed.jutils.structs.Pair;
import io.github.amayaframework.core.contexts.HttpRequest;
import io.github.amayaframework.core.contexts.HttpResponse;
import io.github.amayaframework.core.routers.Route;
import io.github.amayaframework.server.utils.HttpCode;

public class InvokeControllerAction extends PipelineAction<Pair<HttpRequest, Route>, HttpResponse> {
    public InvokeControllerAction() {
        super(ProcessStage.INVOKE_CONTROLLER.name());
    }

    @Override
    public HttpResponse apply(Pair<HttpRequest, Route> pair) {
        try {
            return pair.getValue().apply(pair.getKey());
        } catch (IllegalArgumentException e) {
            reject(HttpCode.BAD_REQUEST);
        } catch (Exception e) {
            reject(HttpCode.INTERNAL_SERVER_ERROR);
        }
        return null;
    }
}
