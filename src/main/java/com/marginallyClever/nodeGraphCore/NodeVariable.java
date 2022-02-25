package com.marginallyClever.nodeGraphCore;

import java.awt.*;

/**
 * Describes an input or output connection with for a {@link Node} and stores the value at that connection.
 * @author Dan Royer
 * @since 2022-02-01
 */
public class NodeVariable<T> {
    /**
     * Dimensions used for bounds calculations and intersection tests.
     */
    public static final int DEFAULT_WIDTH = 150;

    /**
     * Dimensions used for bounds calculations and intersection tests.
     */
    public static final int DEFAULT_HEIGHT = 20;

    /**
     * The value within this variable
     */
    protected T value;

    /**
     * The type of the value stored in this variable.
     */
    protected final Class<T> type;

    /**
     * The name of this variable.  Change this value in production and serialization will break!
     */
    protected String name;

    /**
     * does this variable have an input?
     */
    protected boolean hasInput;

    /**
     * does this variable have an output?
     */
    protected boolean hasOutput;

    /**
     * is this variable dirty?
     */
    protected boolean isDirty;

    /**
     * bounding rectangle of this variable.
     */
    protected final Rectangle rectangle = new Rectangle();

    /**
     * Constructor for subclasses to call.
     * @param _name the variable name
     * @param type the variable type
     * @param startingValue the starting value
     * @param _hasInput does this variable have an input?
     * @param _hasOutput does this variable have an input?
     */
    private NodeVariable(String _name,Class<T> type,T startingValue,boolean _hasInput,boolean _hasOutput) {
        super();
        this.type = type;
        this.name = _name;
        this.value = startingValue;
        this.hasInput = _hasInput;
        this.hasOutput = _hasOutput;
        this.isDirty = true;
        this.rectangle.setBounds(0,0,DEFAULT_WIDTH,DEFAULT_HEIGHT);
    }

    /**
     * Called to create a new instance of a NodeVariable.
     * @param name the variable name
     * @param clazz the variable type
     * @param startingValue the starting value
     * @param hasInput does this variable have an input?
     * @param hasOutput does this variable have an input?
     * @param <T> the class type.
     * @return the new instance.
     */
    public static <T> NodeVariable<T> newInstance(String name,Class<T> clazz,T startingValue,boolean hasInput,boolean hasOutput) {
        return new NodeVariable<>(name,clazz,startingValue,hasInput,hasOutput);
    }

    /**
     * Creates a copy of this {@link NodeVariable}, while flipping hasInput and hasOutput
     * @return an inverted copy of this {@link NodeVariable}.
     */
    public NodeVariable<T> createInverse() {
        return new NodeVariable<>(name,type,value,!hasInput,!hasOutput);
    }

    /**
     * Returns the bounding rectangle.
     * @return the bounding rectangle.
     */
    public Rectangle getRectangle() {
        return rectangle;
    }

    /**
     * Returns the name
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value.  Casts to this variable's type.
     * @param arg0 the new value to set.
     */
    @SuppressWarnings("unchecked")
    public void setValue(Object arg0) {
        if(isValidType(arg0)) {
            value = (T)arg0;
            isDirty = true;
        }
    }

    /**
     * Returns the class of this value type.
     * @return the class of this value type.
     */
    public Class<T> getTypeClass() {
        return type;
    }

    /**
     * Returns the simple name of the class of this value type.
     * @return the simple name of the class of this value type.
     */
    public String getTypeName() {
        return type.getSimpleName();
    }

    /**
     * Returns true if the given item is an instance of this value's type.
     * @param arg0 the given item
     * @return true if the given item is an instance of this value's type.
     */
    public boolean isValidType(Object arg0) {
        return type.isInstance(arg0);
    }

    /**
     * Returns the value.
     * @return the value.
     */
    public T getValue() {
        return value;
    }

    /**
     * Sets the dirty state.
     * @param state the new dirty state.
     */
    public void setIsDirty(boolean state) {
        isDirty=state;
    }

    /**
     * Returns the dirty state.
     * @return the dirty state.
     */
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

    /**
     * Returns true if this variable has an output
     * @return true if this variable has an output
     */
    public boolean getHasOutput() {
        return hasOutput;
    }

    /**
     * Returns true if this variable has an input
     * @return true if this variable has an input
     */
    public boolean getHasInput() {
        return hasInput;
    }

    /**
     * Returns the center of the input connection point of this variable
     * @return the center of the input connection point of this variable
     */
    public Point getInPosition() {
        return new Point((int)rectangle.getMinX(), rectangle.y+rectangle.height/2);
    }

    /**
     * Returns the center of the output connection point of this variable
     * @return the center of the output connection point of this variable
     */
    public Point getOutPosition() {
        return new Point((int)rectangle.getMaxX(), rectangle.y+rectangle.height/2);
    }
}
