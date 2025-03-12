package com.marginallyclever.nodegraphcore;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.ServiceLoader;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestDAO4JSONFactory {
    @AfterEach
    public void afterEach() {
        DAO4JSONFactory.clear();
    }

    @Test
    public void testFindNodes() {
        assertEquals(0,DAO4JSONFactory.getNames().length);
        ServiceLoader<DAORegistry> loader = ServiceLoader.load(DAORegistry.class);
        for(DAORegistry registry : loader) {
            registry.registerDAO();
        }
        assert(DAO4JSONFactory.size()>0);
    }

    @Test
    public void testFindNodes2() {
        assertEquals(0,DAO4JSONFactory.getNames().length);
        DAO4JSONFactory.registerAllDAOInPackage("com.marginallyclever.nodegraphcore.json");
        assert(DAO4JSONFactory.size()>0);
    }
}
