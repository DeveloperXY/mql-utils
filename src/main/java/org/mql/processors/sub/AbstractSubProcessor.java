package org.mql.processors.sub;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.util.Elements;

/**
 * @author Mohammed Aouf ZOUAG, on 12/13/2017
 */
public abstract class AbstractSubProcessor implements SubProcessor {
    protected ProcessingEnvironment processingEnvironment;
    protected RoundEnvironment roundEnvironment;
    protected Elements elementUtils;

    public AbstractSubProcessor(ProcessingEnvironment processingEnvironment, RoundEnvironment roundEnvironment) {
        this.processingEnvironment = processingEnvironment;
        this.roundEnvironment = roundEnvironment;
        elementUtils = processingEnvironment.getElementUtils();
    }

    public ProcessingEnvironment getProcessingEnvironment() {
        return processingEnvironment;
    }

    public RoundEnvironment getRoundEnvironment() {
        return roundEnvironment;
    }

    public Elements getElementUtils() {
        return elementUtils;
    }
}
