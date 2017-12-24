package org.mql.processors.sub;

import org.mql.utils.Annotations;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.*;
import javax.tools.Diagnostic;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A processor that operates based on an internal construct annotation that provides
 * the suffix to be checked for.
 *
 * @author Mohammed Aouf ZOUAG, on 12/23/2017
 */
public class SuffixProcessor extends AbstractSubProcessor {

    /**
     * The annotation from which to extract the suffix.
     */
    private TypeElement constructAnnotation;
    /**
     * The required suffix for the class elements to be checked.
     */
    private String suffix;

    public SuffixProcessor(ProcessingEnvironment processingEnvironment,
                           RoundEnvironment roundEnvironment, TypeElement annotation) {
        super(processingEnvironment, roundEnvironment);
        this.constructAnnotation = annotation;
        extractSuffixFrom(annotation);
    }

    private void extractSuffixFrom(TypeElement annotation) {
        TypeElement nameSuffixedWithElement = elementUtils.getTypeElement(Annotations.NAME_SUFFIXED_WITH);
        AnnotationMirror nameSuffixedWithAnnotation = annotation.getAnnotationMirrors()
                .stream()
                .filter(ann -> ann.getAnnotationType().asElement().equals(nameSuffixedWithElement))
                .findAny()
                .get();

        Map<? extends ExecutableElement, ? extends AnnotationValue> annotationMap =
                nameSuffixedWithAnnotation.getElementValues();

        suffix = Annotations.getAttributeValue("value", annotationMap);
    }

    @Override
    public boolean run() {
        return roundEnvironment.getElementsAnnotatedWith(constructAnnotation)
                .stream()
                .map(this::checkThatClassIsAppropriatelySuffixed)
                .collect(Collectors.toList())
                .stream()
                .allMatch(result -> result);
    }

    /**
     * @param classElement the class element to be verified
     * @return true if the class name ends with {@code suffix}, false otherwise
     */
    private boolean checkThatClassIsAppropriatelySuffixed(Element classElement) {
        String className = classElement.getSimpleName().toString();
        if (className.endsWith(suffix))
            return true;

        String errorMessage = String.format("The class '%s' should be suffixed with '%s'.", className, suffix);
        processingEnvironment.getMessager().printMessage(Diagnostic.Kind.NOTE, errorMessage, classElement);
        return false;
    }

    public TypeElement getConstructAnnotation() {
        return constructAnnotation;
    }

    public void setConstructAnnotation(TypeElement constructAnnotation) {
        this.constructAnnotation = constructAnnotation;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
}
