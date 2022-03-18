package com.marginallyclever.donatello;

import com.marginallyclever.donatello.modaltools.RememberKeyStateAction;
import com.marginallyclever.nodegraphcore.*;
import com.marginallyclever.donatello.actions.*;
import com.marginallyclever.donatello.actions.undoable.*;
import com.marginallyclever.donatello.modaltools.ConnectionEditTool;
import com.marginallyclever.donatello.modaltools.NodeMoveTool;
import com.marginallyclever.donatello.modaltools.RectangleSelectTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
 * {@link Donatello} is a Graphic User Interface to edit a {@link NodeGraph}.
 * @author Dan Royer
 * @since 2022-02-01
 */
public class Donatello extends JPanel {
    private static final Logger logger = LoggerFactory.getLogger(Donatello.class);
    /**
     * Used by save and load actions
      */
    public static final FileNameExtensionFilter FILE_FILTER = new FileNameExtensionFilter("Node Graph","graph");
    private static final Color COLOR_SELECTED_NODE = Color.GREEN;
    private static final Color COLOR_CONNECTION_EXTERNAL_INBOUND = Color.decode("#FFFF00");
    private static final Color COLOR_CONNECTION_INTERNAL = Color.decode("#FF00FF");
    private static final Color COLOR_CONNECTION_EXTERNAL_OUTBOUND = Color.decode("#00FFFF");

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

    /**
     * Manages undo/redo in the editor.
     */
    private final UndoManager undoManager = new UndoManager();

    /**
     * declared here so that it can be referenced by the RedoAction.
     */
    private final UndoAction undoAction = new UndoAction(undoManager);

    /**
     * declared here so that it can be referenced by the UndoAction.
     */
    private final RedoAction redoAction = new RedoAction(undoManager);

    private final UndoHandler undoHandler = new UndoHandler(undoManager, undoAction, redoAction);

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
    public Donatello(NodeGraph model) {
        super(new BorderLayout());
        this.model = model;

        paintArea = new NodeGraphViewPanel(model);

        this.add(toolBar,BorderLayout.NORTH);
        this.add(paintArea,BorderLayout.CENTER);

        setupTools();
        setupPaintArea();

        attachMouse();

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

        g.setColor(COLOR_SELECTED_NODE);
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

        g.setColor(COLOR_CONNECTION_EXTERNAL_INBOUND);
        for( NodeConnection c : in ) {
            paintArea.paintConnection(g,c);
        }
        g.setColor(COLOR_CONNECTION_INTERNAL);
        for( NodeConnection c : both ) {
            paintArea.paintConnection(g,c);
        }
        g.setColor(COLOR_CONNECTION_EXTERNAL_OUTBOUND);
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
        menuBar.add(setupHelpMenu());
    }

    private void setupTools() {
        RectangleSelectTool rectangleSelectTool = new RectangleSelectTool(this);
        NodeMoveTool moveTool = new NodeMoveTool(this);
        ConnectionEditTool connectionEditTool = new ConnectionEditTool(this,"Add connection","Remove connection");
        tools.add(rectangleSelectTool);
        tools.add(moveTool);
        tools.add(connectionEditTool);

        swapTool(tools.get(0));
    }

    private JMenu setupHelpMenu() {
        JMenu menu = new JMenu("Help");

        BrowseURLAction showLog = new BrowseURLAction("Open log file",FileHelper.convertToFileURL(FileHelper.getLogFile()));
        BrowseURLAction update = new BrowseURLAction("Check for updates","https://github.com/MarginallyClever/NodeGraphCore/releases");
        BrowseURLAction problem = new BrowseURLAction("I have a problem...","https://github.com/MarginallyClever/NodeGraphCore/issues");
        BrowseURLAction drink = new BrowseURLAction("Buy me a drink","https://www.paypal.com/donate/?hosted_button_id=Y3VZ66ZFNUWJE");
        BrowseURLAction community = new BrowseURLAction("Join the community","https://discord.gg/TbNHKz6rpy");
        BrowseURLAction idea = new BrowseURLAction("I have an idea!","https://github.com/MarginallyClever/NodeGraphCore/issues");

        community.putValue(Action.SMALL_ICON, new UnicodeIcon("🤝"));
        drink.putValue(Action.SMALL_ICON, new UnicodeIcon("🍹"));
        update.putValue(Action.SMALL_ICON, new UnicodeIcon("📰"));
        problem.putValue(Action.SMALL_ICON, new UnicodeIcon("☏"));
        idea.putValue(Action.SMALL_ICON, new UnicodeIcon("💭"));
        problem.putValue(Action.SMALL_ICON, new UnicodeIcon("⚡"));

        menu.add(community);
        menu.add(drink);
        menu.add(update);
        menu.addSeparator();
        menu.add(idea);
        menu.add(problem);
        menu.addSeparator();
        menu.add(showLog);

        return menu;
    }

    private JMenu setupToolMenu() {
        JMenu menu = new JMenu("Tools");

        ButtonGroup group = new ButtonGroup();

        // TODO select action: ⛶
        // TODO move action: ✥ or ⬌\r⬍
        // TODO connect action: 🔌

        for(ModalTool tool : tools) {
            AbstractAction swapAction = new SwapToolsAction(this, tool);
            swapAction.putValue(Action.ACCELERATOR_KEY,tool.getAcceleratorKey());
            JToggleButton button = new JToggleButton(swapAction);
            group.add(button);
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
        NewGraphAction newGraphAction = new NewGraphAction("New",this);
        LoadGraphAction loadGraphAction = new LoadGraphAction("Load",this);
        SaveGraphAction saveGraphAction = new SaveGraphAction("Save",this);
        PrintGraphAction printGraphAction = new PrintGraphAction("Print",this);
        UpdateGraphAction updateGraphAction = new UpdateGraphAction("Update",this);
        StraightenGraphAction straightenGraphAction = new StraightenGraphAction("Straighten",this);
        OrganizeGraphAction organizeGraphAction = new OrganizeGraphAction("Organize",this);

        newGraphAction.putValue(Action.SMALL_ICON,new UnicodeIcon("🌱"));
        loadGraphAction.putValue(Action.SMALL_ICON,new UnicodeIcon("🗁"));
        saveGraphAction.putValue(Action.SMALL_ICON,new UnicodeIcon("🖫"));
        printGraphAction.putValue(Action.SMALL_ICON,new UnicodeIcon("🖶"));
        updateGraphAction.putValue(Action.SMALL_ICON,new UnicodeIcon("▶"));
        straightenGraphAction.putValue(Action.SMALL_ICON,new UnicodeIcon("🧹"));
        straightenGraphAction.putValue(Action.SMALL_ICON,new UnicodeIcon("📐"));
        organizeGraphAction.putValue(Action.SMALL_ICON,new UnicodeIcon("📝"));

        //TODO toggleKeepUpdatingAction.putValue(Action.SMALL_ICON,new UnicodeIcon("🔃"));

        actions.add(newGraphAction);
        actions.add(saveGraphAction);
        actions.add(loadGraphAction);
        actions.add(updateGraphAction);
        actions.add(printGraphAction);
        actions.add(straightenGraphAction);
        actions.add(organizeGraphAction);

        newGraphAction.putValue(Action.ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));
        saveGraphAction.putValue(Action.ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
        loadGraphAction.putValue(Action.ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_L, KeyEvent.CTRL_DOWN_MASK));
        printGraphAction.putValue(Action.ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.CTRL_DOWN_MASK));
        updateGraphAction.putValue(Action.ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_U, 0));
        organizeGraphAction.putValue(Action.ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));

        menu.add(newGraphAction);
        menu.add(loadGraphAction);
        menu.add(saveGraphAction);
        menu.addSeparator();
        menu.add(updateGraphAction);
        menu.add(printGraphAction);
        menu.add(straightenGraphAction);
        menu.add(organizeGraphAction);

        return menu;
    }

    /**
     * Populates the popupBar and the node menu with actions and assigns accelerator keys.
     */
    private JMenu setupNodeMenu() {
        JMenu menu = new JMenu("Node");

        undoAction.setActionRedo(redoAction);
        redoAction.setActionUndo(undoAction);

        CopyGraphAction copyGraphAction = new CopyGraphAction("Copy",this);
        PasteGraphAction pasteGraphAction = new PasteGraphAction("Paste",this);
        DeleteGraphAction deleteGraphAction = new DeleteGraphAction("Delete",this);
        CutGraphAction cutGraphAction = new CutGraphAction("Cut", deleteGraphAction, copyGraphAction);
        AddNodeAction addNodeAction = new AddNodeAction("Add",this);
        EditNodeAction editNodesAction = new EditNodeAction("Edit",this);
        ForciblyUpdateNodesAction forciblyUpdateNodesAction = new ForciblyUpdateNodesAction("Force update",this);
        FoldGraphAction foldGraphAction = new FoldGraphAction("Fold",this, cutGraphAction);
        UnfoldGraphAction unfoldGraphAction = new UnfoldGraphAction("Unfold",this);
        IsolateGraphAction isolateGraphAction = new IsolateGraphAction("Isolate",this);
        SelectAllAction selectAllAction = new SelectAllAction("Select all",this);
        InvertSelectionAction invertSelectionAction = new InvertSelectionAction("Invert selection",this);

        undoAction.putValue(Action.SMALL_ICON, new UnicodeIcon("⇤"));
        redoAction.putValue(Action.SMALL_ICON, new UnicodeIcon("⇥"));

        copyGraphAction.putValue(Action.SMALL_ICON, new UnicodeIcon("🗐"));
        pasteGraphAction.putValue(Action.SMALL_ICON, new UnicodeIcon("📎"));
        deleteGraphAction.putValue(Action.SMALL_ICON, new UnicodeIcon("🗑"));
        cutGraphAction.putValue(Action.SMALL_ICON, new UnicodeIcon("✂"));
        addNodeAction.putValue(Action.SMALL_ICON, new UnicodeIcon("➕"));
        editNodesAction.putValue(Action.SMALL_ICON, new UnicodeIcon("✏"));
        forciblyUpdateNodesAction.putValue(Action.SMALL_ICON, new UnicodeIcon("⏩"));
        foldGraphAction.putValue(Action.SMALL_ICON, new UnicodeIcon("⫏"));
        unfoldGraphAction.putValue(Action.SMALL_ICON, new UnicodeIcon("⟃"));
        isolateGraphAction.putValue(Action.SMALL_ICON, new UnicodeIcon("𝄄"));

        actions.add(undoAction);
        actions.add(redoAction);
        actions.add(copyGraphAction);
        actions.add(pasteGraphAction);
        actions.add(deleteGraphAction);
        actions.add(cutGraphAction);
        actions.add(addNodeAction);
        actions.add(editNodesAction);
        actions.add(forciblyUpdateNodesAction);
        actions.add(foldGraphAction);
        actions.add(unfoldGraphAction);
        actions.add(isolateGraphAction);
        actions.add(selectAllAction);
        actions.add(invertSelectionAction);

        undoAction.putValue(Action.ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK));
        redoAction.putValue(Action.ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK|KeyEvent.SHIFT_DOWN_MASK));

        copyGraphAction.putValue(Action.ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK));
        pasteGraphAction.putValue(Action.ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_DOWN_MASK));
        deleteGraphAction.putValue(Action.ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
        cutGraphAction.putValue(Action.ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_DOWN_MASK));
        addNodeAction.putValue(Action.ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_PLUS, KeyEvent.CTRL_DOWN_MASK));
        editNodesAction.putValue(Action.ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_E, KeyEvent.CTRL_DOWN_MASK));
        foldGraphAction.putValue(Action.ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_BRACELEFT, KeyEvent.CTRL_DOWN_MASK));
        unfoldGraphAction.putValue(Action.ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_BRACERIGHT, KeyEvent.CTRL_DOWN_MASK));
        isolateGraphAction.putValue(Action.ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_I, KeyEvent.CTRL_DOWN_MASK));
        selectAllAction.putValue(Action.ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_DOWN_MASK));
        invertSelectionAction.putValue(Action.ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_I, KeyEvent.CTRL_DOWN_MASK|KeyEvent.SHIFT_DOWN_MASK));

        menu.add(undoAction);
        menu.add(redoAction);
        menu.addSeparator();
        menu.add(selectAllAction);
        menu.add(invertSelectionAction);
        menu.add(copyGraphAction);
        menu.add(cutGraphAction);
        menu.add(pasteGraphAction);
        menu.add(deleteGraphAction);
        menu.addSeparator();
        menu.add(addNodeAction);
        menu.add(editNodesAction);
        menu.add(forciblyUpdateNodesAction);
        menu.addSeparator();
        menu.add(foldGraphAction);
        menu.add(unfoldGraphAction);
        menu.add(isolateGraphAction);

        popupBar.add(addNodeAction);
        popupBar.add(editNodesAction);
        popupBar.add(forciblyUpdateNodesAction);
        popupBar.addSeparator();
        popupBar.add(foldGraphAction);
        popupBar.add(unfoldGraphAction);
        popupBar.add(isolateGraphAction);
        popupBar.addSeparator();
        popupBar.add(copyGraphAction);
        popupBar.add(cutGraphAction);
        popupBar.add(pasteGraphAction);
        popupBar.addSeparator();
        popupBar.add(deleteGraphAction);

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
     * Respond to popup menu requests
     */
    private void attachMouse() {
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
     * call {@link Donatello#setSelectedNodes(List)} or {@link #setSelectedNode(Node)}.
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
            if(a instanceof EditorAction) {
                ((EditorAction)a).updateEnableStatus();
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

    public static void setSystemLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            logger.debug("System look and feel could not be set.");
        }
    }

    public void addEdit(UndoableEdit undoableEdit) {
        undoHandler.undoableEditHappened(new UndoableEditEvent(this,undoableEdit));
    }

    /**
     * Main entry point.  Good for independent test.
     * @param args command line arguments.
     */
    public static void main(String[] args) {
        NodeFactory.loadRegistries();
        DAO4JSONFactory.loadRegistries();

        Donatello.setSystemLookAndFeel();

        PropertiesHelper.showProperties();
        PropertiesHelper.listAllNodes();
        PropertiesHelper.listAllDAO();

        Donatello panel = new Donatello(new NodeGraph());

        JFrame frame = new JFrame("Donatello Node Graph Editor");
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(new Dimension(1200,800));
        frame.setLocationRelativeTo(null);
        frame.add(panel);
        panel.setupMenuBar();
        frame.setVisible(true);
    }
}
