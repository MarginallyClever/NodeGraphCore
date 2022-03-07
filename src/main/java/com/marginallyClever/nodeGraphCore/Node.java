package com.marginallyClever.nodeGraphCore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link Node} is a collection of zero or more inputs and zero or more outputs connected by some operator.
 * The operator is defined by extending the {@link Node} class and defining the {@code update()} method.
 * @author Dan Royer
 * @since 2022-02-01
 */
public abstract class Node {
    /**
     * The default height of the title bar.
     */
    public static final int TITLE_HEIGHT = 25;

    /**
     * This is used to ensure all {@link Node}s in a
     */
    private static int uniqueIDSource=0;

    private int uniqueID;

    private String name;

    private String label;

    private Rectangle rectangle;

    private final List<NodeVariable<?>> variables;

    /**
     * Default constructor
     * @param name the name of the class of this type of Node, for serialization and user selection.
     *             Developers of derived classes should not change this name after it is in production
     *             - it will break serialization.
     */
    protected Node(String name) {
        super();
        this.uniqueID = ++uniqueIDSource;
        this.name = name;
        this.label = "";
        this.rectangle = new Rectangle(0,0,150,50);
        this.variables = new ArrayList<>();
    }

    /**
     * Return one new instance of this type of {@link Node}.
     * Override this method in derived classes.
     * @return One new instance of this type of {@link Node}.
     */
    public abstract Node create();

    /**
     * Adjust the UniqueIDSource, the global number used to guarantee unique names for all classes.
     * Be very careful messing with this number!  It is exposed here for folding, unfolding, and serialization.
     * @param index the new value.
     */
    public static void setUniqueIDSource(int index) {
        uniqueIDSource=index;
    }

    /**
     * Returns the current uniqueIDSource.
     * @return the current uniqueIDSource.
     */
    public static int getUniqueIDSource() {
        return uniqueIDSource;
    }

    /**
     * Sets the unique ID of this Node.
     * @param id the new ID value.
     */
    public void setUniqueID(int id) {
        uniqueID=id;
    }

    /**
     * Returns the unique ID of this Node.
     * @return the unique ID of this Node.
     */
    public int getUniqueID() {
        return uniqueID;
    }

    /**
     * Returns the list of variables in this node.
     * @return the list of variables in this node.
     */
    public List<NodeVariable<?>> getVariables() {
        return variables;
    }

    /**
     * Sets the bounding rectangle for this node.
     * @param rectangle the new bounds.
     */
    public void setRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

    /**
     * Returns the bounding rectangle for this node.
     * @return the bounding rectangle for this node.
     */
    public Rectangle getRectangle() {
        return rectangle;
    }

    /**
     * Returns the name of this node.
     * @return the name of this node.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the unique name of this node, a combination of unique ID and name.
     * @return the unique name of this node, a combination of unique ID and name.
     */
    public String getUniqueName() {
        return uniqueID+"-"+name;
    }

    /**
     * Override this method to provide the custom behavior of this node.
     * Runs regardless of dirty inputs or outputs.
     */
    public abstract void update();

    /**
     * Runs {@link Node#update()} only if the node is considered dirty.  It is up to individual nodes to decide
     * if they are done (no longer dirty)
     */
    public void updateIfNotDirty() {
        if(!isDirty()) return;
        update();
    }

    /**
     * Recalculate the bounds of this node.
     */
    public void updateBounds() {
        int w=(int)rectangle.getWidth();
        int h=Node.TITLE_HEIGHT;
        int y=getRectangle().y;
        int x=getRectangle().x;
        for(NodeVariable v : variables) {
            Rectangle r = v.getRectangle();
            r.y=h+y;
            r.x=x;
            if(w < r.width) w = r.width;
            h += r.height;
        }
        rectangle.width=w;
        rectangle.height=h;
    }

    /**
     * Returns true if any input variables variables are dirty.
     * @return true if any input variables are dirty.
     */
    public boolean isDirty() {
        for(NodeVariable<?> v : variables) {
            if(v.getHasInput() && v.getIsDirty()) return true;
        }
        return false;
    }

    /**
     * Makes all input variables not dirty.
     */
    protected void cleanAllInputs() {
        for(NodeVariable<?> v : variables) {
            if(v.getHasInput()) v.setIsDirty(false);
        }
    }

    /**
     * Set all outputs to not dirty.
     */
    public void cleanAllOutputs() {
        for(NodeVariable<?> v : variables) {
            if(v.getHasOutput()) v.setIsDirty(false);
        }
    }

    /**
     * Add a {@link NodeVariable} to this node.
     * @param v the new {@link NodeVariable}
     */
    protected void addVariable(NodeVariable v) {
        variables.add(v);
    }

    /**
     * Remove a {@link NodeVariable} from this node.
     * @param v the old {@link NodeVariable}
     */
    protected void removeVariable(NodeVariable v) {
        variables.remove(v);
    }

    /**
     * Returns the number of variables in this node.
     * @return the number of variables in this node.
     */
    public int getNumVariables() {
        return variables.size();
    }

    /**
     * Get the i-th {@link NodeVariable} in this node.
     * @param index the index.
     * @return the i-th {@link NodeVariable} in this node.
     * @throws IndexOutOfBoundsException when an invalid index is requested.
     */
    public NodeVariable<?> getVariable(int index) throws IndexOutOfBoundsException {
        return variables.get(index);
    }

    @Override
    public String toString() {
        return "Node{" +
                "name=" + getName() +
                ", uniqueID=" + getUniqueID() +
                ", label=" + label +
                ", variables=" + variables +
                ", rectangle=" + rectangle +
                '}';
    }

    /**
     * Returns the center of the input connection point of the requested {@link NodeVariable}.
     * @param index the requested index
     * @return the center of the input connection point of the requested {@link NodeVariable}.
     */
    public Point getInPosition(int index) {
        Rectangle r = getRectangle();
        Point p = new Point(r.x,r.y+(int)getPointHeight(index));
        return p;
    }

    /**
     * Returns the center of the output connection point of the requested {@link NodeVariable}.
     * @param index the requested index
     * @return the center of the output connection point of the requested {@link NodeVariable}.
     */
    public Point getOutPosition(int index) {
        Rectangle r = getRectangle();
        Point p = new Point(r.x+r.width,r.y+(int)getPointHeight(index));
        return p;
    }

    private double getPointHeight(int index) {
        float y = TITLE_HEIGHT;
        Rectangle inr = getRectangle();
        for(int i=0;i<index;++i) {
            y += getVariable(i).getRectangle().height;
        }
        y += getVariable(index).getRectangle().height/2;
        return y;
    }

    /**
     * Returns the label (nickname) of this node.
     * @return the label (nickname) of this node.
     */
    public String getLabel() {
        return label;
    }

    /**
     * Sets the label for this node.
     * @param str the new label.
     */
    public void setLabel(String str) {
        label=str;
    }

    /**
     * Sets the top left corner of the {@link Node}'s rectangle.
     * @param point the new position of the top left corner.
     */
    public void setPosition(Point point) {
        rectangle.x=point.x;
        rectangle.y=point.y;
    }

    /**
     * Move this node some relative cartesian value.
     * @param dx the x axis amount.
     * @param dy the y axis amount.
     */
    public void moveRelative(int dx, int dy) {
        rectangle.x += dx;
        rectangle.y += dy;
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put("name",name);
        jo.put("uniqueID",uniqueID);
        jo.put("label", label);
        jo.put("rectangle", JSONHelper.rectangleToJSON(rectangle));
        jo.put("variables", getAllVariablesAsJSON());
        return jo;
    }

    private JSONArray getAllVariablesAsJSON() {
        JSONArray vars = new JSONArray();
        for(NodeVariable<?> v : variables) {
            vars.put(v.toJSON());
        }
        return vars;
    }

    public void parseJSON(JSONObject jo) throws JSONException {
        String joName = jo.getString("name");
        if(!name.equals(joName)) throw new JSONException("Node types do not match: "+name+", "+joName);

        uniqueID = jo.getInt("uniqueID");
        if(jo.has("label")) {
            String s = jo.getString("label");
            if(!s.equals("null")) label = s;
        }
        rectangle.setBounds(JSONHelper.rectangleFromJSON(jo.getJSONObject("rectangle")));
        parseAllVariablesFromJSON(jo.getJSONArray("variables"));
    }

    private void parseAllVariablesFromJSON(JSONArray vars) throws JSONException {
        guaranteeSameNumberOfVariables(vars);
        for(int i=0;i<vars.length();++i) {
            variables.get(i).parseJSON(vars.getJSONObject(i));
        }
    }

    private void guaranteeSameNumberOfVariables(JSONArray vars) throws JSONException {
        if(vars.length() != variables.size()) {
            int a = variables.size();
            int b = vars.length();
            throw new JSONException("JSON bad number of node variables.  Expected "+a+" found "+b);
        }
    }
}
