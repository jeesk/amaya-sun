package io.github.amayaframework.core.pipelines.debug;

import com.github.romanqed.jutils.structs.pipeline.PipelineResult;
import io.github.amayaframework.core.contexts.HttpResponse;
import io.github.amayaframework.core.pipelines.PipelineAction;
import io.github.amayaframework.core.util.LogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InputResultDebugAction extends PipelineAction<PipelineResult, PipelineResult> {
    private static final Logger logger = LoggerFactory.getLogger(InputResultDebugAction.class);

    @Override
    public PipelineResult apply(PipelineResult result) {
        String message = "The input pipeline is completed\n" +
                "Interrupted: " + result.isInterrupted() + "\n" +
                "Exception: " + result.getException() + "\n" +
                "Result: " + result.getResult().getClass().getSimpleName() + "\n";
        Object toCheck = result.getResult();
        if (toCheck instanceof HttpResponse) {
            message += LogUtil.getResponseData((HttpResponse) toCheck);
        }
        logger.debug(message);
        return result;
    }
}
