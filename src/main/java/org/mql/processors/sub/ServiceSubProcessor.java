package org.mql.processors.sub;

import org.mql.utils.Annotations;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;

/**
 * @author Mohammed Aouf ZOUAG, on 12/23/2017
 */
public class ServiceSubProcessor extends SuffixProcessor {
    public ServiceSubProcessor(ProcessingEnvironment processingEnvironment,
                               RoundEnvironment roundEnvironment) {
        super(processingEnvironment, roundEnvironment,
                processingEnvironment.getElementUtils().getTypeElement(Annotations.SERVICE));
    }

    @Override
    public String getSuccessMessage() {
        return "All service classes are well named.";
    }
}
