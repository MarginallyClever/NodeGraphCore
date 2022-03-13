package com.marginallyClever.nodeGraphSwing;

import com.marginallyClever.nodeGraphCore.JSON_DAO_Factory;
import com.marginallyClever.nodeGraphCore.NodeFactory;
import com.marginallyClever.nodeGraphCore.NodeGraph;

import javax.swing.*;
import java.awt.*;

/**
 * Launch {@link NodeGraphEditorPanel}.
 * @author Dan Royer
 * @since 2022-02-01
 */
public class Donatello {
    public static void main(String[] args) {
        NodeFactory.loadRegistries();
        JSON_DAO_Factory.loadRegistries();

        NodeGraphEditorPanel.setSystemLookAndFeel();

        NodeGraphEditorPanel panel = new NodeGraphEditorPanel(new NodeGraph());

        JFrame frame = new JFrame("Donatello");
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(new Dimension(1200,800));
        frame.setLocationRelativeTo(null);
        frame.add(panel);
        panel.setupMenuBar();
        frame.setVisible(true);
    }
}
