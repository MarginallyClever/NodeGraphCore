package com.marginallyclever.nodegraphcore;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A scheduler that runs a graph of nodes using a thread pool.
 */
public class ThreadPoolScheduler {
    private final ExecutorService threadPool = Executors.newVirtualThreadPerTaskExecutor();
    private final BlockingQueue<Node> readyNodes = new LinkedBlockingQueue<>();
    private final AtomicInteger activeTasks = new AtomicInteger(0);

    public ThreadPoolScheduler() {}

    /**
     * Submits a node to the scheduler for execution.
     * @param node The node to submit
     */
    public void submit(Node node) {
        if(readyNodes.contains(node)) {
            // move node to the end of the queue
            readyNodes.remove(node);
            readyNodes.add(node);
        } else {
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
            try {
                node.update();
                node.updateBounds();
                node.setInputsClean();
                for(Node downstreamNode : node.getDownstreamNodes()) {
                    if(downstreamNode.isDirty()) {
                        submit(downstreamNode); // Submit downstream nodes
                    }
                }
            } catch (Exception e) {
                System.err.println("Error in node execution: " + e.getMessage());
                e.printStackTrace();
            } finally {
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
}