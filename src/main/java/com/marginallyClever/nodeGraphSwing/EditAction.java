package com.marginallyClever.nodeGraphSwing;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * {@link AbstractAction}s in the {@link NodeGraphEditorPanel} implement this interface to update their own
 * {@link AbstractAction#setEnabled(boolean)} status.
 * @author Dan Royer
 * @since 2022-02-23
 */
public interface EditAction {
    /**
     * Called by the {@link NodeGraphEditorPanel} when the editor believes it is time to confirm enable status.
     */
    void updateEnableStatus();
}
