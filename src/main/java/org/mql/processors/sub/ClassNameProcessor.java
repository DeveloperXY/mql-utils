package org.mql.processors.sub;

import org.mql.processors.models.FailureSubject;
import org.mql.processors.models.Payload;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import java.util.stream.Collectors;

/**
 * @author Mohammed Aouf ZOUAG, on 12/13/2017
 */
public class ClassNameProcessor extends AbstractSubProcessor {

    public ClassNameProcessor(ProcessingEnvironment processingEnvironment, RoundEnvironment roundEnvironment) {
        super(processingEnvironment, roundEnvironment);
    }

    @Override
    public Payload run() {
        // The root elements will contain a package definition, due to the presence of a package-info.java file
        // We just need the class
        return calculatePayload(() ->
                ElementFilter.typesIn(roundEnvironment.getRootElements())
                        .stream()
                        .map(Object::toString)
                        .map(elementUtils::getTypeElement)
                        .map(this::checkThatClassNameIsCapitalized)
                        .collect(Collectors.toList()) // Weird bug happens if I don't collect into a List here:
                        .stream()                     // Only the first encountered class is processed.
                        .allMatch(b -> b)); // calling map() then allMatch instead of AllMatch alone to process all class names
    }

    @Override
    protected String getSuccessMessage() {
        return "All class names are capitalized.";
    }

    private boolean checkThatClassNameIsCapitalized(TypeElement classElement) {
        String className = classElement.getSimpleName().toString();
        if (Character.isLowerCase(className.charAt(0))) {
            String appropriateClassName = className.length() == 1 ? className.toUpperCase() :
                    Character.toUpperCase(className.charAt(0)) + className.substring(1);
            String errorMessage = String.format("The class '%s' should be named '%s'.",
                    className, appropriateClassName);
            failureSubjects.add(new FailureSubject(classElement, errorMessage));
            return false;
        }
        return true;
    }
}
