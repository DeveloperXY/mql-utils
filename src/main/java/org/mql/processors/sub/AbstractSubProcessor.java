package org.mql.processors.sub;

import org.mql.processors.models.FailureSubject;
import org.mql.processors.models.Payload;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.util.Elements;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author Mohammed Aouf ZOUAG, on 12/13/2017
 */
public abstract class AbstractSubProcessor implements SubProcessor {
    protected ProcessingEnvironment processingEnvironment;
    protected RoundEnvironment roundEnvironment;
    protected Elements elementUtils;

    protected boolean status;
    protected List<FailureSubject> failureSubjects;

    public AbstractSubProcessor(ProcessingEnvironment processingEnvironment, RoundEnvironment roundEnvironment) {
        this.processingEnvironment = processingEnvironment;
        this.roundEnvironment = roundEnvironment;
        elementUtils = processingEnvironment.getElementUtils();
        failureSubjects = new ArrayList<>();
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

    public List<FailureSubject> getFailureSubjects() {
        return failureSubjects;
    }

    public boolean getStatus() {
        return status;
    }

    public Payload calculatePayload(Supplier<Boolean> resultSupplier) {
        status = resultSupplier.get();
        return statusBasedPayload();
    }

    public Payload statusBasedPayload() {
        return status ? new Payload(true, getSuccessMessage()) :
                new Payload(false, failureSubjects);
    }

    /**
     * @return a success message to be dispatched, indicating that the processor's execution was successful.
     */
    protected abstract String getSuccessMessage();
}
