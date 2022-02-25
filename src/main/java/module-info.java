/**
 * NodeGraphCore contains essential elements for describing a {@link com.marginallyClever.nodeGraphCore.NodeGraph} for
 * flow-based programming.<br>
 * <br>
 * NodeGraphSwing contains Swing-based {@link com.marginallyClever.nodeGraphCore.Node}s and all Swing-based tools for
 * editing {@link com.marginallyClever.nodeGraphCore.NodeGraph}s.
 */
module com.marginallyClever.NodeGraphCore {
    requires java.desktop;
    requires com.google.gson;

    exports com.marginallyClever.nodeGraphCore;
    exports com.marginallyClever.nodeGraphSwing;
}