package com.marginallyClever.nodeGraphSwing;

import com.marginallyClever.nodeGraphCore.BuiltInRegistry;
import com.marginallyClever.nodeGraphCore.NodeGraph;
import com.marginallyClever.nodeGraphSwing.nodes.images.BufferedImageJSON_DAO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;

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
        SwingRegistry.register();
    }

    private boolean bufferedImagesEqual(BufferedImage img1, BufferedImage img2) {
        if(img1.getType() != img2.getType()
            || img1.getWidth() != img2.getWidth()
            || img1.getHeight() != img2.getHeight()) {
            return false;
        }

        for (int x = 0; x < img1.getWidth(); x++) {
            for (int y = 0; y < img1.getHeight(); y++) {
                if (img1.getRGB(x, y) != img2.getRGB(x, y))
                    return false;
            }
        }

        return true;
    }

    /**
     * Test {@link BufferedImage}.
     */
    @Test
    public void testBufferedImageDAO() {
        BufferedImageJSON_DAO dao = new BufferedImageJSON_DAO();
        BufferedImage r1 = new BufferedImage(2,3,BufferedImage.TYPE_INT_RGB);
        BufferedImage r2=dao.fromJSON(dao.toJSON(r1));
        assert(bufferedImagesEqual(r1,r2));
    }
}
