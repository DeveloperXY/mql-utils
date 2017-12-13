package org.mql.processors.sub;

/**
 * @author Mohammed Aouf ZOUAG, on 12/13/2017
 */
@FunctionalInterface
public interface SubProcessor {
    /**
     * @return a boolean indicating the result of the sub processor's execution.
     */
    boolean run();
}
