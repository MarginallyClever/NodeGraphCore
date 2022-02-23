package com.marginallyClever.nodeGraphSwing;

import com.marginallyClever.nodeGraphCore.NodeFactory;
import com.marginallyClever.nodeGraphCore.Node;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.atomic.AtomicReference;

public class NodeFactoryPanel extends JPanel {
    private final JComboBox<String> names = new JComboBox<>(NodeFactory.getNames());
    private final JButton confirmButton = new JButton("Add");

    public NodeFactoryPanel() {
        super(new BorderLayout());
        this.add(names,BorderLayout.CENTER);
        this.add(confirmButton,BorderLayout.SOUTH);
    }

    public static Node runAsDialog(Frame frame) {
        JDialog dialog = new JDialog(frame,"Add Node", Dialog.ModalityType.DOCUMENT_MODAL);

        final AtomicReference<Node> result = new AtomicReference<Node>();

        final NodeFactoryPanel panel = new NodeFactoryPanel();
        panel.confirmButton.addActionListener((e)->{
            try {
                result.set(NodeFactory.createNode((String) panel.names.getSelectedItem()));
                dialog.dispose();
            } catch(IllegalArgumentException e1) {
                e1.printStackTrace();
            }
        });

        dialog.add(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);

        return result.get();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame(NodeFactoryPanel.class.getSimpleName());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        NodeFactoryPanel.runAsDialog(frame);
    }
}
