package com.marginallyclever.donatello;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;

public abstract class ModalTool extends MouseAdapter {

    public abstract void paint(Graphics g);

    public void attachKeyboardAdapter() {}
    public void detachKeyboardAdapter() {}

    public void attachMouseAdapter() {}
    public void detachMouseAdapter() {}

    public void restart() {}

    public abstract String getName();

    /**
     * Returns the {@link KeyStroke} associated with activating this tool
     * @return the {@link KeyStroke} associated with activating this tool
     */
    public KeyStroke getAcceleratorKey() {
        return null;
    }

    public Icon getSmallIcon() {
        return null;
    }
}
