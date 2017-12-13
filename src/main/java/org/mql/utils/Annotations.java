package org.mql.utils;

import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import java.util.Map;
import java.util.Set;

/**
 * @author Mohammed Aouf ZOUAG, on 12/11/2017
 */
public class Annotations {

    public static final String BEST_PRACTICES = "org.mql.bestpractices.CheckForBestPractices";
    public static final String MODEL = "org.mql.bestpractices.Model";
    public static final String ACTION_REQUIRED = "org.mql.jee.annotations.ActionRequired";

    /**
     * Retrieves the value of an attribute from the map of the annotation's values.
     *
     * @param attributeName the name of the attribute to lookup
     * @param annotationMap the map that holds the attributes and their associated values
     * @return the value of the requested attribute
     */
    public static String getAttributeValue(String attributeName,
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

    public static Integer getIntAttributeValue(String attributeName,
                                           Map<? extends ExecutableElement, ? extends AnnotationValue> annotationMap) {
        String attributeValue = getAttributeValue(attributeName, annotationMap);
        try {
            return Integer.parseInt(attributeValue);
        } catch (NumberFormatException e) {
            throw new IllegalStateException(String.format("The attribute '%s' is not an integer."));
        }
    }
}
