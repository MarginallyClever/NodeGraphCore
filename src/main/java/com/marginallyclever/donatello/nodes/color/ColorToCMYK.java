package com.marginallyclever.donatello.nodes.color;

import com.marginallyclever.donatello.nodes.images.ColorHelper;
import com.marginallyclever.nodegraphcore.Node;
import com.marginallyclever.nodegraphcore.NodeVariable;

import java.awt.*;

/**
 * Separates a color into its four component CMYK channels.  Each channel is a value 0...1.
 * @author Dan Royer
 * @since 2022-03-19
 */
public class ColorToCMYK extends Node {
    private final NodeVariable<Color> color = NodeVariable.newInstance("color", Color.class, new Color(0,0,0,0),true,false);
    private final NodeVariable<Number> cyan = NodeVariable.newInstance("cyan", Number.class, 0,false,true);
    private final NodeVariable<Number> magenta = NodeVariable.newInstance("magenta", Number.class, 0,false,true);
    private final NodeVariable<Number> yellow = NodeVariable.newInstance("yellow", Number.class, 0,false,true);
    private final NodeVariable<Number> black = NodeVariable.newInstance("black", Number.class, 0,false,true);

    /**
     * Constructor for subclasses to call.
     */
    public ColorToCMYK() {
        super("ColorToCMYK");
        addVariable(color);
        addVariable(cyan);
        addVariable(magenta);
        addVariable(yellow);
        addVariable(black);
    }

    /**
     * Constructor that sets starting values.
     * @param color the RGB color starting value.
     */
    public ColorToCMYK(Color color) {
        this();
        this.color.setValue(color);
    }

    @Override
    public Node create() {
        return new ColorToCMYK();
    }

    @Override
    public void update() {
        Color c = color.getValue();
        double [] cmyk = ColorHelper.IntToCMYK(ColorHelper.ColorToInt(c));
        cyan.setValue(   cmyk[0]/255.0);
        magenta.setValue(cmyk[1]/255.0);
        yellow.setValue( cmyk[2]/255.0);
        black.setValue(  cmyk[3]/255.0);
        cleanAllInputs();
    }
}
