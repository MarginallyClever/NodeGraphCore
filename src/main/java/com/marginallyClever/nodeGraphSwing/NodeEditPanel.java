package com.marginallyClever.nodeGraphSwing;

import com.marginallyClever.nodeGraphCore.JSONHelper;
import com.marginallyClever.nodeGraphCore.Node;
import com.marginallyClever.nodeGraphCore.NodeFactory;
import com.marginallyClever.nodeGraphCore.NodeVariable;
import com.marginallyClever.nodeGraphCore.builtInNodes.math.Add;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Swing UI allowing a user to edit an existing {@link Node}.
 * @author Dan Royer
 * @since 2022-02-23
 */
public class NodeEditPanel extends JPanel {
    /**
     * The {@link Node} being edited.
     */
    private final Node node;
    /**
     * The edit field for the label (nickname) of the {@link Node}.
     */
    private final JTextField labelField = new JTextField();
    /**
     * The fields being edited.
     */
    private final ArrayList<JTextField> fields = new ArrayList<>();

    /**
     * The Constructor for subclasses to call.
     * @param node the {@link Node} to edit.
     */
    public NodeEditPanel(Node node) {
        super();
        this.node=node;
        this.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.gridx=0;
        c.gridy=0;
        c.weightx=1;
        c.fill = GridBagConstraints.HORIZONTAL;

        addReadOnlyField(c,"Type",node.getName());
        addReadOnlyField(c,"ID",Integer.toString(node.getUniqueID()));
        addLabelField(c);

        for(int i=0;i<node.getNumVariables();++i) {
            addTextField(node.getVariable(i),c);
        }
    }

    /**
     * Adds one variable to the panel as a label/text field pair.
     * @param variable the {@link NodeVariable} to add.
     * @param c {@link GridBagConstraints} for placement.
     */
    private void addTextField(NodeVariable<?> variable,GridBagConstraints c) {
        c.gridx=0;  this.add(new JLabel(variable.getName()),c);
        c.gridx=1;  this.add(new JLabel(variable.getTypeName()),c);
        JTextField textField = new JTextField(variable.getValue().toString());
        c.gridx=2;  this.add(textField,c);
        c.gridy++;
        fields.add(textField);
    }

    /**
     * Adds the node 'label' field to the edit panel.
     * @param c {@link GridBagConstraints} for placement.
     */
    private void addLabelField(GridBagConstraints c) {
        c.gridx=0;
        this.add(new JLabel("Label"),c);
        c.gridx=1;
        this.add(labelField,c);
        labelField.setText(node.getLabel());
        c.gridy++;
    }

    /**
     * Adds one read-only label/value pair to the edit panel.
     * @param c {@link GridBagConstraints} for placement.
     * @param name the label
     * @param value the value
     */
    private void addReadOnlyField(GridBagConstraints c,String name,String value) {
        c.gridx=0;
        this.add(new JLabel(name),c);
        c.gridx=1;
        JLabel v = new JLabel(value);
        v.setEnabled(false);
        this.add(v,c);
        c.gridy++;
    }

    /**
     * Displays an edit dialog for a given node.  Returns a copy of the node with any alterations, if any.
     * @param subject the node to edit.
     * @param frame the parent frame.
     */
    public static void runAsDialog(Node subject,Frame frame) {
        NodeEditPanel panel = new NodeEditPanel(subject);
        if(JOptionPane.showConfirmDialog(frame,panel,"Edit "+subject.getName(),JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE) == JOptionPane.OK_OPTION) {
            subject.setLabel(panel.getLabel());
        }
    }

    /**
     * Returns the value of the label field
     * @return the value of the label field
     */
    private String getLabel() {
        return labelField.getText();
    }

    /**
     * Performs a deep copy of the subject
     * @param subject the node to copy.
     * @return the copy.
     */
    private static Node deepCopy(Node subject) {
        return JSONHelper.deepCopy(subject);
    }

    /**
     * main entry point.  Good for independent test.
     * @param args command line arguments.
     */
    public static void main(String[] args) {
        // a test case
        Node node = new Add();
        NodeEditPanel.runAsDialog(node,null);
    }
}
