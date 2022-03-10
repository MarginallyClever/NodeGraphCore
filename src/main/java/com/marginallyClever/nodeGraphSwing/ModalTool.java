package com.marginallyClever.nodeGraphSwing;

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
}
