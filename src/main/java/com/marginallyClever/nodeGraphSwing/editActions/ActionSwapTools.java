package com.marginallyClever.nodeGraphSwing.editActions;

import com.marginallyClever.nodeGraphSwing.ModalTool;
import com.marginallyClever.nodeGraphSwing.NodeGraphEditorPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ActionSwapTools extends AbstractAction {
    private final NodeGraphEditorPanel editor;
    private final ModalTool tool;

    public ActionSwapTools(NodeGraphEditorPanel editor, ModalTool tool) {
        super(tool.getName());
        this.editor = editor;
        this.tool = tool;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        editor.swapTool(tool);
    }
}
