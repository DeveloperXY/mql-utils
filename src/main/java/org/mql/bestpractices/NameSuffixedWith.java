package org.mql.bestpractices;

import java.lang.annotation.*;

/**
 * Meta-annotation used specify a suffix for the classes to be annotated in a non-direct way
 * with this annotation.
 *
 * @author Mohammed Aouf ZOUAG, on 12/22/2017
 */
@Documented
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface NameSuffixedWith {
    /**
     * @return the suffix that should be used to name the class annotated with this annotation.
     */
    String value();
}
