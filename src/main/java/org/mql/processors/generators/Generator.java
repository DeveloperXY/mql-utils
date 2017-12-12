package org.mql.processors.generators;

/**
 * @author Mohammed Aouf ZOUAG, on 12/12/2017
 */
public interface Generator<T extends Generatable> {
    /**
     * Creates a Java source file corresponding to the passed in generatable.
     *
     * @param generatable to be generated
     */
    void generate(T generatable);
}
