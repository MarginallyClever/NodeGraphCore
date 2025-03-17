package com.marginallyclever.nodegraphcore;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.FileSystems;
import java.util.Arrays;
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

    @Test
    public void listAllNodesRegistered() {
        NodeFactory.loadRegistries();
        System.out.println("All registered nodegraphcore nodes: "+ Arrays.toString(NodeFactory.getNames()));
        NodeFactory.clear();
    }

    @Test
    public void testLoadingDonatelloExtensionsIfAvailable() {
        String sep = FileSystems.getDefault().getSeparator();
        String path = System.getProperty("user.home") + sep + "Donatello" + sep + "extensions" + sep;

        if((new File(path).exists())) {
            System.out.println("Loading Donatello extensions from " + path);
            ServiceLoaderHelper.addAllPathFiles(path);
            listAllNodesRegistered();
        }
    }
}
