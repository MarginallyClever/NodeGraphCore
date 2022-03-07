package com.marginallyClever.nodeGraphSwing;

import com.marginallyClever.nodeGraphSwing.nodes.images.BufferedImageJSON_DAO;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test JSON Data Access Objects.
 * @author Dan Royer
 * @since 2022-03-07
 */
public class TestJSON_DAO {
    /**
     * Test {@link BufferedImage}.
     */
    @Test
    public void testRectangleDAO() {
        BufferedImageJSON_DAO dao = new BufferedImageJSON_DAO();
        BufferedImage r1 = new BufferedImage(2,3,BufferedImage.TYPE_INT_RGB);
        BufferedImage r2=dao.fromJSON(dao.toJSON(r1));
        assertEquals(r1,r2);
    }
}
