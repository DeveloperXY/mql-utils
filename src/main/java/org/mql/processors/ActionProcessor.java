package org.mql.processors;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import java.util.Set;

/**
 * @author Mohammed Aouf ZOUAG, on 12/11/2017
 */
@SupportedAnnotationTypes("org.mql.jee.annotations.Action")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class ActionProcessor extends AbstractProcessor {

    private TypeElement actionElement;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        actionElement = processingEnv.getElementUtils().getTypeElement("org.mql.jee.annotations.Action");
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        roundEnv.getElementsAnnotatedWith(actionElement)
                .stream()
                .map(element -> ((TypeElement) element))
                .forEach(clazz -> System.out.println(clazz.getSimpleName().toString() + " is annotated with @Action"));

        return false;
    }
}
