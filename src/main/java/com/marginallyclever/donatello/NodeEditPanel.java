package com.marginallyclever.donatello;

import com.marginallyclever.nodegraphcore.Node;
import com.marginallyclever.nodegraphcore.NodeVariable;
import com.marginallyclever.nodegraphcore.corenodes.math.Add;

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
        c.gridy++;
        addReadOnlyField(c,"ID",Integer.toString(node.getUniqueID()));
        c.gridy++;
        addLabelField(c);
        c.gridy++;

        for(int i=0;i<node.getNumVariables();++i) {
            addVariableField(node.getVariable(i),c);
            c.gridy++;
        }
    }

    private void addVariableField(NodeVariable<?> variable,GridBagConstraints c) {
        if(variable.getValue() instanceof Number || variable.getValue() instanceof String) {
            addTextField(variable,c);
        } else {
            addReadOnlyField(c,variable.getName(),variable.getTypeName());
        }
    }

    /**
     * Adds one variable to the panel as a label/text field pair.
     * @param variable the {@link NodeVariable} to add.
     * @param c {@link GridBagConstraints} for placement.
     */
    private void addTextField(NodeVariable<?> variable,GridBagConstraints c) {
        c.anchor = GridBagConstraints.LINE_START;
        c.gridx=0;
        this.add(new JLabel(variable.getName()),c);

        JTextField textField = new JTextField(variable.getValue().toString());
        fields.add(textField);
        c.anchor = GridBagConstraints.LINE_END;
        c.gridx=1;
        this.add(textField,c);
    }

    /**
     * Adds the node 'label' field to the edit panel.
     * @param c {@link GridBagConstraints} for placement.
     */
    private void addLabelField(GridBagConstraints c) {
        c.anchor = GridBagConstraints.LINE_START;
        c.gridx=0;
        this.add(new JLabel("Label"),c);

        c.anchor = GridBagConstraints.LINE_END;
        c.gridx=1;
        labelField.setText(node.getLabel());
        this.add(labelField,c);
    }

    /**
     * Adds one read-only label/value pair to the edit panel.
     * @param c {@link GridBagConstraints} for placement.
     * @param name the label
     * @param value the value
     */
    private void addReadOnlyField(GridBagConstraints c,String name,String value) {
        c.anchor = GridBagConstraints.LINE_START;
        c.gridx=0;
        this.add(new JLabel(name),c);

        c.anchor = GridBagConstraints.LINE_END;
        c.gridx=1;
        JLabel v = new JLabel(value);
        v.setEnabled(false);
        this.add(v,c);
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
            readAllFields(subject,panel);
        }
    }

    private static void readAllFields(Node subject,NodeEditPanel panel) {
        int j=0;
        for(int i=0;i<subject.getNumVariables();++i) {
            NodeVariable<?> v = subject.getVariable(i);
            if(v.getValue() instanceof Number || v.getValue() instanceof String) {
                panel.readTextField(j++, subject.getVariable(i));
            }
        }
    }

    private void readTextField(int index,NodeVariable<?> variable) {
        JTextField f = fields.get(index);
        if(f==null) {
            // TODO ???
            return;
        }

        if(variable.getValue() instanceof Number) {
            variable.setValue(Double.parseDouble(f.getText()));
        } else if(variable.getValue() instanceof String) {
            variable.setValue(f.getText());
        } else {
            // TODO ???
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
     * main entry point.  Good for independent test.
     * @param args command line arguments.
     */
    public static void main(String[] args) {
        // a test case
        Node node = new Add();
        NodeEditPanel.runAsDialog(node,null);
    }
}
