package org.mql.bestpractices;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Target;

/**
 * A type annotated with this annotation shall be checked if its name starts with a capital letter.
 *
 * @author Mohammed Aouf ZOUAG, on 12/12/2017
 */
@Documented
@Inherited
@Target(ElementType.TYPE)
public @interface Capitalized {
}
