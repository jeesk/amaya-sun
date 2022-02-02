package io.github.amayaframework.core.handlers;

import com.github.romanqed.jutils.pipeline.PipelineResult;
import io.github.amayaframework.core.configurators.BaseSunConfigurator;
import io.github.amayaframework.core.controllers.Controller;
import io.github.amayaframework.core.pipelines.SunRequestData;
import io.github.amayaframework.core.util.AmayaConfig;
import io.github.amayaframework.server.interfaces.HttpExchange;
import io.github.amayaframework.server.interfaces.HttpHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collections;

/**
 * <p>A class representing the sun handler used inside the server. Built on pipelines.</p>
 * <p>The http connection processing scheme is quite simple: first, the input pipeline is triggered,
 * resulting in an HttpResponse. Then the output pipeline is triggered, the purpose of which is to
 * process and verify the received HttpResponse. After that, the server receives a response.</p>
 */
public class SunHandler implements HttpHandler {
    private final IOHandler handler;
    private final SunWrapper wrapper;

    public SunHandler(Controller controller) {
        handler = new BaseIOHandler(controller, Collections.singletonList(new BaseSunConfigurator()));
        Logger logger = LoggerFactory.getLogger(getClass());
        wrapper = new SunWrapper(logger, AmayaConfig.INSTANCE.getCharset());
    }

    public IOHandler getHandler() {
        return handler;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        SunRequestData requestData = new SunRequestData(exchange);
        PipelineResult processResult = handler.process(requestData);
        wrapper.process(exchange, processResult);
    }
}
