package org.mql.processors.models;

import javax.lang.model.element.Element;

/**
 * @author Mohammed Aouf ZOUAG, on 12/24/2017
 */
public class FailureSubject {
    private Element element;
    private String message;

    public FailureSubject(Element element, String message) {
        this.element = element;
        this.message = message;
    }

    public Element getElement() {
        return element;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "FailureSubject{" +
                "element=" + element +
                ", message='" + message + '\'' +
                '}';
    }
}
