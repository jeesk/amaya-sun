package io.github.amayaframework.core.handlers;

import com.github.romanqed.jutils.structs.pipeline.Pipeline;
import com.github.romanqed.jutils.structs.pipeline.PipelineResult;
import io.github.amayaframework.core.contexts.HttpResponse;
import io.github.amayaframework.core.controllers.Controller;
import io.github.amayaframework.server.interfaces.HttpExchange;

import java.io.IOException;

/**
 * <p>A class representing the main handler used inside the server. Built on pipelines.</p>
 * <p>The http connection processing scheme is quite simple: first, the input pipeline is triggered,
 * resulting in an HttpResponse. Then the output pipeline is triggered, the purpose of which is to
 * process and verify the received HttpResponse. After that, the server receives a response.</p>
 */
public class PipelineHandler extends AmayaHandler {
    private final Pipeline input;
    private final Pipeline output;

    public PipelineHandler(Controller controller) {
        super(controller);
        input = new Pipeline();
        output = new Pipeline();
    }

    /**
     * Returns pipeline handles input
     *
     * @return {@link Pipeline}
     */
    public Pipeline input() {
        return input;
    }

    /**
     * Returns pipeline handles output
     *
     * @return {@link Pipeline}
     */
    public Pipeline output() {
        return output;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        PipelineResult inputResult = input.process(exchange);
        HttpResponse response = (HttpResponse) output.process(inputResult).getResult();
        String body;
        try {
            body = (String) response.getBody();
        } catch (Exception e) {
            reject(exchange);
            return;
        }
        exchange.getResponseHeaders().putAll(response.getHeaders());
        sendAnswer(exchange, response.getCode(), body);
    }
}
