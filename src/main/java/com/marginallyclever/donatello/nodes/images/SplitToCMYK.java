package com.marginallyclever.donatello.nodes.images;

import com.marginallyclever.nodegraphcore.Node;
import com.marginallyclever.nodegraphcore.NodeVariable;

import java.awt.image.BufferedImage;

/**
 * Split a {@link BufferedImage} into separate CMYK channels.
 * @author Dan Royer
 * @since 2022-02-23
 */
public class SplitToCMYK extends Node {
    private final NodeVariable<BufferedImage> image   = NodeVariable.newInstance("image",   BufferedImage.class, new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB),true,false);
    private final NodeVariable<BufferedImage> cyan    = NodeVariable.newInstance("Cyan",    BufferedImage.class, new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB),false,true);
    private final NodeVariable<BufferedImage> magenta = NodeVariable.newInstance("Magenta", BufferedImage.class, new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB),false,true);
    private final NodeVariable<BufferedImage> yellow  = NodeVariable.newInstance("Yellow",  BufferedImage.class, new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB),false,true);
    private final NodeVariable<BufferedImage> black   = NodeVariable.newInstance("Black",   BufferedImage.class, new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB),false,true);

    /**
     * Constructor for subclasses to call.
     */
    public SplitToCMYK() {
        super("SplitToCMYK");
        addVariable(image);
        addVariable(cyan);
        addVariable(magenta);
        addVariable(yellow);
        addVariable(black);
    }

    /**
     * Constructor that sets starting values.
     * @param img the starting value.
     */
    public SplitToCMYK(BufferedImage img) {
        this();
        this.image.setValue(img);
    }

    @Override
    public Node create() {
        return new SplitToCMYK();
    }

    @Override
    public void update() {
        BufferedImage src = image.getValue();
        int h = src.getHeight();
        int w = src.getWidth();

        BufferedImage channelCyan    = new BufferedImage(w,h,src.getType());
        BufferedImage channelMagenta = new BufferedImage(w,h,src.getType());
        BufferedImage channelYellow  = new BufferedImage(w,h,src.getType());
        BufferedImage channelBlack   = new BufferedImage(w,h,src.getType());

        for (int py = 0; py < h; ++py) {
            for (int px = 0; px < w; ++px) {
                int pixel = src.getRGB(px,py);
                //double a = 255-((pixel>>24) & 0xff);
                double r = 1.0-(double)((pixel >> 16) & 0xff) / 255.0;
                double g = 1.0-(double)((pixel >>  8) & 0xff) / 255.0;
                double b = 1.0-(double)((pixel      ) & 0xff) / 255.0;
                // now convert to cmyk
                double k = Math.min(Math.min(r,g),b);   // should be Math.max(Math.max(r,g),b) but colors are inverted.
                double ik = 1.0 - k;

                //if(ik<1.0/255.0) {
                //  c=m=y=0;
                //} else {
                    int c = (int)Math.max(0,Math.min(255, 255 * (r-k) / k ));
                    int m = (int)Math.max(0,Math.min(255, 255 * (g-k) / k ));
                    int y = (int)Math.max(0,Math.min(255, 255 * (b-k) / k ));
                    int k2 = (int)Math.max(0,Math.min(255, 255 * ik ));
                //}
                channelCyan   .setRGB(px, py, RGBtoInt(c,c,c));
                channelMagenta.setRGB(px, py, RGBtoInt(m,m,m));
                channelYellow .setRGB(px, py, RGBtoInt(y,y,y));
                channelBlack  .setRGB(px, py, RGBtoInt(k2,k2,k2));
            }
        }

        cyan   .setValue(channelCyan);
        magenta.setValue(channelMagenta);
        yellow .setValue(channelYellow);
        black  .setValue(channelBlack);

        cleanAllInputs();
    }

    private int RGBtoInt(int r, int g, int b) {
        r = ( r & 0xff ) << 16;
        g = ( g & 0xff ) << 8;
        b = ( b & 0xff );
        return r|g|b;
    }
}
