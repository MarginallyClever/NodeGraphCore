# Node Graph and Editor

A pure Java implementation of Node based [data flow programming](https://en.wikipedia.org/wiki/Dataflow_programming).

![img](preview-for-github.png)

### Features

- Parallel operation: Nodes are not directed or forced to run.  There is little danger of large networks overflowing the stack. 
- Foldable: Collapse a subgraph down to a single node with Fold and reverse with Unfold
- Convenient: Built in nodes for basic math and reporting.
- Tested: Unit tests for everything!  If it can be tested, we shall!
- A sample editor has been provided in Java Swing.  The main executable class is `com.marginallyClever.nodeGraphSwing.NodeGraphEditorPanel`.
- While running the Swing editor you can also access Swing-only nodes like `LoadImage` and `PrintImage`.  PrintImage will appear in the background of the node editor panel. 

### Discuss

Join [the Discord channel](https://discord.gg/Q5TZFmB) and make new friends.

### Based on work by

- https://nodes.io/story/
- https://github.com/janbijster/cobble
- https://github.com/kenk42292/shoyu
- https://github.com/paceholder/nodeeditor
- https://github.com/miho/VWorkflows
- Unity
- Blender
- and others
