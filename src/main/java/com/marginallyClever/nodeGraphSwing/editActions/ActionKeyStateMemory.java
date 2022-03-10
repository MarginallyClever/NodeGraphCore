package com.marginallyClever.nodeGraphSwing.editActions;

import com.marginallyClever.nodeGraphSwing.KeyStateMemory;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ActionKeyStateMemory extends AbstractAction {
    private final KeyStateMemory memory;
    private final boolean onRelease;
    private final int keyCode;

    public ActionKeyStateMemory(KeyStateMemory memory,int keyCode, boolean onRelease) {
        super();
        this.memory=memory;
        this.onRelease = onRelease;
        this.keyCode = keyCode;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(onRelease) {
            memory.keyReleased(keyCode);
        } else {
            memory.keyPressed(keyCode);
        }
    }
}
