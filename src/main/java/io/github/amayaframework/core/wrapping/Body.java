package io.github.amayaframework.core.wrapping;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Body {
    String value() default "";
}
