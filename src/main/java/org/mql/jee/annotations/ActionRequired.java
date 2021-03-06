package org.mql.jee.annotations;

import java.lang.annotation.*;

/**
 * Indicates that the class annotated with it is requesting the generation of a corresponding action class.
 *
 * @author Mohammed Aouf ZOUAG, on 12/11/2017
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ActionRequired {
    /**
     * @return the name of the corresponding action class to be generated.
     */
    String value() default "";

    /**
     * @return the default result of the invoked action method.
     */
    String defaultResult() default "ok";
}