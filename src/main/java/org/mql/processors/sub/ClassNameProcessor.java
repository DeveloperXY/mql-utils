package org.mql.processors.sub;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic;

/**
 * @author Mohammed Aouf ZOUAG, on 12/13/2017
 */
public class ClassNameProcessor extends AbstractSubProcessor {

    public ClassNameProcessor(ProcessingEnvironment processingEnvironment, RoundEnvironment roundEnvironment) {
        super(processingEnvironment, roundEnvironment);
    }

    @Override
    public boolean run() {
        // The root elements will contain a package definition, due to the presence of a package-info.java file
        // We just need the class elements
        return ElementFilter.typesIn(roundEnvironment.getRootElements())
                .stream()
                .map(Object::toString)
                .map(elementUtils::getTypeElement)
                .allMatch(this::checkThatClassNameIsCapitalized);
    }

    private boolean checkThatClassNameIsCapitalized(TypeElement classElement) {
        String className = classElement.getSimpleName().toString();
        if (Character.isLowerCase(className.charAt(0))) {
            String appropriateClassName = className.length() == 1 ? className.toUpperCase() :
                    Character.toUpperCase(className.charAt(0)) + className.substring(1);
            String errorMessage = String.format("The class '%s' should be named '%s'.",
                    className, appropriateClassName);
            processingEnvironment.getMessager().printMessage(
                    Diagnostic.Kind.NOTE, errorMessage, classElement);

            return false;
        }

        return true;
    }
}
