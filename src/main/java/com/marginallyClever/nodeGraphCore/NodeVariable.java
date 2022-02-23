package com.marginallyClever.nodeGraphCore;

import java.awt.*;

/**
 * {@link NodeVariable}
 */
public class NodeVariable<T> {
    public static final int IN=1;
    public static final int OUT=2;
    public static final int DEFAULT_WIDTH = 150;
    public static final int DEFAULT_HEIGHT = 20;

    protected T value;
    protected final Class<T> type;

    protected String name;
    protected boolean hasInput;
    protected boolean hasOutput;

    protected boolean isDirty;
    protected final Rectangle rectangle = new Rectangle();

    private NodeVariable(String _name,Class<T> type,T defaultValue,boolean _hasInput,boolean _hasOutput) {
        super();
        this.type = type;
        this.name = _name;
        this.value = defaultValue;
        this.hasInput = _hasInput;
        this.hasOutput = _hasOutput;
        this.isDirty = true;
        this.rectangle.setBounds(0,0,DEFAULT_WIDTH,DEFAULT_HEIGHT);
    }

    public static <T> NodeVariable<T> newInstance(String name,Class<T> clazz,T defaultValue,boolean hasInput,boolean hasOutput) {
        return new NodeVariable<>(name,clazz,defaultValue,hasInput,hasOutput);
    }

    /**
     * Creates a copy of this {@link NodeVariable}, while flipping hasInput and hasOutput
     * @return an inverted copy of this {@link NodeVariable}.
     */
    public NodeVariable<T> createInverse() {
        return new NodeVariable<>(name,type,value,!hasInput,!hasOutput);
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public String getName() {
        return name;
    }

    @SuppressWarnings("unchecked")
    public void setValue(Object arg0) {
        if(isValidType(arg0)) {
            value = (T)arg0;
            isDirty = true;
        }
    }

    public Class<T> getTypeClass() {
        return type;
    }

    public String getTypeName() {
        return type.getSimpleName();
    }

    public boolean isValidType(Object arg0) {
        return type.isInstance(arg0);
    }

    public T getValue() {
        return value;
    }

    public void setIsDirty(boolean state) {
        isDirty=state;
    }

    public boolean getIsDirty() {
        return isDirty;
    }

    @Override
    public String toString() {
        return "NodeVariable{" +
                "name='" + name + '\'' +
                ", isDirty=" + isDirty +
                ", hasInput=" + hasInput +
                ", hasOutput=" + hasOutput +
                ", value=" + value +
                '}';
    }

    public boolean getHasOutput() {
        return hasOutput;
    }

    public boolean getHasInput() {
        return hasInput;
    }

    public Point getInPosition() {
        return new Point((int)rectangle.getMinX(), rectangle.y+rectangle.height/2);
    }

    public Point getOutPosition() {
        return new Point((int)rectangle.getMaxX(), rectangle.y+rectangle.height/2);
    }



}
