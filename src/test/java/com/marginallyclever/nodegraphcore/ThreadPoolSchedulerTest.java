package com.marginallyclever.nodegraphcore;

import com.marginallyclever.nodegraphcore.nodes.LoadNumber;
import com.marginallyclever.nodegraphcore.nodes.PrintToStdOut;
import com.marginallyclever.nodegraphcore.nodes.math.Multiply;
import org.junit.jupiter.api.Test;

public class ThreadPoolSchedulerTest {
    @Test
    public void test() {
        // Create a thread pool scheduler with 4 threads
        ThreadPoolScheduler scheduler = new ThreadPoolScheduler();

        // Create nodes
        LoadNumber nodeA = new LoadNumber(5);
        LoadNumber nodeB = new LoadNumber(3);
        Multiply multiply = new Multiply();
        PrintToStdOut printNode = new PrintToStdOut();

        // Connect nodes
        new Connection(nodeA, 2, multiply, 0);
        new Connection(nodeB, 2, multiply, 1);
        new Connection(multiply, 2, printNode, 0);

        // Submit nodes to the scheduler
        scheduler.submit(nodeA);
        scheduler.submit(nodeB);

        scheduler.run();

        // Shutdown the scheduler after execution
        scheduler.shutdown(10);
    }
}

