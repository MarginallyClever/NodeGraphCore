package com.marginallyclever.nodegraphcore;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.util.ServiceLoader;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestDAO4JSONFactory {
    @AfterAll
    public static void afterAll() {
        DAO4JSONFactory.clear();
    }

    @Test
    public void testFindNodes() {
        assertEquals(0,NodeFactory.getNames().length);
        ServiceLoader<DAORegistry> loader = ServiceLoader.load(DAORegistry.class);
        int count=0;
        for(DAORegistry registry : loader) {
            registry.registerDAO();
            ++count;
        }
        assertEquals(1,count);
    }
}
