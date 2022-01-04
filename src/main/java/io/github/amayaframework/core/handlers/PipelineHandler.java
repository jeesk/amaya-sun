package io.github.amayaframework.core.handlers;

import com.github.romanqed.jutils.structs.pipeline.Pipeline;
import com.github.romanqed.jutils.structs.pipeline.PipelineResult;
import io.github.amayaframework.core.contexts.HttpResponse;
import io.github.amayaframework.core.controllers.Controller;
import io.github.amayaframework.server.interfaces.HttpExchange;

import java.io.IOException;

public class PipelineHandler extends AmayaHandler {
    private final Pipeline input;
    private final Pipeline output;

    public PipelineHandler(Controller controller) {
        super(controller);
        input = new Pipeline();
        output = new Pipeline();
    }

    public Pipeline input() {
        return input;
    }

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
