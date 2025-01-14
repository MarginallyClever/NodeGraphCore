package com.marginallyclever.nodegraphcore.scheduler;

import org.junit.jupiter.api.Test;

public class ThreadPoolSchedulerTest {
    @Test
    public void test() {
        // Create a thread pool scheduler with 4 threads and 10-second timeout
        ThreadPoolScheduler scheduler = new ThreadPoolScheduler(4);

        // Create nodes
        MultiplyNode node1 = new MultiplyNode(2);
        MultiplyNode node2 = new MultiplyNode(3);
        PrintNode printNode = new PrintNode();

        // Connect nodes
        node1.connect(node2);
        node2.connect(printNode);

        // Provide input to the first node
        node1.addInput(5);

        // Submit nodes to the scheduler
        scheduler.submit(node1);

        scheduler.run();

        // Shutdown the scheduler after execution
        scheduler.shutdown();
    }
}

