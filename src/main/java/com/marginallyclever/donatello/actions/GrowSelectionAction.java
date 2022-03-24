package com.marginallyclever.donatello.actions;

import com.marginallyclever.donatello.Donatello;
import com.marginallyclever.nodegraphcore.Node;
import com.marginallyclever.nodegraphcore.NodeConnection;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Select all the {@link com.marginallyclever.nodegraphcore.Node}s directly connected to the already selected nodes.
 */
public class GrowSelectionAction extends AbstractAction {
    /**
     * The editor being affected.
     */
    private final Donatello editor;

    /**
     * Constructor for subclasses to call.
     * @param name the name of this action visible on buttons and menu items.
     * @param editor the editor affected by this Action.
     */
    public GrowSelectionAction(String name, Donatello editor) {
        super(name);
        this.editor = editor;
    }

    /**
     * Updates the model and repaints the panel.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        List<Node> list = new ArrayList<>(editor.getSelectedNodes());
        List<Node> adjacent = new ArrayList<>();
        List<NodeConnection> potential = new ArrayList<>();

        for( NodeConnection c : editor.getGraph().getConnections() ) {
            for( Node n : list ) {
                if (c.isConnectedTo(n)) {
                    potential.add(c);
                    break;
                }
            }
        }

        for( Node n : editor.getGraph().getNodes() ) {
            if(list.contains(n)) continue;
            for( NodeConnection c : potential ) {
                if (c.isConnectedTo(n)) {
                    adjacent.add(n);
                }
            }
        }

        list.addAll(adjacent);
        editor.setSelectedNodes(list);
        editor.repaint();
    }
}
