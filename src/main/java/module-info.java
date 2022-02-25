/**
 * NodeGraphCore contains all the core class descriptions.
 * NodeGraphSwing contains all the Swing implementations.
 */
module com.marginallyClever.NodeGraphCore {
    requires java.desktop;
    requires com.google.gson;

    exports com.marginallyClever.nodeGraphCore;
    exports com.marginallyClever.nodeGraphSwing;
}