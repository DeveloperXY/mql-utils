package org.mql.bestpractices;

import java.lang.annotation.*;

/**
 * @author Mohammed Aouf ZOUAG, on 12/11/2017
 */
@Documented
@Target(ElementType.PACKAGE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckForBestPractices {
    /**
     * @return a boolean indicating whether to activate the checks for best practices or not.
     */
    boolean enabled() default true;
}
