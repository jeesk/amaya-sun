package io.github.amayaframework.core.pipelines;

import io.github.amayaframework.core.contexts.HttpResponse;
import io.github.amayaframework.server.utils.HttpCode;

/**
 * <p>Input action that transfers control to the controller.</p>
 * <p>Receives: {@link AbstractRequestData}</p>
 * <p>Returns: {@link HttpResponse}</p>
 */
public class InvokeControllerAction extends PipelineAction<AbstractRequestData, HttpResponse> {
    @Override
    public HttpResponse apply(AbstractRequestData requestData) {
        try {
            return requestData.getRoute().apply(requestData.getRequest());
        } catch (IllegalArgumentException e) {
            reject(HttpCode.BAD_REQUEST);
        } catch (Exception e) {
            reject(HttpCode.INTERNAL_SERVER_ERROR);
        }
        return null;
    }
}
