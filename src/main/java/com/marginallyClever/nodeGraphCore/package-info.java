/**
 * NodeGraphCore contains essential elements for describing a graph that contains nodes for flow-based programming.
 * The {@link com.marginallyClever.nodeGraphCore.NodeGraph} contains {@link com.marginallyClever.nodeGraphCore.Node}s.
 * Nodes have {@link com.marginallyClever.nodeGraphCore.NodeVariable}s.  NodeVariables are connected with {@link
 * com.marginallyClever.nodeGraphCore.NodeConnection}s.  A graph may be contained within a {@link
 * com.marginallyClever.nodeGraphCore.Subgraph}.
 *
 * Nodes are exposed to editing and serialization tools by registering themselves to the NodeFactory.  See {@link
 * com.marginallyClever.nodeGraphCore.BuiltInRegistry} for examples which include {@link
 * com.marginallyClever.nodeGraphCore.builtInNodes.PrintToStdOut}, boolean logical operators, and basic math functions.
 */
package com.marginallyClever.nodeGraphCore;