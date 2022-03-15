package com.marginallyClever.nodeGraphSwing.actions;

import com.marginallyClever.nodeGraphSwing.Donatello;
import com.marginallyClever.nodeGraphSwing.ModalTool;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class SwapToolsAction extends AbstractAction {
    private final Donatello editor;
    private final ModalTool tool;

    public SwapToolsAction(Donatello editor, ModalTool tool) {
        super(tool.getName());
        this.editor = editor;
        this.tool = tool;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        editor.swapTool(tool);
    }
}
