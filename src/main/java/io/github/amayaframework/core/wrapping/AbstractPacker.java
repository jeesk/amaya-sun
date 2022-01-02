package io.github.amayaframework.core.wrapping;

import io.github.amayaframework.core.contexts.HttpRequest;
import io.github.amayaframework.core.contexts.HttpResponse;
import io.github.amayaframework.core.contexts.HttpTransaction;
import io.github.amayaframework.core.routers.InvalidFormatException;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.function.Function;

public abstract class AbstractPacker implements Packer {
    private static final Set<Class<?>> ALLOWED_TYPES;
    private static final Set<Class<?>> ALLOWED_RETURN_TYPES;

    static {
        List<Class<?>> types = Arrays.asList(Object.class, HttpRequest.class, HttpTransaction.class);
        ALLOWED_TYPES = Collections.unmodifiableSet(new HashSet<>(types));
        List<Class<?>> returnTypes = Arrays.asList(Object.class, HttpResponse.class);
        ALLOWED_RETURN_TYPES = Collections.unmodifiableSet(new HashSet<>(returnTypes));
    }

    @Override
    public Function<HttpRequest, HttpResponse> checkedPack(Object instance, Method method) {
        try {
            return pack(instance, method);
        } catch (Exception e) {
            throw new InvalidFormatException("Invalid method with name \"" + method.getName() + "\" format", e);
        }
    }

    protected void checkParameters(Class<?> returnType, Parameter[] parameters, boolean requireInject) {
        if (parameters.length < 1) {
            throw new IllegalStateException("Too low parameters!");
        }
        if (!requireInject && parameters.length > 1) {
            throw new IllegalStateException("Too many parameters!");
        }
        if (!ALLOWED_RETURN_TYPES.contains(returnType)) {
            throw new IllegalStateException(
                    "Invalid return type: " + returnType.getName() + "! Allowed: " + ALLOWED_RETURN_TYPES
            );
        }
        Class<?> first = parameters[0].getType();
        if (!ALLOWED_TYPES.contains(first)) {
            throw new IllegalStateException(
                    "Invalid first parameter type: " + first.getName() + "! Allowed: " + ALLOWED_TYPES
            );
        }
    }
}
