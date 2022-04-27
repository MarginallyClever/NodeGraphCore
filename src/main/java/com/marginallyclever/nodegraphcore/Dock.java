package com.marginallyclever.nodegraphcore;

import com.marginallyclever.nodegraphcore.json.RectangleDAO4JSON;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.*;

/**
 * Describes an input or output connection with for a {@link Node} and stores the value at that connection.
 * @author Dan Royer
 * @since 2022-02-01
 */
public class Dock<T> {
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
     * @throws IllegalArgumentException if input and output are true at the same time.
     */
    protected Dock(String _name, Class<T> type, T startingValue, boolean _hasInput, boolean _hasOutput) throws IllegalArgumentException {
        super();
        if(hasInput && hasOutput) throw new IllegalArgumentException("Cannot be input and output at the same time!");
        this.type = type;
        this.name = _name;
        this.value = startingValue;
        this.hasInput = _hasInput;
        this.hasOutput = _hasOutput;
        this.rectangle.setBounds(0,0,DEFAULT_WIDTH,DEFAULT_HEIGHT);
    }

    /**
     * Creates a copy of this {@link Dock}, while flipping hasInput and hasOutput
     * @return an inverted copy of this {@link Dock}.
     */
    public Dock<T> createInverse() {
        return new Dock<>(name,type,value,!hasInput,!hasOutput);
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

    @Override
    public String toString() {
        return "NodeVariable{" +
                "name='" + name + '\'' +
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

    public JSONObject toJSON() throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put("value", DAO4JSONFactory.toJSON(this.type,value));
        jo.put("name",name);
        jo.put("hasInput",hasInput);
        jo.put("hasOutput",hasOutput);
        RectangleDAO4JSON dao = new RectangleDAO4JSON();
        jo.put("rectangle", dao.toJSON(rectangle));
        return jo;
    }

    @SuppressWarnings("unchecked")
    public void parseJSON(JSONObject jo) throws JSONException, ClassCastException {
        value = (jo.has("value") ? (T) DAO4JSONFactory.fromJSON(this.type,jo.get("value")) : null);
        name = jo.getString("name");
        hasInput = jo.getBoolean("hasInput");
        hasOutput = jo.getBoolean("hasOutput");
        RectangleDAO4JSON dao = new RectangleDAO4JSON();
        rectangle.setBounds(dao.fromJSON(jo.getJSONObject("rectangle")));
    }
}
