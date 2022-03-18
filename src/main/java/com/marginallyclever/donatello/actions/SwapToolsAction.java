package com.marginallyclever.donatello.actions;

import com.marginallyclever.donatello.Donatello;
import com.marginallyclever.donatello.ModalTool;

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
