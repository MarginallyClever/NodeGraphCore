package com.marginallyClever.nodeGraphCore;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.util.ServiceLoader;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestJSON_DAO_Factory {
    @AfterAll
    public static void afterAll() {
        JSON_DAO_Factory.clear();
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
        assertEquals(2,count);
    }
}
