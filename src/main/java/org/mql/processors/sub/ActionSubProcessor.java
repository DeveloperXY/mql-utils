package org.mql.processors.sub;

import org.mql.utils.Annotations;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.AnnotationMirror;

/**
 * @author Mohammed Aouf ZOUAG, on 12/23/2017
 */
public class ActionSubProcessor extends SuffixProcessor {
    public ActionSubProcessor(ProcessingEnvironment processingEnvironment,
                              RoundEnvironment roundEnvironment) {
        super(processingEnvironment, roundEnvironment,
                processingEnvironment.getElementUtils().getTypeElement(Annotations.ACTION));
    }

    @Override
    public String getSuccessMessage() {
        return "All action classes are well named.";
    }
}
