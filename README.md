# Flow-based Programming

[![Release](https://jitpack.io/v/MarginallyClever/NodeGraphCore.svg)](https://jitpack.io/#MarginallyClever/NodeGraphCore)
![workflow](https://github.com/MarginallyClever/NodeGraphCore/actions/workflows/main.yml/badge.svg)

A pure Java implementation of [Flow-based Programming](https://en.wikipedia.org/wiki/Dataflow_programming) (FBP).

Flow-based programming is best known in no-code/low-code systems like Scratch, Node-RED, and Unreal
Engine's Blueprints.  It is a way to visually represent a program's data flow.  Each node is a
function that takes input and produces output.  The connections between nodes are the data that
flows between them.

Data-flow programming is aesthetically pleasing, greatly reduces the chance of syntax error, and empowering for people
that are not fluent in the archaic syntax of text-only languages.

### Features

- Nodes are not directed or forced to run via triggers.  There is little danger of large networks overflowing the stack.  They could be run in parallel.
- Folding: Collapse a subgraph down to a single Node with *Fold* and reverse with *Unfold*
- Convenient built-in nodes for basic math and reporting.
- Unit tests for everything!  If it can be tested, we shall!
- The editor has written in Java Swing.  The main executable class is `com.marginallyclever.donatello.Donatello`.
- While running the Swing editor you can also access Swing-only nodes like `LoadImage` and `PrintImage`.  PrintImage results will appear in the background of the node editor panel.
- A ~/Donatello/ folder contains the application log file.
- A ~/Donatello/extensions/ folder contains 3rd party plugins.  Add new Nodes or write your own.

### Getting started

1. Download the latest source code from https://github.com/MarginallyClever/NodeGraphCore/
2. Use your favorite IDE to import the Maven project.
3. Use Maven to "install" the project.  It will now be available as a local dependency in your other projects.

./src/test/java/com/marginallyclever/nodegraphcore has unit tests, which are also examples of how to use the API.

### Use it, Discuss it, Love it.

- Please see the [Javadoc with the full API for Core and Swing](https://marginallyclever.github.io/NodeGraphCore/javadoc).
- Please see guide for [how to Contribute](https://github.com/MarginallyClever/NodeGraphCore/blob/main/CONTRIBUTING.md)
- The [Official webpage](https://marginallyclever.github.io/NodeGraphCore/)!
- Join [the Discord channel](https://discord.gg/Q5TZFmB) and make new friends.

### Based on work by

- https://github.com/otto-link/GNode/
- https://nodes.io/story/
- https://github.com/janbijster/cobble
- https://github.com/kenk42292/shoyu
- https://github.com/paceholder/nodeeditor
- https://github.com/miho/VWorkflows
- https://nodered.org/
- Maya, Unity, Blender
- NoFlo, Flowhub
- and others

### See also

- [Flow based programming Discord](https://discord.com/invite/YBQj6UsD5H)
- https://jpaulm.github.io/fbp/