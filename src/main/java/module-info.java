/**
 * NodeGraphCore contains essential elements for describing a {@link com.marginallyclever.nodegraphcore.NodeGraph} for
 * flow-based programming.<br>
 * <br>
 * NodeGraphSwing contains Swing-based {@link com.marginallyclever.nodegraphcore.Node}s and all Swing-based tools for
 * editing {@link com.marginallyclever.nodegraphcore.NodeGraph}s.
 */
module com.marginallyclever.nodegraphcore {
    requires java.desktop;
    requires org.json;
    requires org.slf4j;
    requires ch.qos.logback.core;

    uses com.marginallyclever.nodegraphcore.NodeRegistry;
    provides com.marginallyclever.nodegraphcore.NodeRegistry with
            com.marginallyclever.nodegraphcore.BuiltInRegistry,
            com.marginallyclever.donatello.SwingRegistry;

    uses com.marginallyclever.nodegraphcore.DAORegistry;
    provides com.marginallyclever.nodegraphcore.DAORegistry with
            com.marginallyclever.nodegraphcore.BuiltInRegistry,
            com.marginallyclever.donatello.SwingRegistry;

    exports com.marginallyclever.nodegraphcore;
    exports com.marginallyclever.nodegraphcore.json;

    exports com.marginallyclever.donatello;
    exports com.marginallyclever.donatello.actions;
}