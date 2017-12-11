package org.mql.jee.web.action;

/**
 * A convenience class to implement the BasicAction interface without having to implement any of its methods.
 *
 * @author Mohammed Aouf ZOUAG, on 12/11/2017
 */
public abstract class AbstractAction implements BasicAction<String> {
    @Override
    public String add() {
        throw new UnsupportedOperationException("AbstractAction.add() isn't supported.");
    }

    @Override
    public String update() {
        throw new UnsupportedOperationException("AbstractAction.update() isn't supported.");
    }

    @Override
    public String delete() {
        throw new UnsupportedOperationException("AbstractAction.delete() isn't supported.");
    }

    @Override
    public String list() {
        throw new UnsupportedOperationException("AbstractAction.list() isn't supported.");
    }
}
