module com.marginallyclever.nodegraphcore {
    requires java.desktop;
    requires org.json;
    requires org.slf4j;
    requires org.reflections;
    requires io.github.classgraph;
    requires java.prefs;
    requires java.compiler;
    requires jsr305;

    uses com.marginallyclever.nodegraphcore.NodeRegistry;
    provides com.marginallyclever.nodegraphcore.NodeRegistry with
            com.marginallyclever.nodegraphcore.BuiltInRegistry;

    uses com.marginallyclever.nodegraphcore.DAORegistry;
    provides com.marginallyclever.nodegraphcore.DAORegistry with
            com.marginallyclever.nodegraphcore.BuiltInRegistry;

    exports com.marginallyclever.nodegraphcore;
    exports com.marginallyclever.nodegraphcore.json;
    exports com.marginallyclever.nodegraphcore.port;
    exports com.marginallyclever.nodegraphcore.nodes;
}