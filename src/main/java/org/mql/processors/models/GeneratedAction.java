package org.mql.processors.models;

import org.mql.processors.generators.Generatable;

/**
 * A class that holds the data for a new Action class to be generated.
 *
 * @author Mohammed Aouf ZOUAG, on 12/11/2017
 */
public class GeneratedAction implements Generatable {
    /**
     * The actionName of the action to be generated.
     */
    private String actionName;
    /**
     * The value to be returned as a actionResult of invoking the genreated class's action methods.
     */
    private String actionResult;
    /**
     * The qualified package actionName under which the generated action will reside.
     */
    private String qualifiedPackageName;
    /**
     * The simple class name of the model that is handled by the action class to be generated.
     */
    private String modelClassName;
    /**
     * The name of the model field on its corresponding action class.
     */
    private String modelName;
    /**
     * The name of the list of models' field on the corresponding action class.
     */
    private String modelListName;
    /**
     * The qualified name of the model class.
     */
    private String modelQualifiedClassName;

    public GeneratedAction(String actionName, String actionResult, String qualifiedPackageName,
                           String modelClassName, String modelQualifiedClassName) {
        this.actionName = actionName;
        this.actionResult = actionResult;
        this.qualifiedPackageName = qualifiedPackageName;
        this.modelClassName = modelClassName;
        this.modelQualifiedClassName = modelQualifiedClassName;

        modelName = modelClassName.substring(0, 1).toLowerCase() + modelClassName.substring(1);
        modelListName = modelName + "List";
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public String getActionResult() {
        return actionResult;
    }

    public void setActionResult(String actionResult) {
        this.actionResult = actionResult;
    }

    public String getQualifiedPackageName() {
        return qualifiedPackageName;
    }

    public void setQualifiedPackageName(String qualifiedPackageName) {
        this.qualifiedPackageName = qualifiedPackageName;
    }

    public String getModelClassName() {
        return modelClassName;
    }

    public void setModelClassName(String modelClassName) {
        this.modelClassName = modelClassName;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getModelListName() {
        return modelListName;
    }

    public void setModelListName(String modelListName) {
        this.modelListName = modelListName;
    }

    public String getModelQualifiedClassName() {
        return modelQualifiedClassName;
    }

    public void setModelQualifiedClassName(String modelQualifiedClassName) {
        this.modelQualifiedClassName = modelQualifiedClassName;
    }

    @Override
    public String toString() {
        return "GeneratedAction{" +
                "actionName='" + actionName + '\'' +
                ", actionResult='" + actionResult + '\'' +
                ", qualifiedPackageName='" + qualifiedPackageName + '\'' +
                ", modelClassName='" + modelClassName + '\'' +
                ", modelName='" + modelName + '\'' +
                ", modelListName='" + modelListName + '\'' +
                ", modelQualifiedClassName='" + modelQualifiedClassName + '\'' +
                '}';
    }
}
