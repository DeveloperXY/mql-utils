package org.mql.processors.sub;

import org.mql.processors.models.FailureSubject;
import org.mql.processors.models.Payload;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * @author Mohammed Aouf ZOUAG, on 12/13/2017
 */
public class MethodNameProcessor extends AbstractSubProcessor {

    public MethodNameProcessor(ProcessingEnvironment processingEnvironment, RoundEnvironment roundEnvironment) {
        super(processingEnvironment, roundEnvironment);
    }

    @Override
    public Payload run() {
        // The root elements will contain a package definition, due to the presence of a package-info.java file
        // We just need the class elements
        return calculatePayload(() ->
                ElementFilter.typesIn(roundEnvironment.getRootElements())
                        .stream()
                        .map(Object::toString)
                        .map(elementUtils::getTypeElement)
                        .map(TypeElement::getEnclosedElements)
                        .map(ElementFilter::methodsIn)
                        .flatMap(Collection::stream)
                        .map(this::checkThatMethodNameIsCapitalized)
                        .collect(Collectors.toList()) // Weird bug happens if I don't collect into a List here:
                        .stream()                     // Only the methods of the first encountered class are processed.
                        .allMatch(b -> b));
    }

    @Override
    protected String getSuccessMessage() {
        return "All methods are well named.";
    }

    private boolean checkThatMethodNameIsCapitalized(ExecutableElement methodElement) {
        String methodName = methodElement.getSimpleName().toString();
        if (Character.isUpperCase(methodName.charAt(0))) {
            String appropriateMethodName = methodName.length() == 1 ? methodName.toLowerCase() :
                    Character.toLowerCase(methodName.charAt(0)) + methodName.substring(1);
            String errorMessage = String.format("The method '%s' should be named '%s'.",
                    methodName, appropriateMethodName);
            failureSubjects.add(new FailureSubject(methodElement, errorMessage));
            return false;
        }
        return true;
    }
}
