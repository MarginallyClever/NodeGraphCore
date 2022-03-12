package com.marginallyClever.nodeGraphSwing;

import com.marginallyClever.nodeGraphCore.BuiltInRegistry;
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
        BuiltInRegistry.register();
        SwingRegistry.register();

        NodeGraph model = new NodeGraph();
        NodeGraphEditorPanel panel = new NodeGraphEditorPanel(model);
        panel.setSystemLookAndFeel();

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
