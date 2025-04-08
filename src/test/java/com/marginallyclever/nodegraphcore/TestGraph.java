package com.marginallyclever.nodegraphcore;

import com.marginallyclever.nodegraphcore.nodes.LoadNumber;
import com.marginallyclever.nodegraphcore.nodes.PrintToStdOut;
import com.marginallyclever.nodegraphcore.port.Port;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class TestGraph {
    @Test
    public void testGraphPorts() {
        Graph graph = new Graph();
        LoadNumber loadNumber = new LoadNumber();
        PrintToStdOut printToStdOut = new PrintToStdOut();
        graph.add(loadNumber);
        graph.add(printToStdOut);
        Assertions.assertEquals(0,graph.getInputs().size());

        List<Port<?>> ports = new ArrayList<>();
        ports.add(loadNumber.getPort(0));
        ports.add(printToStdOut.getPort(0));
        graph.addPorts(ports);

        Assertions.assertEquals(2,graph.getInputs().size());
        Assertions.assertEquals(0,graph.getOutputs().size());
    }
}
