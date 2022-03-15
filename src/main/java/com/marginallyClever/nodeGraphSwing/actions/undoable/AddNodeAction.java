package com.marginallyClever.nodeGraphSwing.actions.undoable;

import com.marginallyClever.nodeGraphCore.Node;
import com.marginallyClever.nodeGraphCore.NodeGraph;
import com.marginallyClever.nodeGraphSwing.AddNodePanel;
import com.marginallyClever.nodeGraphSwing.Donatello;
import com.marginallyClever.nodeGraphSwing.edits.AddNodeEdit;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Launches the "Add Node" dialog.  If the user clicks "Ok" then the selected {@link Node} type is added to the
 * current editor {@link NodeGraph}.
 * @author Dan Royer
 * @since 2022-02-21
 */
public class AddNodeAction extends AbstractAction {
    /**
     * The editor being affected.
     */
    private final Donatello editor;

    /**
     * Constructor for subclasses to call.
     * @param name the name of this action visible on buttons and menu items.
     * @param icon the small icon of this action visible on buttons and menu items.
     * @param editor the editor affected by this Action.
     */
    public AddNodeAction(String name, Donatello editor) {
        super(name);
        this.editor = editor;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Node n = AddNodePanel.runAsDialog((JFrame)SwingUtilities.getWindowAncestor(editor));
        if(n!=null) {
            n.setPosition(editor.getPopupPoint());
            editor.addEdit(new AddNodeEdit((String)this.getValue(Action.NAME),editor,n));
        }
    }
}
