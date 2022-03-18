/**
 * NodeGraphCore contains essential elements for describing a graph that contains nodes for flow-based programming.
 * The {@link com.marginallyclever.nodegraphcore.NodeGraph} contains {@link com.marginallyclever.nodegraphcore.Node}s.
 * Nodes have {@link com.marginallyclever.nodegraphcore.NodeVariable}s.  NodeVariables are connected with {@link
 * com.marginallyclever.nodegraphcore.NodeConnection}s.  A graph may be contained within a {@link
 * com.marginallyclever.nodegraphcore.Subgraph}.
 *
 * Nodes are exposed to editing and serialization tools by registering themselves to the NodeFactory.  See {@link
 * com.marginallyclever.nodegraphcore.BuiltInRegistry} for examples which include {@link
 * com.marginallyclever.nodegraphcore.corenodes.PrintToStdOut}, boolean logical operators, and basic math functions.
 */
package com.marginallyclever.nodegraphcore;