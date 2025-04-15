package com.marginallyclever.nodegraphcore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A scheduler that runs a graph of nodes using a thread pool.
 */
public class ThreadPoolScheduler {
    private static final Logger logger = LoggerFactory.getLogger(ThreadPoolScheduler.class);
    private final ExecutorService threadPool = Executors.newVirtualThreadPerTaskExecutor();
    private final BlockingQueue<Node> readyNodes = new LinkedBlockingQueue<>();
    private final AtomicInteger activeTasks = new AtomicInteger(0);
    private boolean queueDownstreamNodes = false;

    public ThreadPoolScheduler() {}

    /**
     * Submits a node to the scheduler for execution.
     * @param node The node to submit
     */
    public void submit(Node node) {
        if(readyNodes.contains(node)) {
            logger.debug("defer {} {}", node.getName(), node.getLabel());
            // move node to the tail of the queue
            readyNodes.remove(node);
            readyNodes.add(node);
        } else {
            logger.debug("add {} {}", node.getName(), node.getLabel());
            readyNodes.add(node); // Add the node to the ready queue
            activeTasks.incrementAndGet(); // Increment task count
        }
    }

    /**
     * @return true if there are no nodes in the ready queue and no active tasks
     */
    public boolean isIdle() {
        return readyNodes.isEmpty() && activeTasks.get() == 0;
    }

    /**
     * Starts the scheduler and processes nodes until all are completed.
     */
    public void run() {
        while (!isIdle()) { // Process nodes until all are completed
            update();
        }
    }

    /**
     * Updates the scheduler by executing the next node in the ready queue.
     */
    public void update() {
        Node node = readyNodes.poll(); // Get the next node
        if (node == null) return;

        threadPool.submit(() -> {
            logger.debug("start {} {}", node.getName(), node.getLabel());
            try {
                node.update();
                node.updateBounds();
                node.setInputsClean();
                if(queueDownstreamNodes) {
                    for (Node downstreamNode : node.getDownstreamNodes()) {
                        if (!downstreamNode.isDirty()) continue;
                        if (!hasQueued(downstreamNode)) {
                            submit(downstreamNode);  // Submit downstream nodes
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println("Error in node execution: " + e.getMessage());
                e.printStackTrace();
            } finally {
                logger.debug("end {} {}", node.getName(), node.getLabel());
                activeTasks.decrementAndGet(); // Mark this task as completed
            }
        });
    }

    public void shutdown(long timeoutSeconds) {
        threadPool.shutdown();
        try {
            if (!threadPool.awaitTermination(timeoutSeconds, TimeUnit.SECONDS)) {
                threadPool.shutdownNow();
            }
        } catch (InterruptedException e) {
            threadPool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    /**
     * @param n the {@link Node} to check
     * @return true if the node is in the ready queue
     */
    public boolean hasQueued(Node n) {
        return readyNodes.contains(n);
    }

    public void setQueueDownstreamNodes(boolean queueDownstreamNodes) {
        this.queueDownstreamNodes = queueDownstreamNodes;
    }

    public boolean getQueueDownstreamNodes() {
        return queueDownstreamNodes;
    }
}