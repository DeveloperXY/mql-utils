package org.mql.processors.models;

/**
 * A class that holds the data for a new Action class to be generated.
 *
 * @author Mohammed Aouf ZOUAG, on 12/11/2017
 */
public class GeneratedAction {
    /**
     * The name of the action to be generated.
     */
    private String name;
    /**
     * The value to be returned as a result of invoking the genreated class's action methods.
     */
    private String result;
    /**
     * The qualified package name under which the generated action will reside.
     */
    private String qualifiedPackageName;

    public GeneratedAction(String name, String result, String qualifiedPackageName) {
        this.name = name;
        this.result = result;
        this.qualifiedPackageName = qualifiedPackageName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getQualifiedPackageName() {
        return qualifiedPackageName;
    }

    public void setQualifiedPackageName(String qualifiedPackageName) {
        this.qualifiedPackageName = qualifiedPackageName;
    }

    @Override
    public String toString() {
        return "GeneratedAction{" +
                "name='" + name + '\'' +
                ", result='" + result + '\'' +
                ", qualifiedPackageName='" + qualifiedPackageName + '\'' +
                '}';
    }
}
