package org.mql.jee.web.action;

/**
 * @param <T> the type of the result returned by the action's methods.
 * @author Mohammed Aouf ZOUAG, on 12/11/2017
 */
public interface AbstractAction<T> {
    T add();
    T update();
    T delete();
    T list();
}
