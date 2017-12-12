package org.mql.processors;

import org.mql.processors.generators.ActionGenerator;
import org.mql.processors.models.GeneratedAction;
import org.mql.utils.AnnotationUtils;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import java.util.Map;
import java.util.Set;

/**
 * @author Mohammed Aouf ZOUAG, on 12/11/2017
 */
@SupportedAnnotationTypes("org.mql.jee.annotations.ActionRequired")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class ActionProcessor extends AbstractProcessor {

    /**
     * The qualified name of the package under which the generated action classes will reside.
     */
    public static final String DEFAULT_ACTION_PACKAGE_NAME = "org.mql.generated.actions";
    public static final String DEFAULT_ACTION_NAME_SUFFIX = "Action";

    private Elements elementUtils;
    private TypeElement actionElement;

    private ActionGenerator actionGenerator;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        elementUtils = processingEnv.getElementUtils();
        actionElement = processingEnv.getElementUtils().getTypeElement("org.mql.jee.annotations.ActionRequired");

        actionGenerator = new ActionGenerator(processingEnv);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        roundEnv.getElementsAnnotatedWith(actionElement)
                .stream()
                .map(element -> ((TypeElement) element))
                .map(this::extractAction)
                .forEach(actionGenerator::generate);

        return false;
    }

    /**
     * @param modelElement the element corresponding to the annotated model class
     * @return a GeneratedAction instance that holds the information of the new action class to be generated.
     */
    private GeneratedAction extractAction(TypeElement modelElement) {
        String modelClassSimpleName = modelElement.getSimpleName().toString();
        String modelQualifiedClassName = modelElement.getQualifiedName().toString();

        AnnotationMirror annotation = modelElement.getAnnotationMirrors()
                .stream()
                .filter(annotationMirror -> annotationMirror.getAnnotationType().asElement().equals(actionElement))
                .findAny()
                .get();

        Map<? extends ExecutableElement, ? extends AnnotationValue> annotationMap =
                elementUtils.getElementValuesWithDefaults(annotation);

        String actionName = AnnotationUtils.getAttributeValue("value", annotationMap);

        if (actionName.isEmpty()) {
            // No name was provided, use a default one
            actionName = modelClassSimpleName + DEFAULT_ACTION_NAME_SUFFIX;
        }

        String defaultResult = AnnotationUtils.getAttributeValue("defaultResult", annotationMap);
        return new GeneratedAction(actionName, defaultResult, DEFAULT_ACTION_PACKAGE_NAME,
                modelClassSimpleName, modelQualifiedClassName);
    }
}
