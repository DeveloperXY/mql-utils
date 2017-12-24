package org.mql.processors.sub;

import org.mql.processors.models.FailureSubject;
import org.mql.processors.models.Payload;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Set;

/**
 * @author Mohammed Aouf ZOUAG, on 12/13/2017
 */
public class ModelSubProcessor extends AbstractSubProcessor {

    private static final String MODELS_PACKAGE_NAME = "models";

    private TypeElement modelElement;

    public ModelSubProcessor(ProcessingEnvironment processingEnvironment, RoundEnvironment roundEnvironment) {
        super(processingEnvironment, roundEnvironment);

        modelElement = elementUtils.getTypeElement("org.mql.bestpractices.Model");
    }

    @Override
    public Payload run() {
        Set<? extends Element> annotatedElements = roundEnvironment.getElementsAnnotatedWith(modelElement);
        if (annotatedElements.size() == 0)
            return new Payload(false, "No @Model classes found.");

        return calculatePayload(() -> roundEnvironment.getElementsAnnotatedWith(modelElement)
                .stream()
                .map(element -> ((TypeElement) element))
                .allMatch(this::isModelClassWellPackaged));
    }

    @Override
    protected String getSuccessMessage() {
        return "All model classes are well-packaged.";
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
                        "For best practices, don't declare the '%s' class in the '%s' package. " +
                                "Instead, declare it within a '%s' package for the example. " +
                                "A package name ending with '%s' will indicate that it contains model classes only. " +
                                "That won't only make your code more readable, but more understandable too.",
                        modelSimpleName, hostPackageName, hostPackageName + "." + MODELS_PACKAGE_NAME, MODELS_PACKAGE_NAME);
                isWellPackaged = false;
            }
        }

        if (!isWellPackaged)
            failureSubjects.add(new FailureSubject(annotatedClass, errorMessage));

        return isWellPackaged;
    }
}
