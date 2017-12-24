package org.mql.processors.models;

import java.util.List;

/**
 * This class represents the result of the execution of a given sub processor.
 * This result is to be dispatched to a main processor.
 *
 * @author Mohammed Aouf ZOUAG, on 12/24/2017
 */
public class Payload {
    /**
     * A boolean flag indicating the result of the execution of a given sub processor.
     */
    private boolean status;
    private String successMessage;
    /**
     * A list of subjects who failed the sub processor's checks.
     */
    private List<FailureSubject> failureSubjects;

    public Payload(boolean status, String successMessage) {
        this.status = status;
        this.successMessage = successMessage;
    }

    public Payload(boolean status, List<FailureSubject> failureSubjects) {
        this.status = status;
        this.failureSubjects = failureSubjects;
    }

    public boolean getStatus() {
        return status;
    }

    public String getSuccessMessage() {
        return successMessage;
    }

    public List<FailureSubject> getFailureSubjects() {
        return failureSubjects;
    }

    @Override
    public String toString() {
        return "Payload{" +
                "status=" + status +
                ", successMessage='" + successMessage + '\'' +
                ", failureSubjects=" + failureSubjects +
                '}';
    }
}
