package io.github.amayaframework.core.pipelines.debug;

import io.github.amayaframework.core.pipelines.AbstractRequestData;
import io.github.amayaframework.core.pipelines.PipelineAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RouteDebugAction extends PipelineAction<AbstractRequestData, AbstractRequestData> {
    private static final Logger logger = LoggerFactory.getLogger(RouteDebugAction.class);

    @Override
    public AbstractRequestData apply(AbstractRequestData requestData) {
        String message = "Route found successfully\n" +
                "Method: " + requestData.getMethod() + '\n' +
                "Route: " + requestData.getRoute().route() + '\n' +
                "Path: " + requestData.getPath() + '\n';
        logger.debug(message);
        return requestData;
    }
}
