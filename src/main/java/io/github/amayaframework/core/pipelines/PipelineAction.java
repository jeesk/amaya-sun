package io.github.amayaframework.core.pipelines;

import com.github.romanqed.jutils.structs.pipeline.PipelineInterruptException;
import io.github.amayaframework.core.contexts.HttpResponse;
import io.github.amayaframework.server.utils.HttpCode;

import java.util.Objects;
import java.util.function.Function;

public abstract class PipelineAction<T, R> implements Function<T, R> {
    private final String name;

    public PipelineAction(String name) {
        this.name = Objects.requireNonNull(name);
    }

    public String getName() {
        return name;
    }

    protected void interrupt(Object ret) {
        throw new PipelineInterruptException(null, ret);
    }

    protected void reject(HttpCode code) {
        HttpResponse ret = new HttpResponse(code);
        ret.setBody(code.getMessage());
        interrupt(ret);
    }
}
