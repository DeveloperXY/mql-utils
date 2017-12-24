package org.mql.processors;

import org.mql.processors.models.FailureSubject;
import org.mql.processors.models.Payload;
import org.mql.processors.sub.*;
import org.mql.utils.Annotations;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.partitioningBy;

/**
 * @author Mohammed Aouf ZOUAG, on 12/11/2017
 */
@SupportedAnnotationTypes("*")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class BestPracticesProcessor extends AbstractProcessor {
    /**
     * A flag indicating whether this processor had already processed annotations in a previous round.
     */
    private boolean hasProcessedAnnotations = false;

    private Elements elementUtils;
    private TypeElement bestPracticesElement;
    private RoundEnvironment roundEnv;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        elementUtils = processingEnv.getElementUtils();

        bestPracticesElement = elementUtils.getTypeElement(Annotations.BEST_PRACTICES);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        this.roundEnv = roundEnv;

        if (!roundEnv.errorRaised() && !roundEnv.processingOver()) {
            if (checksAreEnabled(roundEnv)) {
                displayMessage("Verifying MQL best practices...");
                runSubProcessors();

                // All annotations were processed
                hasProcessedAnnotations = true;
            } else {
                if (!hasProcessedAnnotations) {
                    displayMessage("MQL: Best practices mode is disabled.");
                    hasProcessedAnnotations = true;
                }
            }
        }
        return false;
    }

    private void runSubProcessors() {
        Map<Boolean, List<Payload>> payloadMap = Stream.of(
                new ModelSubProcessor(processingEnv, roundEnv),
                new ClassNameProcessor(processingEnv, roundEnv),
                new MethodNameProcessor(processingEnv, roundEnv),
                new ActionSubProcessor(processingEnv, roundEnv))
                .map(AbstractSubProcessor::run)
                .collect(partitioningBy(Payload::getStatus));

        payloadMap.get(true)
                .stream()
                .map(Payload::getSuccessMessage)
                .forEach(this::displayMessage);
        payloadMap.get(false)
                .stream()
                .map(Payload::getFailureSubjects)
                .flatMap(Collection::stream)
                .forEach(subject -> displayMessageWithElement(subject.getMessage(), subject.getElement()));
    }

    private void displayMessage(String message) {
        displayMessage(Diagnostic.Kind.NOTE, message, null);
    }

    private void displayMessage(Diagnostic.Kind kind, String message, Element element) {
        if (element == null)
            processingEnv.getMessager().printMessage(kind, message);
        else
            processingEnv.getMessager().printMessage(kind, message, element);
    }

    private void displayMessageWithElement(String message, Element element) {
        displayMessage(Diagnostic.Kind.NOTE, message, element);
    }

    /**
     * This method determines whether to activate the checks for the use of best practices or not,
     * based on the value specified within the @{@link org.mql.bestpractices.CheckForBestPractices} annotation.
     *
     * @param roundEnvironment
     * @return true if checks are enabled, false otherwise
     */
    private boolean checksAreEnabled(RoundEnvironment roundEnvironment) {
        Optional<? extends Element> optional = roundEnvironment.getElementsAnnotatedWith(bestPracticesElement)
                .stream()
                .findAny();

        if (!optional.isPresent())
            return false; // No @CheckForBestPractices annotation could be found

        Element annotatedPackage = optional.get();
        AnnotationMirror annotation = annotatedPackage.getAnnotationMirrors()
                .stream()
                .filter(annotationMirror -> annotationMirror.getAnnotationType()
                        .asElement().equals(bestPracticesElement))
                .findAny()
                .get();

        Map<? extends ExecutableElement, ? extends AnnotationValue> annotationMap =
                elementUtils.getElementValuesWithDefaults(annotation);

        return Boolean.valueOf(Annotations.getAttributeValue("enabled", annotationMap));
    }
}
