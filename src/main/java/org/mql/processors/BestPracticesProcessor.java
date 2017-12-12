package org.mql.processors;

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

    private static final String MODELS_PACKAGE_NAME = "models";

    private Elements elementUtils;
    private TypeElement bestPracticesElement;
    private TypeElement modelElement;
    /**
     * A flag indicating whether this processor had already processed annotations in a previous round.
     */
    private boolean hasProcessedAnnotations = false;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        elementUtils = processingEnv.getElementUtils();

        bestPracticesElement = elementUtils.getTypeElement("org.mql.bestpractices.CheckForBestPractices");
        modelElement = elementUtils.getTypeElement("org.mql.bestpractices.Model");
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (!roundEnv.errorRaised() && !roundEnv.processingOver()) {
            if (checksAreEnabled(roundEnv)) {
                processingEnv.getMessager().printMessage(
                        Diagnostic.Kind.NOTE, "MQL: Best practices mode is enabled.");

                boolean allModelsAreWellPackaged = checkThatAllModelsAreWellPackaged(roundEnv);

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

    private boolean checkThatAllModelsAreWellPackaged(RoundEnvironment roundEnv) {
        return roundEnv.getElementsAnnotatedWith(modelElement)
                .stream()
                .map(element -> ((TypeElement) element))
                .allMatch(this::isModelClassWellPackaged);
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

    /**
     * Checks if the passed-in model class is declared within the {@value MODELS_PACKAGE_NAME} package.
     *
     * @param annotatedClass the model class to be checked
     * @return true if the class is well packaged, & false otherwise.
     */
    private boolean isModelClassWellPackaged(TypeElement annotatedClass) {
        boolean isWellPackaged = true;
        String errorMessage = "";
        String modelQualifiedName = annotatedClass.getQualifiedName().toString();
        String modelSimpleName = annotatedClass.getSimpleName().toString();

        if (modelSimpleName.equals(modelQualifiedName)) {
            // The annotated class is within the default package
            errorMessage = String.format(
                    "For best practices, don't declare the '%s' class in the default package. " +
                            "Declare it within a package whose name ends with '%s' instead. " +
                            "That's more trivial.",
                    modelSimpleName, MODELS_PACKAGE_NAME);
            isWellPackaged = false;
        } else {
            // The annotated class is declared within a package different than the default one
            // Get the host package's name
            String hostPackageName = modelQualifiedName.substring(0, modelQualifiedName.lastIndexOf("."));
            if (!hostPackageName.endsWith(MODELS_PACKAGE_NAME)) {
                // The model class is not declared within the appropriate package
                errorMessage = String.format(
                        "For best practices, don't declare the '%s' class in the '%s' package.\n" +
                                "Instead, declare it within a '%s' package for the example.\n" +
                                "A package name ending with '%s' will indicate that it contains model classes only.\n" +
                                "That won't only make your code more readable, but more understandable too.",
                        modelSimpleName, hostPackageName, hostPackageName + "." + MODELS_PACKAGE_NAME, MODELS_PACKAGE_NAME);
                isWellPackaged = false;
            }
        }

        if (!isWellPackaged)
            processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, errorMessage, annotatedClass);

        return isWellPackaged;
    }
}
