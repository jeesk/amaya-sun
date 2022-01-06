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
        if (result.getException() != null) {
            reject(HttpCode.INTERNAL_SERVER_ERROR);
        }
        try {
            return (HttpResponse) result.getResult();
        } catch (ClassCastException e) {
            reject(HttpCode.INTERNAL_SERVER_ERROR);
            return null;
        }
    }
}
