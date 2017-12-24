package org.mql.utils;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;

/**
 * A helper class to display messages using a Messager object through a processing environment.
 *
 * @author Mohammed Aouf ZOUAG, on 12/24/2017
 */
public class MessagerUtils {

    private static ProcessingEnvironment processingEnvironment;

    public static void init(ProcessingEnvironment processingEnv) {
        processingEnvironment = processingEnv;
    }

    public static void displayMessage(String message) {
        displayMessage(Diagnostic.Kind.NOTE, message, null);
    }

    public static void displayMessage(Diagnostic.Kind kind, String message, Element element) {
        if (element == null)
            processingEnvironment.getMessager().printMessage(kind, message);
        else
            processingEnvironment.getMessager().printMessage(kind, message, element);
    }

    public static void displayMessageWithElement(String message, Element element) {
        displayMessage(Diagnostic.Kind.NOTE, message, element);
    }
}
