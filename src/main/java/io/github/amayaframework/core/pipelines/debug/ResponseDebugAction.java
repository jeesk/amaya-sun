package io.github.amayaframework.core.pipelines.debug;

import io.github.amayaframework.core.contexts.HttpResponse;
import io.github.amayaframework.core.pipelines.PipelineAction;
import io.github.amayaframework.core.util.LogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResponseDebugAction extends PipelineAction<HttpResponse, HttpResponse> {
    private static final Logger logger = LoggerFactory.getLogger(ResponseDebugAction.class);

    @Override
    public HttpResponse apply(HttpResponse response) {
        String message = "HttpResponse was received successfully\n" +
                "Implementation used: " + response.getClass().getSimpleName() + "\n";
        logger.debug(message + LogUtil.getResponseData(response));
        return response;
    }
}
