package com.marginallyClever.nodeGraphSwing;

import com.marginallyClever.nodeGraphCore.*;
import com.marginallyClever.nodeGraphSwing.editActions.*;
import com.marginallyClever.nodeGraphSwing.modalTools.ConnectionEditTool;
import com.marginallyClever.nodeGraphSwing.modalTools.NodeMoveTool;
import com.marginallyClever.nodeGraphSwing.modalTools.RectangleSelectTool;

import javax.swing.*;
import javax.swing.event.UndoableEditEvent;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link NodeGraphEditorPanel} is a Graphic User Interface to edit a {@link NodeGraph}.
 * @author Dan Royer
 * @since 2022-02-01
 */
public class NodeGraphEditorPanel extends JPanel {
    /**
     * Used by save and load actions
      */
    public static final FileNameExtensionFilter FILE_FILTER = new FileNameExtensionFilter("Node Graph","graph");

    /**
     * The {@link NodeGraph} to edit.
     */
    public final NodeGraph model;

    /**
     * The panel into which the {@link NodeGraph} will be painted.
     */
    private final NodeGraphViewPanel paintArea;

    /**
     * The currently selected nodes for group operations
     */
    private final List<Node> selectedNodes = new ArrayList<>();

    /**
     * Store copied nodes in this buffer.  Could be a user-space file instead.
     */
    private final NodeGraph copiedGraph = new NodeGraph();

    private final UndoManager undoManager = new UndoManager();
    private final ActionUndo actionUndo = new ActionUndo(undoManager);
    private final ActionRedo actionRedo = new ActionRedo(undoManager);
    private final UndoHandler undoHandler = new UndoHandler(undoManager,actionUndo,actionRedo);

    /**
     * The toolBar is where the user can switch between tools.
     */
    private final JToolBar toolBar = new JToolBar();

    /**
     * The popupBar appears when the user right clicks in the paintArea.  It contains all actions that affect one or
     * more {@link Node}s within the model.
     */
    private final JPopupMenu popupBar = new JPopupMenu();

    /**
     * The list of actions registered in the editor.  This list is used for calls to
     * {@link #updateActionEnableStatus()}.
     */
    private final ArrayList<AbstractAction> actions = new ArrayList<>();

    /**
     * The list of modal tools, only one of which can be active at any time.
     */
    private final ArrayList<ModalTool> tools = new ArrayList<>();

    /**
     * The active tool from the list of tools.
     */
    private ModalTool activeTool;

    /**
     * cursor position when the popup menu happened.
     */
    private final Point popupPoint = new Point();

    /**
     * Default constructor
     * @param model the {@link NodeGraph} to edit.
     */
    public NodeGraphEditorPanel(NodeGraph model) {
        super(new BorderLayout());
        this.model = model;

        paintArea = new NodeGraphViewPanel(model);

        this.add(toolBar,BorderLayout.NORTH);
        this.add(new JScrollPane(paintArea),BorderLayout.CENTER);

        setupTools();
        setupPaintArea();

        attachMouseAdapter();

        setupMenuBar();

        setSelectedNodes(null);
        updateActionEnableStatus();
    }

    /**
     * Sets up the editor as a {@link NodeGraphViewListener} so that it can add editor-specific decorations to the
     * painted nodes.
     */
    private void setupPaintArea() {
        paintArea.addViewListener((g,e)->{
            highlightSelectedNodes(g);
            activeTool.paint(g);
        });
        paintArea.updatePaintAreaBounds();
        paintArea.repaint();
    }

    /**
     * Paints Node boundaries in a highlighted color.
     * @param g the {@link Graphics} context
     */
    private void highlightSelectedNodes(Graphics g) {
        if(selectedNodes.isEmpty()) return;

        ArrayList<NodeConnection> in = new ArrayList<>();
        ArrayList<NodeConnection> out = new ArrayList<>();

        g.setColor(Color.GREEN);
        for( Node n : selectedNodes) {
            paintArea.paintNodeBorder(g, n);

            for( NodeConnection c : model.getConnections() ) {
                if(c.getOutNode()==n) in.add(c);
                if(c.getInNode()==n) out.add(c);
            }
        }
        ArrayList<NodeConnection> both = new ArrayList<>(in);
        both.retainAll(out);
        in.removeAll(both);
        out.removeAll(both);

        g.setColor(Color.decode("#ffff00"));
        for( NodeConnection c : in ) {
            paintArea.paintConnection(g,c);
        }
        g.setColor(Color.decode("#ff00ff"));
        for( NodeConnection c : both ) {
            paintArea.paintConnection(g,c);
        }
        g.setColor(Color.decode("#00ffff"));
        for( NodeConnection c : out ) {
            paintArea.paintConnection(g,c);
        }
    }

    public void setupMenuBar() {
        JFrame topFrame = (JFrame)SwingUtilities.getWindowAncestor(this);
        if(topFrame==null) return;

        JMenuBar menuBar = new JMenuBar();
        topFrame.setJMenuBar(menuBar);

        menuBar.add(setupGraphMenu());
        menuBar.add(setupNodeMenu());
        menuBar.add(setupToolMenu());
    }

    private void setupTools() {
        RectangleSelectTool rectangleSelectTool = new RectangleSelectTool(this);
        NodeMoveTool moveTool = new NodeMoveTool(this);
        ConnectionEditTool connectionEditTool = new ConnectionEditTool(this);
        tools.add(rectangleSelectTool);
        tools.add(moveTool);
        tools.add(connectionEditTool);

        swapTool(tools.get(0));
    }

    private JMenu setupToolMenu() {
        JMenu menu = new JMenu("Tools");

        for(ModalTool tool : tools) {
            AbstractAction swapAction = new ActionSwapTools(this, tool);
            swapAction.putValue(Action.ACCELERATOR_KEY,tool.getAcceleratorKey());
            JButton button = new JButton(swapAction);
            button.setMnemonic(tool.getAcceleratorKey().getKeyCode());
            toolBar.add(button);
            menu.add(swapAction);
        }

        menu.addSeparator();

        JMenuItem showToolBar = new JCheckBoxMenuItem("Show tool bar");
        menu.add(showToolBar);
        showToolBar.addActionListener(e -> toolBar.setVisible(showToolBar.isSelected()));
        showToolBar.setSelected(true);

        return menu;
    }

    /**
     * Populates the toolBar with actions and assigns accelerator keys.
     */
    private JMenu setupGraphMenu() {
        JMenu menu = new JMenu("Graph");
        ActionNewGraph actionNewGraph = new ActionNewGraph("New",this);
        ActionSaveGraph actionSaveGraph = new ActionSaveGraph("Save",this);
        ActionLoadGraph actionLoadGraph = new ActionLoadGraph("Load",this);
        ActionUpdateGraph actionUpdateGraph = new ActionUpdateGraph("Update",this);

        ActionPrintGraph actionPrintGraph = new ActionPrintGraph("Print",this);
        ActionStraightenGraph actionStraightenGraph = new ActionStraightenGraph("Straighten",this);

        actions.add(actionNewGraph);
        actions.add(actionSaveGraph);
        actions.add(actionLoadGraph);
        actions.add(actionUpdateGraph);
        actions.add(actionPrintGraph);
        actions.add(actionStraightenGraph);

        actionNewGraph.putValue(Action.ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));
        actionSaveGraph.putValue(Action.ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
        actionLoadGraph.putValue(Action.ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_L, KeyEvent.CTRL_DOWN_MASK));
        actionPrintGraph.putValue(Action.ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.CTRL_DOWN_MASK));
        actionUpdateGraph.putValue(Action.ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_U, KeyEvent.CTRL_DOWN_MASK));

        menu.add(actionNewGraph);
        menu.add(actionLoadGraph);
        menu.add(actionSaveGraph);
        menu.add(actionUpdateGraph);
        menu.addSeparator();
        menu.add(actionPrintGraph);
        menu.add(actionStraightenGraph);
        menu.addSeparator();

        return menu;
    }

    /**
     * Populates the popupBar and the node menu with actions and assigns accelerator keys.
     */
    private JMenu setupNodeMenu() {
        JMenu menu = new JMenu("Node");

        actionUndo.setActionRedo(actionRedo);
        actionRedo.setActionUndo(actionUndo);

        ActionCopyGraph actionCopyGraph = new ActionCopyGraph("Copy",this);
        ActionPasteGraph actionPasteGraph = new ActionPasteGraph("Paste",this);
        ActionDeleteGraph actionDeleteGraph = new ActionDeleteGraph("Delete",this);
        ActionCutGraph actionCutGraph = new ActionCutGraph("Cut", actionDeleteGraph, actionCopyGraph);
        AddNodeAction actionAddNode = new AddNodeAction("Add",this);
        ActionEditNodes actionEditNodes = new ActionEditNodes("Edit",this);
        ActionForciblyUpdateNodes actionForciblyUpdateNodes = new ActionForciblyUpdateNodes("Force update",this);
        ActionFoldGraph actionFoldGraph = new ActionFoldGraph("Fold",this, actionCutGraph);
        ActionUnfoldGraph actionUnfoldGraph = new ActionUnfoldGraph("Unfold",this);
        ActionIsolateGraph actionIsolateGraph = new ActionIsolateGraph("Isolate",this);
        ActionSelectAll actionSelectAll = new ActionSelectAll("Select all",this);
        ActionInvertSelection actionInvertSelection = new ActionInvertSelection("Invert selection",this);

        actions.add(actionUndo);
        actions.add(actionRedo);
        actions.add(actionCopyGraph);
        actions.add(actionPasteGraph);
        actions.add(actionDeleteGraph);
        actions.add(actionCutGraph);
        actions.add(actionAddNode);
        actions.add(actionEditNodes);
        actions.add(actionForciblyUpdateNodes);
        actions.add(actionFoldGraph);
        actions.add(actionUnfoldGraph);
        actions.add(actionIsolateGraph);
        actions.add(actionSelectAll);
        actions.add(actionInvertSelection);

        actionUndo.putValue(Action.ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK));
        actionRedo.putValue(Action.ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK|KeyEvent.SHIFT_DOWN_MASK));

        actionCopyGraph.putValue(Action.ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK));
        actionPasteGraph.putValue(Action.ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_DOWN_MASK));
        actionDeleteGraph.putValue(Action.ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
        actionCutGraph.putValue(Action.ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_DOWN_MASK));
        actionAddNode.putValue(Action.ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_PLUS, KeyEvent.CTRL_DOWN_MASK));
        actionEditNodes.putValue(Action.ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_E, KeyEvent.CTRL_DOWN_MASK));
        actionFoldGraph.putValue(Action.ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_BRACELEFT, KeyEvent.CTRL_DOWN_MASK));
        actionUnfoldGraph.putValue(Action.ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_BRACERIGHT, KeyEvent.CTRL_DOWN_MASK));
        actionIsolateGraph.putValue(Action.ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_I, KeyEvent.CTRL_DOWN_MASK));
        actionSelectAll.putValue(Action.ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_DOWN_MASK));
        actionInvertSelection.putValue(Action.ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_I, KeyEvent.CTRL_DOWN_MASK|KeyEvent.SHIFT_DOWN_MASK));

        menu.add(actionUndo);
        menu.add(actionRedo);
        menu.addSeparator();
        menu.add(actionSelectAll);
        menu.add(actionInvertSelection);
        menu.add(actionCopyGraph);
        menu.add(actionCutGraph);
        menu.add(actionPasteGraph);
        menu.add(actionDeleteGraph);
        menu.addSeparator();
        menu.add(actionAddNode);
        menu.add(actionEditNodes);
        menu.add(actionForciblyUpdateNodes);
        menu.addSeparator();
        menu.add(actionFoldGraph);
        menu.add(actionUnfoldGraph);
        menu.add(actionIsolateGraph);

        popupBar.add(actionAddNode);
        popupBar.add(actionEditNodes);
        popupBar.add(actionForciblyUpdateNodes);
        popupBar.addSeparator();
        popupBar.add(actionFoldGraph);
        popupBar.add(actionUnfoldGraph);
        popupBar.add(actionIsolateGraph);
        popupBar.addSeparator();
        popupBar.add(actionCopyGraph);
        popupBar.add(actionCutGraph);
        popupBar.add(actionPasteGraph);
        popupBar.addSeparator();
        popupBar.add(actionDeleteGraph);

        return menu;
    }

    public void swapTool(ModalTool tool) {
        deactivateCurrentTool();
        activeTool = tool;
        activateCurrentTool();
    }

    private void deactivateCurrentTool() {
        if(activeTool != null) {
            activeTool.detachKeyboardAdapter();
            activeTool.detachMouseAdapter();
        }
    }

    private void activateCurrentTool() {
        if(activeTool != null) {
            activeTool.attachKeyboardAdapter();
            activeTool.attachMouseAdapter();
        }
    }

    /**
     * Attaches the mouse adapter
     */
    private void attachMouseAdapter() {
        paintArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                maybeShowPopup(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                maybeShowPopup(e);
            }

            private void maybeShowPopup(MouseEvent e) {
                if(e.isPopupTrigger()) {
                    popupPoint.setLocation(e.getPoint());
                    popupBar.show(e.getComponent(),e.getX(),e.getY());
                }
            }
        });
    }

    /**
     * Move all selected nodes some relative cartesian amount.
     * @param dx the x-axis amount.
     * @param dy the y-axis amount.
     */
    public void moveSelectedNodes(int dx, int dy) {
        for(Node n : selectedNodes) {
            n.moveRelative(dx,dy);
        }
    }

    /**
     * Sets the list of selected nodes to one item.
     * @param n the new selected node.
     */
    public void setSelectedNode(Node n) {
        ArrayList<Node> nodes = new ArrayList<>();
        if(n!=null) nodes.add(n);
        setSelectedNodes(nodes);
    }

    /**
     * Sets the list of selected nodes.
     * @param list the new list of selected nodes.
     */
    public void setSelectedNodes(List<Node> list) {
        selectedNodes.clear();
        if (list != null) selectedNodes.addAll(list);
        updateActionEnableStatus();
        paintArea.repaint();
    }

    /**
     * Returns all selected nodes.  To change the selected nodes do not edit this list.  Instead,
     * call {@link NodeGraphEditorPanel#setSelectedNodes(List)} or {@link #setSelectedNode(Node)}.
     * @return all selected nodes.
     */
    public List<Node> getSelectedNodes() {
        return selectedNodes;
    }

    /**
     * All Actions have the tools to check for themselves if they are active.
     */
    private void updateActionEnableStatus() {
        for(AbstractAction a : actions) {
            if(a instanceof EditAction) {
                ((EditAction)a).updateEnableStatus();
            }
        }
    }

    /**
     * Returns the graph being edited.
     * @return the graph being edited.
     */
    public NodeGraph getGraph() {
        return model;
    }

    /**
     * Returns the cursor location when the popup began.
     * @return the cursor location when the popup began.
     */
    public Point getPopupPoint() {
        return popupPoint;
    }

    /**
     * Store a copy of some part of the graph for later.
     * @param graph the graph to set as copied.
     */
    public void setCopiedGraph(NodeGraph graph) {
        copiedGraph.clear();
        copiedGraph.add(graph);
    }

    /**
     * Returns the stored graph marked as copied.
     * @return the stored graph marked as copied.
     */
    public NodeGraph getCopiedGraph() {
        return copiedGraph;
    }

    /**
     * Clears the internal graph and resets everything.
     */
    public void clear() {
        model.clear();
        Node.setUniqueIDSource(0);
        activeTool.restart();
        setSelectedNode(null);
        repaint();
    }

    public NodeGraphViewPanel getPaintArea() {
        return paintArea;
    }

    private static void setSystemLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {}
    }

    public void addEdit(UndoableEdit undoableEdit) {
        undoHandler.undoableEditHappened(new UndoableEditEvent(this,undoableEdit));
    }

    /**
     * Main entry point.  Good for independent test.
     * @param args command line arguments.
     */
    public static void main(String[] args) {
        BuiltInRegistry.register();
        SwingRegistry.register();
        setSystemLookAndFeel();

        NodeGraph model = new NodeGraph();
        NodeGraphEditorPanel panel = new NodeGraphEditorPanel(model);

        JFrame frame = new JFrame("Node Graph Editor");
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(new Dimension(1200,800));
        frame.setLocationRelativeTo(null);
        frame.add(panel);
        panel.setupMenuBar();
        frame.setVisible(true);
    }
}
