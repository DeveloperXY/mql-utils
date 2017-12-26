package org.mql.processors;

import org.mql.processors.models.Payload;
import org.mql.processors.sub.*;
import org.mql.utils.Annotations;
import org.mql.utils.MessagerUtils;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.util.Elements;
import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.partitioningBy;
import static org.mql.utils.MessagerUtils.displayMessage;
import static org.mql.utils.MessagerUtils.displayMessageWithElement;

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
            MessagerUtils.init(processingEnv);

            if (checksAreEnabled(roundEnv)) {
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
                new ActionSubProcessor(processingEnv, roundEnv),
                new ServiceSubProcessor(processingEnv, roundEnv))
                .map(AbstractSubProcessor::run)
                .collect(partitioningBy(Payload::getStatus));

        // Process the payloads of the sub processors that ran successfully
        StringJoiner sj = new StringJoiner("\n")
                .add("Verifying MQL's best practices...")
                .add("")
                .add("-------------- SUCCESSFUL CHECKS --------------");
        payloadMap.get(true)
                .stream()
                .map(Payload::getSuccessMessage)
                .forEach(sj::add);
        // Process the payloads of the sub processors that encountered a best practices violation
        List<Payload> failedPayloads = payloadMap.get(false);
        if (failedPayloads.size() == 0) {
            sj.add("-----------------------------------------------");
            displayMessage(sj.toString());
        }
        else {
            sj.add("").add("-------------- WHAT'S WRONG --------------");
            displayMessage(sj.toString());
            failedPayloads.stream()
                    .map(Payload::getFailureSubjects)
                    .flatMap(Collection::stream)
                    .forEach(subject -> displayMessageWithElement(subject.getMessage(), subject.getElement()));
        }
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
