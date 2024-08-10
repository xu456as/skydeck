package io.skydeck.gserver.annotation;

import io.skydeck.gserver.enums.CardNameType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.TYPE)
public @interface CardExecMeta {
    CardNameType cardNameType();
    String settlement();
}
