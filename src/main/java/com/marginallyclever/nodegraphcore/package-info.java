/**
 * NodeGraphCore contains essential elements for describing a graph that contains nodes for flow-based programming.
 * The {@link com.marginallyclever.nodegraphcore.Graph} contains {@link com.marginallyclever.nodegraphcore.Node}s.
 * Nodes have {@link com.marginallyclever.nodegraphcore.dock.Dock}s.  NodeVariables are connected with {@link
 * com.marginallyclever.nodegraphcore.Connection}s.  A graph may be contained within a {@link
 * com.marginallyclever.nodegraphcore.Subgraph}.
 *
 *
 * Nodes are exposed to editing and serialization tools by registering themselves to the NodeFactory.  See {@link
 * com.marginallyclever.nodegraphcore.BuiltInRegistry} for examples which include {@link
 * com.marginallyclever.nodegraphcore.nodes.PrintToStdOut}, boolean logical operators, and basic math functions.
 *
 *
 * The core nodes are registered by calling {@link com.marginallyclever.nodegraphcore.BuiltInRegistry#registerNodes()}.
 * The core DAO are registered by calling {@link com.marginallyclever.nodegraphcore.BuiltInRegistry#registerDAO()}.
 */
package com.marginallyclever.nodegraphcore;