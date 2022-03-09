package com.marginallyClever.nodeGraphSwing.actions;

import com.marginallyClever.nodeGraphCore.Node;
import com.marginallyClever.nodeGraphCore.NodeGraph;
import com.marginallyClever.nodeGraphSwing.NodeFactoryPanel;
import com.marginallyClever.nodeGraphSwing.NodeGraphEditorPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Launches the "Add Node" dialog.  If the user clicks "Ok" then the selected {@link Node} type is added to the
 * current editor {@link NodeGraph}.
 * @author Dan Royer
 * @since 2022-02-21
 */
public class ActionAddNode extends AbstractAction {
    /**
     * The editor being affected.
     */
    private final NodeGraphEditorPanel editor;

    /**
     * Constructor for subclasses to call.
     * @param name the name of this action visible on buttons and menu items.
     * @param editor the editor affected by this Action.
     */
    public ActionAddNode(String name, NodeGraphEditorPanel editor) {
        super(name);
        this.editor = editor;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Node n = NodeFactoryPanel.runAsDialog((JFrame)SwingUtilities.getWindowAncestor(editor));
        if(n!=null) {
            n.setPosition(editor.getPopupPoint());
            editor.getGraph().add(n);
            editor.setSelectedNode(n);
            editor.repaint();
        }
    }
}
