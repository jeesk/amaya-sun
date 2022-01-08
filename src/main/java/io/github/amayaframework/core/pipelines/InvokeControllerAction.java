package io.github.amayaframework.core.pipelines;

import io.github.amayaframework.core.contexts.HttpResponse;
import io.github.amayaframework.server.utils.HttpCode;

/**
 * <p>Input action that transfers control to the controller.</p>
 * <p>Receives: {@link RequestData}</p>
 * <p>Returns: {@link HttpResponse}</p>
 */
public class InvokeControllerAction extends PipelineAction<RequestData, HttpResponse> {
    @Override
    public HttpResponse apply(RequestData requestData) {
        try {
            return requestData.route.apply(requestData.request);
        } catch (IllegalArgumentException e) {
            reject(HttpCode.BAD_REQUEST);
        } catch (Exception e) {
            reject(HttpCode.INTERNAL_SERVER_ERROR);
        }
        return null;
    }
}
