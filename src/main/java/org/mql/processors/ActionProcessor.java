package org.mql.processors;

import org.mql.processors.models.GeneratedAction;

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
@SupportedAnnotationTypes("org.mql.jee.annotations.Action")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class ActionProcessor extends AbstractProcessor {

    private Elements elementUtils;
    private TypeElement actionElement;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        elementUtils = processingEnv.getElementUtils();
        actionElement = processingEnv.getElementUtils().getTypeElement("org.mql.jee.annotations.Action");
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        roundEnv.getElementsAnnotatedWith(actionElement)
                .stream()
                .map(element -> ((TypeElement) element))
                .forEach(annotatedClass -> {
                    AnnotationMirror annotation = annotatedClass.getAnnotationMirrors()
                            .stream()
                            .filter(annotationMirror -> annotationMirror.getAnnotationType().asElement().equals(actionElement))
                            .findAny()
                            .get();

                    Map<? extends ExecutableElement, ? extends AnnotationValue> annotationMap =
                            elementUtils.getElementValuesWithDefaults(annotation);

                    String actionName = getAnnotationAttributeValue("value", annotationMap);
                    String defaultResult = getAnnotationAttributeValue("defaultResult", annotationMap);
                    GeneratedAction actionToBeGenerated = new GeneratedAction(actionName, defaultResult);

                    System.out.println(String.format("Generating action '%s' with result '%s'",
                            actionToBeGenerated.getName(), actionToBeGenerated.getResult()));
                });

        return false;
    }

    private String getAnnotationAttributeValue(String attributeName,
                                               Map<? extends ExecutableElement, ? extends AnnotationValue> annotationMap) {
        Set<? extends ExecutableElement> annotationAttributes = annotationMap.keySet();
        ExecutableElement attribute = annotationAttributes.stream()
                .filter(attr -> attr.getSimpleName().toString().equalsIgnoreCase(attributeName))
                .findAny()
                .get();

        return annotationMap.get(attribute)
                .getValue()
                .toString();
    }
}
