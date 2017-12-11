package org.mql.jee.annotations;

import java.lang.annotation.*;

/**
 * @author Mohammed Aouf ZOUAG, on 12/11/2017
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Action {
    /**
     * @return the name of the corresponding action class to be generated.
     */
    String value() default "";
}