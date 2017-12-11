package org.mql.processors.models;

/**
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

    public GeneratedAction(String name, String result) {
        this.name = name;
        this.result = result;
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

    @Override
    public String toString() {
        return "GeneratedAction{" +
                "name='" + name + '\'' +
                ", result='" + result + '\'' +
                '}';
    }
}
