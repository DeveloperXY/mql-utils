package org.mql.processors.sub;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;

/**
 * @author Mohammed Aouf ZOUAG, on 12/13/2017
 */
@FunctionalInterface
public interface SubProcessor {
    boolean run(ProcessingEnvironment processingEnvironment, RoundEnvironment roundEnvironment);

    default boolean run(ProcessingEnvironment processingEnvironment) {
        return run(processingEnvironment, null);
    }

    default boolean run(RoundEnvironment roundEnvironment) {
        return run(null, roundEnvironment);
    }
}
