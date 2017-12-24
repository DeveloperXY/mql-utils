package org.mql.processors.generators;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.mql.processors.models.GeneratedAction;
import org.mql.utils.MessagerUtils;

import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.util.Properties;

/**
 * @author Mohammed Aouf ZOUAG, on 12/12/2017
 */
public class ActionGenerator implements Generator<GeneratedAction> {
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

    private ProcessingEnvironment processingEnvironment;

    public ActionGenerator(ProcessingEnvironment pe) {
        processingEnvironment = pe;
    }

    @Override
    public void generate(GeneratedAction action) {
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

            JavaFileObject jfo = processingEnvironment.getFiler().createSourceFile(action.getActionName());
            MessagerUtils.displayMessage("creating source file: " + jfo.toUri());

            Writer writer = jfo.openWriter();
            velocityTemplate.merge(velocityContext, writer);
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
