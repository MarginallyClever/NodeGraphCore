package com.marginallyClever.nodeGraphSwing.nodes.images;

import com.marginallyClever.nodeGraphCore.Node;
import com.marginallyClever.nodeGraphCore.NodeVariable;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * This {@link Node} can load a Swing {@link BufferedImage}.
 * @author Dan Royer
 * @since 2022-02-23
 */
public class LoadImage extends Node {
    private final NodeVariable<String> filename = NodeVariable.newInstance("filename",String.class,null,true,false);
    private final NodeVariable<BufferedImage> contents = NodeVariable.newInstance("contents", BufferedImage.class, new BufferedImage(1,1,BufferedImage.TYPE_INT_RGB),false,true);
    private final NodeVariable<Number> width = NodeVariable.newInstance("width",Number.class,0,false,true);
    private final NodeVariable<Number> height = NodeVariable.newInstance("height",Number.class,0,false,true);

    /**
     * Constructor for subclasses to call.
     */
    public LoadImage() {
        super("LoadImage");
        addVariable(filename);
        addVariable(contents);
        addVariable(width);
        addVariable(height);
    }

    /**
     * Constructor that sets a starting value
     * @param filename the starting value.
     */
    public LoadImage(String filename) {
        this();
        this.filename.setValue(filename);
    }

    @Override
    public Node create() {
        return new LoadImage();
    }

    @Override
    public void update() {
        try {
            BufferedImage image = ImageIO.read(new File(filename.getValue()));
            contents.setValue(image);
            width.setValue(image.getWidth());
            height.setValue(image.getHeight());
            cleanAllInputs();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
