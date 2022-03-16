/**
 * NodeGraphCore contains essential elements for describing a {@link com.marginallyClever.nodeGraphCore.NodeGraph} for
 * flow-based programming.<br>
 * <br>
 * NodeGraphSwing contains Swing-based {@link com.marginallyClever.nodeGraphCore.Node}s and all Swing-based tools for
 * editing {@link com.marginallyClever.nodeGraphCore.NodeGraph}s.
 */
module com.marginallyClever.nodeGraphCore {
    requires java.desktop;
    requires org.json;
    requires org.slf4j;
    requires logback.core;

    uses com.marginallyClever.nodeGraphCore.NodeRegistry;
    provides com.marginallyClever.nodeGraphCore.NodeRegistry with
            com.marginallyClever.nodeGraphCore.BuiltInRegistry,
            com.marginallyClever.nodeGraphSwing.SwingRegistry;

    uses com.marginallyClever.nodeGraphCore.DAORegistry;
    provides com.marginallyClever.nodeGraphCore.DAORegistry with
            com.marginallyClever.nodeGraphCore.BuiltInRegistry,
            com.marginallyClever.nodeGraphSwing.SwingRegistry;

    exports com.marginallyClever.nodeGraphCore;
    exports com.marginallyClever.nodeGraphCore.json;

    exports com.marginallyClever.nodeGraphSwing;
    exports com.marginallyClever.nodeGraphSwing.actions;
}