package io.github.amayaframework.core.pipelines;

import com.github.romanqed.jutils.structs.pipeline.PipelineResult;
import io.github.amayaframework.core.contexts.HttpResponse;
import io.github.amayaframework.server.utils.HttpCode;

public class CheckResponseAction extends PipelineAction<PipelineResult, HttpResponse> {
    public CheckResponseAction() {
        super(ProcessStage.CHECK_RESPONSE.name());
    }

    @Override
    public HttpResponse apply(PipelineResult result) {
        Exception exception = result.getException();
        if (exception != null) {
            if (exception instanceof IllegalArgumentException) {
                interrupt(reject(HttpCode.BAD_REQUEST));
            }
            if (exception instanceof NotFoundException) {
                interrupt(reject(HttpCode.NOT_FOUND));
            }
        }
        try {
            return (HttpResponse) result.getResult();
        } catch (ClassCastException e) {
            interrupt(reject(HttpCode.INTERNAL_SERVER_ERROR));
            return null;
        }
    }

    private HttpResponse reject(HttpCode code) {
        HttpResponse ret = new HttpResponse(code);
        ret.setBody(code.getMessage());
        return ret;
    }
}
