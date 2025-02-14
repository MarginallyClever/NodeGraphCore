package com.marginallyclever.nodegraphcore.port;

import com.marginallyclever.nodegraphcore.Connection;
import com.marginallyclever.nodegraphcore.DAO4JSONFactory;
import com.marginallyclever.nodegraphcore.json.RectangleDAO4JSON;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.*;

/**
 * Nodes connect to each other through {@link Port}s linked by {@link Connection}s.
 * @param <T> the type of data that passes through this {@link Port}.
 * @author Dan Royer
 * @since 2022-02-01
 */
public abstract class Port<T> {
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

    protected final T defaultValue;

    /**
     * The type of the value stored in this variable.
     */
    protected final Class<T> type;

    /**
     * The name of this variable.  Change this value in production and serialization will break!
     */
    protected String name;

    /**
     * bounding rectangle of this variable.
     */
    protected final Rectangle rectangle = new Rectangle();

    protected boolean isDirty = false;

    /**
     * Constructor for subclasses to call.
     * @param name the variable name
     * @param type the variable type
     * @param startingValue the starting value
     * @throws IllegalArgumentException if input and output are true at the same time.
     */
    protected Port(String name, Class<T> type, T startingValue) throws IllegalArgumentException {
        super();
        this.type = type;
        this.name = name;
        this.value = startingValue;
        this.defaultValue = startingValue;
        this.rectangle.setBounds(0,0,DEFAULT_WIDTH,DEFAULT_HEIGHT);
    }

    /**
     * Creates a copy of this {@link Port}, while flipping hasInput and hasOutput
     * @return an inverted copy of this {@link Port}.
     */
    abstract public Port<T> createInverse();

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
     * Returns the class of this value type.
     * @return the class of this value type.
     */
    public Class<T> getType() {
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
     * @param arg0 the given item
     * @return true if the given item is an instance of this value's type.
     */
    public boolean isValidType(Object arg0) {
        if(arg0==null) return false;
        return type.isAssignableFrom(arg0.getClass());
    }

    /**
     * Sets the value.  Casts to this variable's type.
     * @param arg0 the new value to set.
     */
    @SuppressWarnings("unchecked")
    public void setValue(Object arg0) {
        if(!isValidType(arg0)) return;
        setDirtyOnValueChange(arg0);
        value = (T)arg0;
    }

    protected void setDirtyOnValueChange(Object arg0) {
        if(value!=null) {
            if(!value.equals(arg0)) isDirty = true;
        } else if(arg0 != null) {
            isDirty = true;
        }
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
                ", value=" + value +
                '}';
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
        RectangleDAO4JSON dao = new RectangleDAO4JSON();
        jo.put("rectangle", dao.toJSON(rectangle));
        return jo;
    }

    @SuppressWarnings("unchecked")
    public void fromJSON(JSONObject jo) throws JSONException, ClassCastException {
        value = jo.has("value")
                ? (T) DAO4JSONFactory.fromJSON(this.type,jo.get("value"))
                : null;
        name = jo.getString("name");
        RectangleDAO4JSON dao = new RectangleDAO4JSON();
        rectangle.setBounds(dao.fromJSON(jo.getJSONObject("rectangle")));
    }

    public boolean isDirty() {
        return isDirty;
    }

    public void setDirty(boolean state) {
        isDirty = state;
    }
}
