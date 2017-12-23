package org.mql.processors.sub;

import org.mql.bestpractices.NameSuffixedWith;
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

    private final TypeElement constructAnnotation;
    /**
     * The required suffix for the class elements to be checked.
     */
    private String suffix;

    public SuffixProcessor(ProcessingEnvironment processingEnvironment,
                           RoundEnvironment roundEnvironment, AnnotationMirror annotation) {
        super(processingEnvironment, roundEnvironment);
        this.constructAnnotation = (TypeElement) annotation.getAnnotationType().asElement();
        extractSuffixFrom(annotation);
    }

    private void extractSuffixFrom(AnnotationMirror annotation) {
        TypeElement nameSuffixedWithElement = elementUtils.getTypeElement(Annotations.NAME_SUFFIXED_WITH);
        AnnotationMirror nameSuffixedWithAnnotation = annotation.getAnnotationType()
                .asElement()
                .getAnnotationMirrors()
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
     * @param element the class element to be verified
     * @return true if the class name ends with {@code suffix}, false otherwise
     */
    private boolean checkThatClassIsAppropriatelySuffixed(Element element) {
        String className = element.getSimpleName().toString();
        if (className.endsWith(suffix))
            return true;

        String errorMessage = String.format("The class '%s' should be suffixed with '%s'.", className, suffix);
        processingEnvironment.getMessager().printMessage(Diagnostic.Kind.NOTE, errorMessage, element);
        return false;
    }
}
