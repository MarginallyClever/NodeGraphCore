package com.marginallyClever.nodeGraphCore;

import com.marginallyClever.nodeGraphCore.json.JSON_DAO_Factory;
import com.marginallyClever.nodeGraphCore.json.RectangleJSON_DAO;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test JSON Data Access Objects.
 * @author Dan Royer
 * @since 2022-03-07
 */
public class TestJSON_DAO {
    /**
     * Test {@link Rectangle}.
     */
    @Test
    public void testRectangleDAOConvertBothWays() {
        RectangleJSON_DAO dao = new RectangleJSON_DAO();
        Rectangle r1 = new Rectangle(1,2,3,4);
        Rectangle r2=dao.fromJSON(dao.toJSON(r1));
        assertEquals(r1,r2);
    }
}
