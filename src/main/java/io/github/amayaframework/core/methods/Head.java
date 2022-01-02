package io.github.amayaframework.core.methods;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Head {
    String value() default "";
}
