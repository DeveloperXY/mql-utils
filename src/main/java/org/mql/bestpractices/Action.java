package org.mql.bestpractices;

import java.lang.annotation.*;

/**
 * @author Mohammed Aouf ZOUAG, on 12/22/2017
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@NameSuffixedWith("Action")
public @interface Action {
}
