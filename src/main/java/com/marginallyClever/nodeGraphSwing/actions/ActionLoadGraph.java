package com.marginallyClever.nodeGraphSwing.actions;

import com.marginallyClever.nodeGraphCore.JSONHelper;
import com.marginallyClever.nodeGraphCore.NodeGraph;
import com.marginallyClever.nodeGraphSwing.NodeGraphEditorPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class ActionLoadGraph extends AbstractAction {
    private final NodeGraphEditorPanel editor;
    private final JFileChooser fc = new JFileChooser();

    public ActionLoadGraph(String name, NodeGraphEditorPanel editor) {
        super(name);
        this.editor = editor;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        fc.setFileFilter(NodeGraphEditorPanel.FILE_FILTER);
        if (fc.showOpenDialog(SwingUtilities.getWindowAncestor(editor)) == JFileChooser.APPROVE_OPTION) {
            editor.getGraph().add(loadModelFromFile(fc.getSelectedFile().getAbsolutePath()));
        }
    }

    private NodeGraph loadModelFromFile(String absolutePath) {
        NodeGraph newModel;
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(absolutePath)))) {
            StringBuilder responseStrBuilder = new StringBuilder();
            String inputStr;
            while ((inputStr = reader.readLine()) != null)
                responseStrBuilder.append(inputStr);
            newModel = JSONHelper.getDefaultGson().fromJson(responseStrBuilder.toString(), NodeGraph.class);
        } catch(IOException e) {
            JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(editor),e.getLocalizedMessage());
            e.printStackTrace();
            newModel = new NodeGraph();
        }
        return newModel;
    }
}
