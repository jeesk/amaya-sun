package io.github.amayaframework.core.pipelines;

import com.github.romanqed.jutils.structs.pipeline.PipelineInterruptException;

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

    protected void interrupt(String message, Object ret) {
        throw new PipelineInterruptException(message, ret);
    }

    protected void interrupt(Object ret) {
        interrupt(null, ret);
    }
}
