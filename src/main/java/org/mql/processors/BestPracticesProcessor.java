package org.mql.processors;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.Set;

/**
 * @author Mohammed Aouf ZOUAG, on 12/11/2017
 */
@SupportedAnnotationTypes("org.mql.bestpractices.Model")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class BestPracticesProcessor extends AbstractProcessor {

    private static final String MODELS_PACKAGE_NAME = "models";

    private TypeElement modelElement;
    private boolean hasProcessedModels = false;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        modelElement = processingEnv.getElementUtils().getTypeElement("org.mql.bestpractices.Model");
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        if (!hasProcessedModels && roundEnv.getElementsAnnotatedWith(modelElement)
                .stream()
                .map(element -> ((TypeElement) element))
                .map(this::isModelClassWellPackaged)
                .noneMatch(status -> false)) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "All model classes are well-packaged.");
            hasProcessedModels = true;
        }

        return false;
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
