package org.mql.processors;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.mql.processors.models.GeneratedAction;
import org.mql.utils.AnnotationUtils;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.util.Map;
import java.util.Properties;
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

    /**
     * The name of the velocity template used for the action classes to be generated.
     */
    public static final String VELOCITY_ACTION_CLASS_TEMPLATE_NAME = "actionclass.vm";
    public static final String VELOCITY_PROPERTIES_FILE_NAME = "velocity.properties";

    public static final String VELOCITY_KEY_FOR_PACKAGE_NAME = "packageName";
    public static final String VELOCITY_KEY_FOR_ACTION_CLASS_NAME = "actionClassName";
    public static final String VELOCITY_KEY_FOR_ACTION_RESULT = "actionResult";
    public static final String VELOCITY_KEY_FOR_MODEL_CLASS_NAME = "modelClassName";
    public static final String VELOCITY_KEY_FOR_MODEL_NAME = "modelName";
    public static final String VELOCITY_KEY_FOR_MODEL_LIST_NAME = "modelListName";
    public static final String VELOCITY_KEY_FOR_MODEL_QUALIFIED_CLASS_NAME = "modelQualifiedClassName";

    private Elements elementUtils;
    private TypeElement actionElement;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        elementUtils = processingEnv.getElementUtils();
        actionElement = processingEnv.getElementUtils().getTypeElement("org.mql.jee.annotations.ActionRequired");
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        roundEnv.getElementsAnnotatedWith(actionElement)
                .stream()
                .map(element -> ((TypeElement) element))
                .forEach(annotatedClass -> {
                    String modelClassSimpleName = annotatedClass.getSimpleName().toString();
                    String modelQualifiedClassName = annotatedClass.getQualifiedName().toString();

                    AnnotationMirror annotation = annotatedClass.getAnnotationMirrors()
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
                    GeneratedAction actionToBeGenerated = new GeneratedAction(actionName, defaultResult,
                            DEFAULT_ACTION_PACKAGE_NAME, modelClassSimpleName, modelQualifiedClassName);

                    generateAction(actionToBeGenerated);
                });

        return false;
    }

    /**
     * Creates a Java source file corresponding to the passed in action.
     *
     * @param action to be generated
     */
    private void generateAction(GeneratedAction action) {
        try {
            Properties properties = new Properties();
            URL url = getClass().getClassLoader().getResource(VELOCITY_PROPERTIES_FILE_NAME);
            properties.load(url.openStream());

            VelocityEngine velocityEngine = new VelocityEngine(properties);
            velocityEngine.init();

            VelocityContext velocityContext = new VelocityContext();
            velocityContext.put(VELOCITY_KEY_FOR_PACKAGE_NAME, action.getQualifiedPackageName());
            velocityContext.put(VELOCITY_KEY_FOR_ACTION_CLASS_NAME, action.getActionName());
            velocityContext.put(VELOCITY_KEY_FOR_ACTION_RESULT, action.getActionResult());
            velocityContext.put(VELOCITY_KEY_FOR_MODEL_CLASS_NAME, action.getModelClassName());
            velocityContext.put(VELOCITY_KEY_FOR_MODEL_NAME, action.getModelName());
            velocityContext.put(VELOCITY_KEY_FOR_MODEL_LIST_NAME, action.getModelListName());
            velocityContext.put(VELOCITY_KEY_FOR_MODEL_QUALIFIED_CLASS_NAME, action.getModelQualifiedClassName());

            Template velocityTemplate = velocityEngine.getTemplate(VELOCITY_ACTION_CLASS_TEMPLATE_NAME);

            JavaFileObject jfo = processingEnv.getFiler().createSourceFile(action.getActionName());
            processingEnv.getMessager().printMessage(
                    Diagnostic.Kind.NOTE, "creating source file: " + jfo.toUri());

            Writer writer = jfo.openWriter();
            processingEnv.getMessager().printMessage(
                    Diagnostic.Kind.NOTE, "applying velocity template: " + velocityTemplate.getName());
            velocityTemplate.merge(velocityContext, writer);
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
