package com.marginallyclever.donatello;

import com.marginallyclever.nodegraphcore.PrintWithGraphics;
import com.marginallyclever.nodegraphcore.Node;
import com.marginallyclever.nodegraphcore.NodeConnection;
import com.marginallyclever.nodegraphcore.NodeGraph;
import com.marginallyclever.nodegraphcore.NodeVariable;

import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link NodeGraphViewPanel} visualizes the contents of a {@link NodeGraph} with Java Swing.
 * It can call on {@link NodeGraphViewListener}s to add additional flavor.
 * Override this to implement a unique look and feel.
 * @author Dan Royer
 * @since 2022-02-11
 */
public class NodeGraphViewPanel extends JPanel {
    /**
     * The default {@link Node} background color.
     */
    public static final Color NODE_COLOR_BACKGROUND = Color.WHITE;
    /**
     * The default {@link Node} border color.
     */
    public static final Color NODE_COLOR_BORDER = Color.BLACK;
    /**
     * The default {@link Node} internal border between {@link NodeVariable}.
     */
    public static final Color NODE_COLOR_INTERNAL_BORDER = Color.DARK_GRAY;
    /**
     * The default {@link JPanel} background color.
     */
    public static final Color PANEL_COLOR_BACKGROUND = Color.LIGHT_GRAY;
    /**
     * The default {@link Node} font color.
     */
    public static final Color NODE_COLOR_FONT_CLEAN = Color.BLACK;
    /**
     * The default {@link Node} font color for variables when <pre>getIsDirty()</pre>. is true.
     */
    public static final Color NODE_COLOR_FONT_DIRTY = Color.RED;
    /**
     * The default {@link Node} tile bar font color
     */
    public static final Color NODE_COLOR_TITLE_FONT = Color.WHITE;
    /**
     * The default {@link Node} tile bar background color
     */
    public static final Color NODE_COLOR_TITLE_BACKGROUND = Color.BLACK;
    /**
     * The default {@link Node} female connection point color.
     */
    public static final Color CONNECTION_POINT_COLOR = Color.LIGHT_GRAY;
    /**
     * The default {@link Node} male connection point color.
     */
    public static final Color CONNECTION_COLOR = Color.BLUE;

    /**
     * The default {@link Node} outer border radius.
     */
    public static final int CORNER_RADIUS = 5;

    /**
     * Controls horizontal text alignment within a {@link Node} or {@link NodeVariable}.
     * See {@link #paintText(Graphics, String, Rectangle, int, int)} for more information.
     */
    public static final int ALIGN_LEFT=0;
    /**
     * Controls horizontal text alignment within a {@link Node} or {@link NodeVariable}.
     * See {@link #paintText(Graphics, String, Rectangle, int, int)} for more information.
     */
    public static final int ALIGN_RIGHT=1;
    /**
     * Controls horizontal or vertical text alignment within a {@link Node} or {@link NodeVariable}.
     * See {@link #paintText(Graphics, String, Rectangle, int, int)} for more information.
     */
    public static final int ALIGN_CENTER=2;
    /**
     * Controls vertical text alignment within a {@link Node} or {@link NodeVariable}.
     * See {@link #paintText(Graphics, String, Rectangle, int, int)} for more information.
     */
    public static final int ALIGN_TOP=0;
    /**
     * Controls vertical text alignment within a {@link Node} or {@link NodeVariable}.
     * See {@link #paintText(Graphics, String, Rectangle, int, int)} for more information.
     */
    public static final int ALIGN_BOTTOM=1;

    /**
     * the {@link NodeGraph} to edit.
     */
    private final NodeGraph model;

    /**
     * Constructs one new instance of {@link NodeGraphViewPanel}.
     * @param model the {@link NodeGraph} model to paint.
     */
    public NodeGraphViewPanel(NodeGraph model) {
        super();
        this.model=model;
        this.setBackground(PANEL_COLOR_BACKGROUND);
        this.setFocusable(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        updatePaintAreaBounds();
        super.paintComponent(g);

        paintNodesInBackground(g);

        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);

        for(Node n : model.getNodes()) paintNode(g,n);

        g.setColor(CONNECTION_COLOR);
        for(NodeConnection c : model.getConnections()) paintConnection(g,c);

        firePaintEvent(g);
    }

    /**
     * Paint all {@link Node}s that implement the {@link PrintWithGraphics} interface.
     * @param g the {@link Graphics} context.
     */
    private void paintNodesInBackground(Graphics g) {
        for(Node n : model.getNodes()) {
            if(n instanceof PrintWithGraphics) {
                ((PrintWithGraphics) n).print(g);
            }
        }
    }

    /**
     * Update the bounds of every node in the model {@link NodeGraph}.
     */
    public void updatePaintAreaBounds() {
        Rectangle r = this.getBounds();
        for(Node n : model.getNodes()) {
            n.updateBounds();
            Rectangle other = new Rectangle(n.getRectangle());
            //other.grow(100,100);
            r.add(other.getMinX(),other.getMinY());
            r.add(other.getMaxX(),other.getMaxY());
        }
        Dimension d = new Dimension(r.width,r.height);
        this.setMinimumSize(d);
        this.setMaximumSize(d);
        this.setPreferredSize(d);
        //System.out.println("Bounds="+r.toString());
    }

    /**
     * Paint one {@link Node}
     * @param g the {@link Graphics} context
     * @param n the {@link Node} to paint.
     */
    public void paintNode(Graphics g, Node n) {
        g.setColor(NODE_COLOR_BACKGROUND);
        paintNodeBackground(g,n);

        paintNodeTitleBar(g, n);

        paintAllNodeVariables(g, n);

        g.setColor(NODE_COLOR_BORDER);
        paintNodeBorder(g, n);
    }

    /**
     * Paint the background of one {@link Node}
     * @param g the {@link Graphics} context
     * @param n the {@link Node} to paint.
     */
    public void paintNodeBackground(Graphics g, Node n) {
        Rectangle r = n.getRectangle();
        g.fillRoundRect(r.x, r.y, r.width, r.height, CORNER_RADIUS, CORNER_RADIUS);
    }

    /**
     * Paint the title bar of one {@link Node}.
     * @param g the {@link Graphics} context
     * @param n the {@link Node} to paint.
     */
    public void paintNodeTitleBar(Graphics g, Node n) {
        Rectangle r = n.getRectangle();
        g.setColor(NODE_COLOR_TITLE_BACKGROUND);
        g.fillRoundRect(r.x, r.y, r.width, CORNER_RADIUS*2, CORNER_RADIUS, CORNER_RADIUS);
        g.fillRect(r.x, r.y+CORNER_RADIUS, r.width, Node.TITLE_HEIGHT -CORNER_RADIUS);

        Rectangle box = getNodeInternalBounds(n.getRectangle());
        g.setColor(NODE_COLOR_TITLE_FONT);
        box.height=Node.TITLE_HEIGHT;
        paintText(g,n.getLabel(),box,ALIGN_LEFT,ALIGN_CENTER);
        paintText(g,n.getName(),box,ALIGN_RIGHT,ALIGN_CENTER);
    }

    /**
     * Paint all the {@link NodeVariable}s in one {@link Node}.
     * @param g the {@link Graphics} context
     * @param n the {@link Node} to paint.
     */
    private void paintAllNodeVariables(Graphics g, Node n) {
        for(int i=0;i<n.getNumVariables();++i) {
            NodeVariable<?> v = n.getVariable(i);
            paintVariable(g,v);
        }
    }

    /**
     * Paint one {@link NodeVariable}.
     * @param g the {@link Graphics} context
     * @param v the {@link NodeVariable} to paint.
     */
    public void paintVariable(Graphics g, NodeVariable<?> v) {
        Rectangle box = v.getRectangle();
        Rectangle insideBox = getNodeInternalBounds(box);

        // label
        g.setColor(v.getIsDirty()?NODE_COLOR_FONT_DIRTY : NODE_COLOR_FONT_CLEAN);
        paintText(g,v.getName(),insideBox,ALIGN_LEFT,ALIGN_CENTER);

        // value
        Object vObj = v.getValue();
        if(vObj != null) {
            String val;
            int MAX_CHARS = 10;
            if(vObj instanceof String || vObj instanceof Number) {
                val = vObj.toString();
            } else {
                val = v.getTypeName();
            }
            if (val.length() > MAX_CHARS) val = val.substring(0, MAX_CHARS) + "...";
            paintText(g, val, insideBox, ALIGN_RIGHT, ALIGN_CENTER);
        }

        // internal border
        g.setColor(NODE_COLOR_INTERNAL_BORDER);
        g.drawLine((int)box.getMinX(),(int)box.getMinY(),(int)box.getMaxX(),(int)box.getMinY());

        // connection points
        g.setColor(CONNECTION_POINT_COLOR);
        paintVariableConnectionPoints(g,v);
    }

    /**
     * Returns the adjusted inner bounds of a {@link Node}.
     * Nodes have a left and right margin useful for printing labels and values without overlapping the {@link NodeConnection} points.
     * these edges form an inner bound.  Given a {@link NodeVariable#getRectangle()}, this
     * @param r the outer bounsd of the node.
     * @return the adjusted inner bounds of a {@link Node}.
     */
    public Rectangle getNodeInternalBounds(Rectangle r) {
        Rectangle r2 = new Rectangle(r);
        int padding = (int)NodeConnection.DEFAULT_RADIUS+4;
        r2.x += padding;
        r2.width -= padding*2;
        return r2;
    }

    /**
     * Paint the outside border of one {@link Node}.
     * @param g the {@link Graphics} context
     * @param n the {@link Node} to paint.
     */
    public void paintNodeBorder(Graphics g,Node n) {
        Rectangle r = n.getRectangle();
        g.drawRoundRect(r.x, r.y, r.width, r.height,CORNER_RADIUS,CORNER_RADIUS);
    }

    /**
     * Paint the female end of connection points of one {@link NodeVariable}.
     * @param g the {@link Graphics} context
     * @param v the {@link NodeVariable} to paint.
     */
    public void paintVariableConnectionPoints(Graphics g, NodeVariable<?> v) {
        if(v.getHasInput()) {
            Point p = v.getInPosition();
            int radius = (int)NodeConnection.DEFAULT_RADIUS+2;
            g.drawOval(p.x-radius,p.y-radius,radius*2,radius*2);
        }
        if(v.getHasOutput()) {
            Point p = v.getOutPosition();
            int radius = (int)NodeConnection.DEFAULT_RADIUS+2;
            g.drawOval(p.x-radius,p.y-radius,radius*2,radius*2);
        }
    }

    /**
     * Use the graphics context to paint text within a box with the provided alignment.
     * @param g the graphics context
     * @param str the text to paint
     * @param box the bounding limits
     * @param alignH the desired horizontal alignment.  Can be any one of {@link NodeGraphViewPanel#ALIGN_LEFT}, {@link NodeGraphViewPanel#ALIGN_RIGHT}, or {@link NodeGraphViewPanel#ALIGN_CENTER}
     * @param alignV the desired vertical alignment.  Can be any one of {@link NodeGraphViewPanel#ALIGN_TOP}, {@link NodeGraphViewPanel#ALIGN_BOTTOM}, or {@link NodeGraphViewPanel#ALIGN_CENTER}
     */
    public void paintText(Graphics g,String str,Rectangle box,int alignH,int alignV) {
        if(str==null || str.isEmpty()) return;

        FontRenderContext frc = new FontRenderContext(null, false, false);
        TextLayout layout = new TextLayout(str,g.getFont(),frc);
        FontMetrics metrics = g.getFontMetrics();
        int h = metrics.getHeight();
        int w = metrics.stringWidth(str);

        int x,y;
        switch(alignH) {
            default: x = (int)box.getMinX(); break;
            case ALIGN_RIGHT: x = (int)( box.getMaxX() - w ); break;
            case ALIGN_CENTER: x = (int)( box.getMinX() + (box.getWidth() - w )/2); break;
        }
        switch(alignV) {
            default: y = (int)( box.getMinY() + h ); break;
            case ALIGN_BOTTOM: y = (int)( box.getMaxY() ); break;
            case ALIGN_CENTER: y = (int)( box.getMinY() + (box.getHeight() + h )/2); break;
        }
        layout.draw((Graphics2D)g,x,y);
    }

    /**
     * Paint the male end of connection points at this {@link NodeVariable}.
     * @param g the {@link Graphics} context
     * @param c the {@link NodeVariable} to paint.
     */
    public void paintConnection(Graphics g, NodeConnection c) {
        Point p0 = c.getInPosition();
        Point p3 = c.getOutPosition();
        paintBezierBetweenTwoPoints(g,p0,p3);

        if(c.isOutputValid()) paintConnectionAtPoint(g,c.getOutPosition());
        if(c.isInputValid()) paintConnectionAtPoint(g,c.getInPosition());
    }

    /**
     * Paint the male end of one connection point.
     * @param g the {@link Graphics} context
     * @param p the center of male end to paint.
     */
    public void paintConnectionAtPoint(Graphics g,Point p) {
        int radius = (int) NodeConnection.DEFAULT_RADIUS;
        g.fillOval( p.x - radius, p.y - radius, radius * 2, radius * 2);
    }

    /**
     * Paint a cubic bezier using {@link Graphics} from p0 to p3.
     * @param g the {@link Graphics} painting tool.
     * @param p0 the first point of the cubic bezier spline.
     * @param p3 the last point of the cubic bezier spline.
     */
    public void paintBezierBetweenTwoPoints(Graphics g,Point p0, Point p3) {
        Point p1 = new Point(p0);
        Point p2 = new Point(p3);

        int d=Math.abs(p3.x-p1.x)/2;
        p1.x+=d;
        p2.x-=d;

        Bezier b = new Bezier(
                p0.x,p0.y,
                p1.x,p1.y,
                p2.x,p2.y,
                p3.x,p3.y);
        drawBezier(g,b);
    }

    private void drawBezier(Graphics g, Bezier b) {
        List<Point2D> points = b.generateCurvePoints(0.2);
        int len=points.size();
        int [] x = new int[len];
        int [] y = new int[len];
        for(int i=0;i<len;++i) {
            Point2D p = points.get(i);
            x[i]=(int)p.x;
            y[i]=(int)p.y;
        }
        g.drawPolyline(x,y,len);
    }

    /**
     * listener pattern for painting via {@link NodeGraphViewListener#paint(Graphics, NodeGraphViewPanel)}.
     */
    private final List<NodeGraphViewListener> listeners = new ArrayList<>();

    /**
     * {@link NodeGraphViewListener}s register here.
     * @param p the {@link NodeGraphViewListener} to register.
     */
    public void addViewListener(NodeGraphViewListener p) {
        listeners.add(p);
    }

    /**
     * {@link NodeGraphViewListener}s unregister here.
     * @param p the {@link NodeGraphViewListener} to unregister.
     */
    public void removeViewListener(NodeGraphViewListener p) {
        listeners.remove(p);
    }

    private void firePaintEvent(Graphics g) {
        for( NodeGraphViewListener p : listeners ) {
            p.paint(g, this);
        }
    }

    /**
     * Sets the Graphics context line width.
     * @param g the {@link Graphics} context
     * @param r thew new line width.
     */
    public void setLineWidth(Graphics g,float r) {
        Graphics2D g2 = (Graphics2D)g;
        g2.setStroke(new BasicStroke(r));
    }
}
