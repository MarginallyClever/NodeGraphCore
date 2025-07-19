package com.marginallyclever.nodegraphcore.dynamic;

import javax.swing.*;
import java.awt.*;

/**
 * The main application class.  Uses Java Swing to create a GUI for querying Java classes and methods dynamically.
 * Could be useful for exposing all Java classes and methods to a node graph system like Donatello.
 */
public class DynamicMethodNodeApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Create the main application frame
            JFrame frame = new JFrame("Java Reflection Query");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // Add the SearchPanel to the frame
            frame.getContentPane().add(new ClassSearchPanel());

            // Display the frame
            frame.setMinimumSize(new Dimension(400, 300));
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}