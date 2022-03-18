package com.marginallyclever.nodegraphcore;

import com.marginallyclever.nodegraphcore.json.RectangleDAO4JSON;
import com.marginallyclever.nodegraphcore.json.StringDAO4JSON;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test JSON Data Access Objects.
 * @author Dan Royer
 * @since 2022-03-07
 */
public class TestDAO4JSON {
    @BeforeAll
    public static void beforeAll() {
        NodeFactory.loadRegistries();
        DAO4JSONFactory.loadRegistries();
    }

    @AfterAll
    public static void afterAll() {
        NodeFactory.clear();
        DAO4JSONFactory.clear();
    }

    /**
     * Test {@link Rectangle}.
     */
    @Test
    public void testRectangleDAO() {
        RectangleDAO4JSON dao = new RectangleDAO4JSON();
        Rectangle r1 = new Rectangle(1,2,3,4);
        Rectangle r2=dao.fromJSON(dao.toJSON(r1));
        assertEquals(r1,r2);
    }

    /**
     * Test {@link String}.
     */
    @Test
    public void testStringDAO() {
        StringDAO4JSON dao = new StringDAO4JSON();
        String r1 = new String("abcd");
        String r2=dao.fromJSON(dao.toJSON(r1));
        assertEquals(r1,r2);
    }
}
