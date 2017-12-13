package org.mql.processors;

import org.mql.processors.sub.ModelSubProcessor;
import org.mql.processors.sub.SubProcessor;
import org.mql.utils.AnnotationUtils;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * @author Mohammed Aouf ZOUAG, on 12/11/2017
 */
@SupportedAnnotationTypes({
        "org.mql.bestpractices.CheckForBestPractices",
        "org.mql.bestpractices.Model"
})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class BestPracticesProcessor extends AbstractProcessor {

    private Elements elementUtils;
    private TypeElement bestPracticesElement;
    /**
     * A flag indicating whether this processor had already processed annotations in a previous round.
     */
    private boolean hasProcessedAnnotations = false;
    private RoundEnvironment roundEnv;
    private SubProcessor modelSubProcessor;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        elementUtils = processingEnv.getElementUtils();

        bestPracticesElement = elementUtils.getTypeElement("org.mql.bestpractices.CheckForBestPractices");
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        this.roundEnv = roundEnv;

        if (!roundEnv.errorRaised() && !roundEnv.processingOver()) {
            if (checksAreEnabled(roundEnv)) {
                processingEnv.getMessager().printMessage(
                        Diagnostic.Kind.NOTE, "MQL: Best practices mode is enabled.");
                initializeSubProcessors();

                boolean allModelsAreWellPackaged = modelSubProcessor.run();

                if (allModelsAreWellPackaged) {
                    processingEnv.getMessager().printMessage(
                            Diagnostic.Kind.NOTE, "All model classes are well-packaged.");
                }

                // All annotations were processed
                hasProcessedAnnotations = true;
            } else {
                if (!hasProcessedAnnotations) {
                    processingEnv.getMessager().printMessage(
                            Diagnostic.Kind.NOTE, "MQL: Best practices mode is disabled.");

                    hasProcessedAnnotations = true;
                }
            }
        }
        return false;
    }

    private void initializeSubProcessors() {
        modelSubProcessor = new ModelSubProcessor(processingEnv, roundEnv);
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

        return Boolean.valueOf(AnnotationUtils.getAttributeValue("enabled", annotationMap));
    }
}
