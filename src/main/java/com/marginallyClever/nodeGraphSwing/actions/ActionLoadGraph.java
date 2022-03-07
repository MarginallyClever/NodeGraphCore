package com.marginallyClever.nodeGraphSwing.actions;

import com.marginallyClever.nodeGraphCore.NodeGraph;
import com.marginallyClever.nodeGraphSwing.NodeGraphEditorPanel;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Launches a "select file to open" dialog and attempts to load the {@link NodeGraph} from disk.
 * @author Dan Royer
 * @since 2022-02-21
 */
public class ActionLoadGraph extends AbstractAction {
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
    public ActionLoadGraph(String name, NodeGraphEditorPanel editor) {
        super(name);
        this.editor = editor;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        fc.setFileFilter(NodeGraphEditorPanel.FILE_FILTER);
        if (fc.showOpenDialog(SwingUtilities.getWindowAncestor(editor)) == JFileChooser.APPROVE_OPTION) {
            try {
                editor.getGraph().add(loadGraphFromFile(fc.getSelectedFile().getAbsolutePath()));
                editor.repaint();
            } catch(IOException e1) {
                JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(editor),e1.getLocalizedMessage());
                e1.printStackTrace();
            }
        }
    }

    private NodeGraph loadGraphFromFile(String absolutePath) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(absolutePath)));
        StringBuilder responseStrBuilder = new StringBuilder();
        String inputStr;
        while ((inputStr = reader.readLine()) != null) {
            responseStrBuilder.append(inputStr);
        }
        NodeGraph newModel = new NodeGraph();
        newModel.parseJSON(new JSONObject(responseStrBuilder.toString()));
        return newModel;
    }
}
