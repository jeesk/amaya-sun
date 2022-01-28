package io.github.amayaframework.core.pipelines.debug;

import io.github.amayaframework.core.contexts.HttpRequest;
import io.github.amayaframework.core.pipelines.AbstractRequestData;
import io.github.amayaframework.core.pipelines.PipelineAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestDebugAction extends PipelineAction<AbstractRequestData, AbstractRequestData> {
    private static final Logger logger = LoggerFactory.getLogger(RequestDebugAction.class);

    @Override
    public AbstractRequestData apply(AbstractRequestData requestData) {
        HttpRequest request = requestData.getRequest();
        String message = "The request was parsed successfully\n" +
                "Implementation used: " + request.getClass().getSimpleName() + "\n" +
                "Parsed path parameters: " + request.getPathParameters() + "\n" +
                "Parsed query parameters: " + request.getQueryParameters() + "\n";
        logger.debug(message);
        return requestData;
    }
}
