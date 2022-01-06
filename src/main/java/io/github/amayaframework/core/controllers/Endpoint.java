package io.github.amayaframework.core.controllers;

import io.github.amayaframework.core.AmayaBuilder;
import org.atteo.classindex.IndexAnnotated;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * <p>An annotation that is a marker for user controllers by default.</p>
 * <p>Can be changed using {@link AmayaBuilder#controllerAnnotation(Class)}</p>
 */
@Retention(RetentionPolicy.RUNTIME)
@IndexAnnotated
public @interface Endpoint {
    String value();
}
