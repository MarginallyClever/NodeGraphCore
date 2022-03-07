package com.marginallyClever.nodeGraphSwing.nodes.images;

import com.marginallyClever.nodeGraphCore.Node;
import com.marginallyClever.nodeGraphCore.NodeVariable;

import java.awt.image.BufferedImage;

/**
 * The difference between two images
 * @author Dan Royer
 * @since 2022-02-23
 */
public class Difference extends Node {
    private final NodeVariable<BufferedImage> a = NodeVariable.newInstance("a", BufferedImage.class,new BufferedImage(1,1,BufferedImage.TYPE_INT_RGB),true,false);
    private final NodeVariable<BufferedImage> b = NodeVariable.newInstance("b", BufferedImage.class,new BufferedImage(1,1,BufferedImage.TYPE_INT_RGB),true,false);
    private final NodeVariable<BufferedImage> output = NodeVariable.newInstance("output", BufferedImage.class,new BufferedImage(1,1,BufferedImage.TYPE_INT_RGB),true,false);

    /**
     * Constructor for subclasses to call.
     */
    public Difference() {
        super("Difference");
        addVariable(a);
        addVariable(b);
        addVariable(output);
    }

    /**
     * Constructor that sets starting values.
     * @param a the starting value.
     * @param b the starting value.
     */
    public Difference(BufferedImage a, BufferedImage b) {
        this();
        this.a.setValue(a);
        this.b.setValue(b);
    }

    @Override
    public Node create() {
        return new Difference();
    }

    @Override
    public void update() {
        BufferedImage A = a.getValue();
        BufferedImage B = b.getValue();

        int w = (int)Math.min(A.getWidth(),B.getWidth());
        int h = (int)Math.min(A.getHeight(),B.getHeight());
        BufferedImage C = new BufferedImage(w,h,BufferedImage.TYPE_INT_RGB);

        for(int y=0;y<h;++y) {
            for(int x=0;x<w;++x) {
                int cA = A.getRGB(x,y);
                int cB = B.getRGB(x,y);
                int ar = cA & 0xff0000;
                int ag = cA & 0x00ff00;
                int ab = cA & 0x0000ff;
                int br = cB & 0xff0000;
                int bg = cB & 0x00ff00;
                int bb = cB & 0x0000ff;
                int dr = (ar - br) & 0xff0000;
                int dg = (ag - bg) & 0x00ff00;
                int db = (ab - bb) & 0x0000ff;
                int cC = dr | dg | db;
                C.setRGB(x,y,cC);
            }
        }
        output.setValue(C);
        cleanAllInputs();
    }

}
