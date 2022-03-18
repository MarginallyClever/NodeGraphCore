package com.marginallyclever.donatello.nodes.images.blend;

import com.marginallyclever.nodegraphcore.Node;
import com.marginallyclever.nodegraphcore.NodeVariable;

import java.awt.image.BufferedImage;

/**
 * <a href='https://en.wikipedia.org/wiki/Blend_modes'>blend difference</a>
 * @author Dan Royer
 * @since 2022-02-23
 */
public class BlendDifference extends Node {
    private final NodeVariable<BufferedImage> a = NodeVariable.newInstance("a", BufferedImage.class,new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB),true,false);
    private final NodeVariable<BufferedImage> b = NodeVariable.newInstance("b", BufferedImage.class,new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB),true,false);
    private final NodeVariable<BufferedImage> output = NodeVariable.newInstance("output", BufferedImage.class,new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB),false,true);

    /**
     * Constructor for subclasses to call.
     */
    public BlendDifference() {
        super("BlendDifference");
        addVariable(a);
        addVariable(b);
        addVariable(output);
    }

    /**
     * Constructor that sets starting values.
     * @param a the starting value.
     * @param b the starting value.
     */
    public BlendDifference(BufferedImage a, BufferedImage b) {
        this();
        this.a.setValue(a);
        this.b.setValue(b);
    }

    @Override
    public Node create() {
        return new BlendDifference();
    }

    @Override
    public void update() {
        BufferedImage A = a.getValue();
        BufferedImage B = b.getValue();

        int w = (int)Math.min(A.getWidth(),B.getWidth());
        int h = (int)Math.min(A.getHeight(),B.getHeight());
        BufferedImage C = new BufferedImage(w,h,A.getType());

        for(int y=0;y<h;++y) {
            for(int x=0;x<w;++x) {
                int cA = A.getRGB(x,y);
                int cB = B.getRGB(x,y);
                int ar = ( cA >> 16 ) & 0xff;
                int ag = ( cA >>  8 ) & 0xff;
                int ab = ( cA       ) & 0xff;
                int br = ( cB >> 16 ) & 0xff;
                int bg = ( cB >>  8 ) & 0xff;
                int bb = ( cB       ) & 0xff;
                int dr = Math.abs(ar - br);
                int dg = Math.abs(ag - bg);
                int db = Math.abs(ab - bb);
                int cC = (dr << 16) | (dg << 8) | db;
                C.setRGB(x,y,cC);
            }
        }
        output.setValue(C);
        cleanAllInputs();
    }

}
