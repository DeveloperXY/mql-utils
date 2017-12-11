package org.mql.jee.web.action;

/**
 * A convenience class to implement the BasicAction interface without having to implement any of its methods.
 *
 * @author Mohammed Aouf ZOUAG, on 12/11/2017
 */
public abstract class AbstractAction<T> implements BasicAction<T> {
    @Override
    public T add() {
        throw new UnsupportedOperationException("AbstractAction.add() isn't supported.");
    }

    @Override
    public T update() {
        throw new UnsupportedOperationException("AbstractAction.update() isn't supported.");
    }

    @Override
    public T delete() {
        throw new UnsupportedOperationException("AbstractAction.delete() isn't supported.");
    }

    @Override
    public T list() {
        throw new UnsupportedOperationException("AbstractAction.list() isn't supported.");
    }
}
