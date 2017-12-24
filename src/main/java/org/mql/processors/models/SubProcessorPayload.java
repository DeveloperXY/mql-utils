package org.mql.processors.models;

/**
 * @author Mohammed Aouf ZOUAG, on 12/24/2017
 */
public class SubProcessorPayload {
    /**
     * A boolean flag indicating the result of the execution of a given sub processor.
     */
    private boolean status;
    /**
     * A significant message to be dispatched to the main processor in case the execution was successful,
     * aka the status above is equal to 'true'.
     */
    private String successMessage;

    public SubProcessorPayload(boolean status, String successMessage) {
        this.status = status;
        this.successMessage = successMessage;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getSuccessMessage() {
        return successMessage;
    }

    public void setSuccessMessage(String successMessage) {
        this.successMessage = successMessage;
    }

    @Override
    public String toString() {
        return "SubProcessorPayload{" +
                "status=" + status +
                ", successMessage='" + successMessage + '\'' +
                '}';
    }
}
