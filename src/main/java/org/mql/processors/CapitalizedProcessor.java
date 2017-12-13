package org.mql.processors;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

/**
 * An annotation processor that checks if all the classes are named correctly.
 *
 * @author Mohammed Aouf ZOUAG, on 12/11/2017
 */
@SupportedAnnotationTypes("*")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class CapitalizedProcessor extends AbstractProcessor {

    private Elements elementUtils;
    /**
     * A flag indicating whether this processor had already processed annotations in a previous round.
     */
    private boolean hasProcessedAnnotations = false;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        elementUtils = processingEnv.getElementUtils();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (!hasProcessedAnnotations) {
            List<TypeElement> allClassElements = roundEnv.getRootElements()
                    .stream()
                    .map(Object::toString)
                    .map(elementUtils::getTypeElement)
                    .collect(toList());

            boolean allGood = true;
            for (TypeElement classElement : allClassElements) {
                String className = classElement.getSimpleName().toString();
                if (Character.isLowerCase(className.charAt(0))) {
                    allGood = false;

                    String appropriateClassName = className.length() == 1 ? className.toUpperCase() :
                            Character.toUpperCase(className.charAt(0)) + className.substring(1);
                    String errorMessage = String.format("The class '%s' should be named '%s'.",
                            className, appropriateClassName);
                    processingEnv.getMessager().printMessage(
                            Diagnostic.Kind.NOTE, errorMessage, classElement);
                }
            }

            // All annotations were processed
            hasProcessedAnnotations = true;

            if (allGood)
                processingEnv.getMessager().printMessage(
                        Diagnostic.Kind.NOTE, "All classes are well named.");
        }

        return false;
    }
}
