package com.marginallyclever.nodegraphcore;

import com.marginallyclever.nodegraphcore.dock.Dock;
import com.marginallyclever.nodegraphcore.dock.Input;
import com.marginallyclever.nodegraphcore.dock.Output;

import java.awt.*;
import java.util.ArrayList;

/**
 * A {@link Subgraph} is a {@link Node} which contains another graph.
 */
public class Subgraph extends Node implements SupergraphInput, SupergraphOutput, PrintWithGraphics {
    private final Graph graph = new Graph();

    static class VariablePair {
        public Dock<?> superVariable;
        public Dock<?> subVariable;

        public VariablePair(Dock<?> subVariable) {
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
    public Subgraph(Graph graph) {
        this();
        setGraph(graph);
    }

    /**
     * Stores a deep copy of the given graph and exposes the {@link SupergraphInput}s and {@link SupergraphOutput}s to
     * the supergraph.
     * @param graph the {@link Graph} to store.  A deep copy is made.
     */
    public void setGraph(Graph graph) {
        this.graph.clear();

        if(graph!=null) {
            this.graph.add(graph.deepCopy());
        }

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
                Dock<?> v = n.getVariable(i);
                if(v instanceof Input) {
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
                Dock<?> v = n.getVariable(i);
                if(v instanceof Output) {
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
        int aIn = (a.subVariable  instanceof Input)?1:0;
        int bIn = (b.subVariable  instanceof Input)?1:0;
        if(aIn != bIn) return aIn-bIn;
        // then sort by name alphabetically
        return a.subVariable.getName().compareTo(b.subVariable.getName());
    }

    /**
     * Create and store a supergraph/subgraph variable pair.
     * @param v subgraph variable.
     */
    private void addToPairs(Dock<?> v) {
        VariablePair p = new VariablePair(v);
        pairs.add(p);
    }

    /**
     * Returns the {@link Graph} within this {@link Subgraph}
     * @return the {@link Graph} within this {@link Subgraph}
     */
    public Graph getGraph() {
        return graph;
    }

    @Override
    public void update() {
        for(VariablePair p : pairs) {
            if(p.superVariable instanceof Input) {
                p.subVariable.setValue(p.superVariable.getValue());
            }
            if(p.superVariable instanceof Output) {
                p.superVariable.setValue(p.subVariable.getValue());
            }
        }

        graph.update();
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
