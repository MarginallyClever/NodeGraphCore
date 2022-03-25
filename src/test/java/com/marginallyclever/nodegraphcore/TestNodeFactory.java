package com.marginallyclever.nodegraphcore;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.util.ServiceLoader;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestNodeFactory {
    @AfterAll
    public static void afterAll() {
        NodeFactory.clear();
    }

    @Test
    public void testFindNodes() {
        assertEquals(0,NodeFactory.getNames().length);
        ServiceLoader<NodeRegistry> loader = ServiceLoader.load(NodeRegistry.class);
        int count=0;
        for(NodeRegistry registry : loader) {
            registry.registerNodes();
            ++count;
        }
        assertEquals(1,count);
    }
}
