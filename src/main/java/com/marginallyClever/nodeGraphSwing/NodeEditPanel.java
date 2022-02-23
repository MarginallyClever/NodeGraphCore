package com.marginallyClever.nodeGraphSwing;

import com.marginallyClever.nodeGraphCore.JSONHelper;
import com.marginallyClever.nodeGraphCore.Node;
import com.marginallyClever.nodeGraphCore.NodeVariable;
import com.marginallyClever.nodeGraphCore.builtInNodes.math.Add;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class NodeEditPanel extends JPanel {
    private final Node node;
    private final JTextField labelField = new JTextField();
    private final ArrayList<JTextField> fields = new ArrayList<>();

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

    private void addTextField(NodeVariable<?> variable,GridBagConstraints c) {
        c.gridx=0;  this.add(new JLabel(variable.getName()),c);
        c.gridx=1;  this.add(new JLabel(variable.getTypeName()),c);
        JTextField textField = new JTextField(variable.getValue().toString());
        c.gridx=2;  this.add(textField,c);
        c.gridy++;
        fields.add(textField);
    }

    private void addLabelField(GridBagConstraints c) {
        c.gridx=0;
        this.add(new JLabel("Label"),c);
        c.gridx=1;
        this.add(labelField,c);
        labelField.setText(node.getLabel());
        c.gridy++;
    }

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
     * @return a copy of the node with any alterations, if any.
     */
    public static void runAsDialog(Node subject,Frame frame) {
        NodeEditPanel panel = new NodeEditPanel(subject);
        if(JOptionPane.showConfirmDialog(frame,panel,"Edit "+subject.getName(),JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE) == JOptionPane.OK_OPTION) {
            subject.setLabel(panel.getLabel());
        }
    }

    private String getLabel() {
        return labelField.getText();
    }

    private static Node deepCopy(Node subject) {
        return JSONHelper.deepCopy(subject);
    }

    public static void main(String[] args) {
        // a test case
        Node node = new Add();
        NodeEditPanel.runAsDialog(node,null);
    }
}
