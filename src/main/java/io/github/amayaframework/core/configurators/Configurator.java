package io.github.amayaframework.core.configurators;

import io.github.amayaframework.core.handlers.IOHandler;

import java.util.Objects;
import java.util.function.Consumer;

public interface Configurator extends Consumer<IOHandler> {
    default void configure(IOHandler configurable) {
        Objects.requireNonNull(configurable);
        accept(configurable);
    }
}
