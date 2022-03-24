package com.marginallyclever.donatello.actions;

import com.marginallyclever.donatello.Donatello;
import com.marginallyclever.donatello.modaltools.ContextSensitiveTool;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class SwapToolsAction extends AbstractAction {
    private final Donatello editor;
    private final ContextSensitiveTool tool;

    public SwapToolsAction(Donatello editor, ContextSensitiveTool tool) {
        super(tool.getName());
        this.editor = editor;
        this.tool = tool;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        editor.swapTool(tool);
    }
}
