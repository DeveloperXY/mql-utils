package org.mql.bestpractices;

import java.lang.annotation.*;

/**
 * @author Mohammed Aouf ZOUAG, on 12/11/2017
 */
@Capitalized
@Documented
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Model {
}
