package org.mql.processors.sub;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;

/**
 * @author Mohammed Aouf ZOUAG, on 12/13/2017
 */
public abstract class AbstractSubProcessor implements SubProcessor {
    protected ProcessingEnvironment processingEnvironment;
    protected RoundEnvironment roundEnvironment;

    public AbstractSubProcessor(ProcessingEnvironment processingEnvironment, RoundEnvironment roundEnvironment) {
        this.processingEnvironment = processingEnvironment;
        this.roundEnvironment = roundEnvironment;
    }

    public AbstractSubProcessor(ProcessingEnvironment processingEnvironment) {
        this.processingEnvironment = processingEnvironment;
    }

    public AbstractSubProcessor(RoundEnvironment roundEnvironment) {
        this.roundEnvironment = roundEnvironment;
    }

    public ProcessingEnvironment getProcessingEnvironment() {
        return processingEnvironment;
    }

    public void setProcessingEnvironment(ProcessingEnvironment processingEnvironment) {
        this.processingEnvironment = processingEnvironment;
    }

    public RoundEnvironment getRoundEnvironment() {
        return roundEnvironment;
    }

    public void setRoundEnvironment(RoundEnvironment roundEnvironment) {
        this.roundEnvironment = roundEnvironment;
    }
}
