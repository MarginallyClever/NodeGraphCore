package com.marginallyclever.nodegraphcore;

import java.awt.*;
import java.util.ArrayList;

/**
 * A {@link Subgraph} is a {@link Node} which contains another graph.
 */
public class Subgraph extends Node implements SupergraphInput, SupergraphOutput, PrintWithGraphics {
    private final NodeGraph graph = new NodeGraph();

    private class VariablePair {
        public NodeVariable<?> superVariable;
        public NodeVariable<?> subVariable;

        public VariablePair(NodeVariable<?> subVariable) {
            this.superVariable = subVariable.createInverse();
            this.subVariable = subVariable;
        }
    }

    /**
     * exposed inputs and outputs from the subgraph contents.
     */
    private final ArrayList<VariablePair> pairs = new ArrayList<>();

    /**
     * Constructor for subclasses to call.
     */
    public Subgraph() {
        super("SubGraph");
    }

    /**
     * Constructor for subclasses to call.
     * @param graph the contents of this subgraph.
     */
    public Subgraph(NodeGraph graph) {
        this();
        setGraph(graph);
    }

    /**
     * Stores a deep copy of the given graph and exposes the {@link SupergraphInput}s and {@link SupergraphOutput}s to
     * the supergraph.
     * @param graph the {@link NodeGraph} to store.
     */
    public void setGraph(NodeGraph graph) {
        this.graph.clear();
        this.graph.add(graph.deepCopy());

        for(Node n : this.graph.getNodes()) {
            extractSupergraphInputs(n);
            extractSupergraphOutputs(n);
        }

        // sort and add the pairs.
        pairs.sort(this::sortVariables);
        for(VariablePair p : pairs) {
            this.addVariable(p.superVariable);
        }

        this.updateBounds();
    }

    /**
     * Find all {@link SupergraphOutput} of a node and remember them for later.
     * @param n the node to scan
     */
    private void extractSupergraphOutputs(Node n) {
        if(n instanceof SupergraphOutput) {
            System.out.println("SupergraphOutput "+n.getUniqueName());
            for(int i=0;i<n.getNumVariables();++i) {
                NodeVariable<?> v = n.getVariable(i);
                if(v.getHasInput()) {
                    System.out.println("found output "+v.getName());
                    addToPairs(v);
                }
            }
        }
    }

    /**
     * Find all {@link SupergraphInput} of a node and remember them for later.
     * @param n the node to scan
     */
    private void extractSupergraphInputs(Node n) {
        if(n instanceof SupergraphInput) {
            System.out.println("SupergraphInput "+n.getUniqueName());
            for(int i=0;i<n.getNumVariables();++i) {
                NodeVariable<?> v = n.getVariable(i);
                if(v.getHasOutput()) {
                    System.out.println("found input "+v.getName());
                    addToPairs(v);
                }
            }
        }
    }

    /**
     * Sort variables so that inputs are first in a node, then by name alphabetically
     * @param a pair a
     * @param b pair b
     * @return the sorting test result
     */
    private int sortVariables(VariablePair a, VariablePair b) {
        // all input first
        int aIn = (a.subVariable.getHasInput())?1:0;
        int bIn = (a.subVariable.getHasInput())?1:0;
        if(aIn != bIn) return aIn-bIn;
        // then sort by name alphabetically
        return a.subVariable.getName().compareTo(b.subVariable.getName());
    }

    /**
     * Create and store a supergraph/subgraph variable pair.
     * @param v subgraph variable.
     */
    private void addToPairs(NodeVariable<?> v) {
        VariablePair p = new VariablePair(v);
        pairs.add(p);
    }

    /**
     * Returns the {@link NodeGraph} within this {@link Subgraph}
     * @return the {@link NodeGraph} within this {@link Subgraph}
     */
    public NodeGraph getGraph() {
        return graph;
    }

    @Override
    public Node create() {
        return new Subgraph();
    }

    @Override
    public void update() {
        for(VariablePair p : pairs) {
            if(p.superVariable.getHasInput()) {
                if (p.superVariable.getIsDirty()) {
                    p.subVariable.setValue(p.superVariable.getValue());
                }
            }
            if(p.superVariable.getHasOutput()) {
                if (p.subVariable.getIsDirty()) {
                    p.superVariable.setValue(p.subVariable.getValue());
                }
            }
        }

        graph.update();
        cleanAllInputs();
    }

    @Override
    public void print(Graphics g) {
        for(Node n : this.graph.getNodes()) {
            if(n instanceof PrintWithGraphics) {
                ((PrintWithGraphics)n).print(g);
            }
        }
    }
}
