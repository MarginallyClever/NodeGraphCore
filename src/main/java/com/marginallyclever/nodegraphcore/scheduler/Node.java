package com.marginallyclever.nodegraphcore.scheduler;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * A node in a graph that processes data and passes it to downstream nodes.
 */
public abstract class Node {
    // Input queue for incoming data
    private final BlockingQueue<Object> inputQueue = new LinkedBlockingQueue<>();
    private final BlockingQueue<Node> downstreamNodes = new LinkedBlockingQueue<>();

    public void addInput(Object data) {
        try {
            inputQueue.put(data);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public BlockingQueue<Object> getInputQueue() {
        return inputQueue;
    }

    public void connect(Node nextNode) {
        downstreamNodes.add(nextNode);
    }

    public BlockingQueue<Node> getDownstreamNodes() {
        return downstreamNodes;
    }

    /**
     * Executes the node by processing the input data and passing it to downstream nodes.
     */
    public void execute() {
        while (!inputQueue.isEmpty()) {
            try {
                Object input = inputQueue.poll();
                if (input != null) {
                    Object output = process(input);
                    for (Node downstreamNode : downstreamNodes) {
                        downstreamNode.addInput(output);
                    }
                }
            } catch (Exception e) {
                System.err.println("Error in node execution: " + e.getMessage());
            }
        }
    }

    protected abstract Object process(Object input);
}

