package org.mql.processors.sub;

import org.mql.utils.Annotations;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;

/**
 * @author Mohammed Aouf ZOUAG, on 12/23/2017
 */
public class DaoSubProcessor extends SuffixProcessor {
    public DaoSubProcessor(ProcessingEnvironment processingEnvironment,
                           RoundEnvironment roundEnvironment) {
        super(processingEnvironment, roundEnvironment,
                processingEnvironment.getElementUtils().getTypeElement(Annotations.DAO));
    }

    @Override
    public String getSuccessMessage() {
        return "All DAO classes are well named.";
    }
}
