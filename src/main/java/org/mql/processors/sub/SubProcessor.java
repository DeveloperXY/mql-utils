package org.mql.processors.sub;

import org.mql.processors.models.Payload;

/**
 * @author Mohammed Aouf ZOUAG, on 12/13/2017
 */
@FunctionalInterface
public interface SubProcessor {
    /**
     * @return a payload indicating the result of the sub processor's execution.
     */
    Payload run();
}
