package com.marginallyClever.nodeGraphCore;

import com.marginallyClever.nodeGraphCore.json.RectangleJSON_DAO;
import com.marginallyClever.nodeGraphCore.json.StringJSON_DAO;
import com.marginallyClever.nodeGraphSwing.SwingRegistry;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test JSON Data Access Objects.
 * @author Dan Royer
 * @since 2022-03-07
 */
public class TestJSON_DAO {
    @BeforeAll
    public static void beforeAll() {
        try {
            BuiltInRegistry.register();
        } catch (IllegalArgumentException e) {}
    }

    /**
     * Test {@link Rectangle}.
     */
    @Test
    public void testRectangleDAO() {
        RectangleJSON_DAO dao = new RectangleJSON_DAO();
        Rectangle r1 = new Rectangle(1,2,3,4);
        Rectangle r2=dao.fromJSON(dao.toJSON(r1));
        assertEquals(r1,r2);
    }

    /**
     * Test {@link String}.
     */
    @Test
    public void testStringDAO() {
        StringJSON_DAO dao = new StringJSON_DAO();
        String r1 = new String("abcd");
        String r2=dao.fromJSON(dao.toJSON(r1));
        assertEquals(r1,r2);
    }
}
