package com.github.xwc.compiler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by xwc on 2018/7/4.
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface ShapeType {

    int value() default Integer.MAX_VALUE;

    Class superClass();
}
