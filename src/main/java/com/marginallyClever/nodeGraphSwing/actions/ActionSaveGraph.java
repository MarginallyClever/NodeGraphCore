package com.marginallyClever.nodeGraphSwing.actions;

import com.marginallyClever.nodeGraphCore.JSONHelper;
import com.marginallyClever.nodeGraphCore.NodeGraph;
import com.marginallyClever.nodeGraphSwing.NodeGraphEditorPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;

/**
 * Launches a "select file to save" dialog and attempts to save the {@link NodeGraph} to disk.
 * @author Dan Royer
 * @since 2022-02-21
 */
public class ActionSaveGraph extends AbstractAction {
    /**
     * The editor being affected.
     */
    private final NodeGraphEditorPanel editor;

    /**
     * The file chooser remembers the last path.
     */
    private final JFileChooser fc = new JFileChooser();

    /**
     * Constructor for subclasses to call.
     * @param name the name of this action visible on buttons and menu items.
     * @param editor the editor affected by this Action.
     */
    public ActionSaveGraph(String name, NodeGraphEditorPanel editor) {
        super(name);
        this.editor = editor;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        fc.setFileFilter(NodeGraphEditorPanel.FILE_FILTER);
        if (fc.showSaveDialog(SwingUtilities.getWindowAncestor(editor)) == JFileChooser.APPROVE_OPTION) {
            String name = addExtensionIfNeeded(fc.getSelectedFile().getAbsolutePath());
            saveModelToFile(name);
        }
    }

    private String addExtensionIfNeeded(String s) {
        int last = s.lastIndexOf(".");
        String[] extensions = NodeGraphEditorPanel.FILE_FILTER.getExtensions();
        if(last == -1) {
            // no extension at all
            return s + "." + extensions[0];
        }

        String end = s.substring(last,s.length());
        for(String ext : extensions) {
            // has valid extension
            if(end.equals(ext)) return s;
        }
        // no matching extension
        return s + "." + extensions[0];
    }

    private void saveModelToFile(String absolutePath) {
        try(BufferedWriter w = new BufferedWriter(new FileWriter(absolutePath))) {
            w.write(JSONHelper.getDefaultGson().toJson(editor.getGraph()));
        } catch(Exception e) {
            JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(editor),e.getLocalizedMessage());
            e.printStackTrace();
        }
    }
}
