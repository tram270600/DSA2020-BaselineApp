package geom;

import java.awt.*;
import geom.GeomObject;
import geom.Graph;
import geom.Point2D;
import geom.Rectangle;

public class Axis extends GeomObject {

    private Viewport viewPort;
    public Viewport getViewPort () {return viewPort;}
    public static final int NUM_GRAPHS = 20;

    public Axis (double xMin, double xMax, double yMin, double yMax) {
        this.viewPort = new Viewport (xMin, xMax, yMin, yMax);
    }
    public Axis (Viewport vp) {
        this.viewPort = vp.clone();
    }

    @Override
    public void draw(Graphics g, SpaceMapping mapper) {
        drawXAxis(g, mapper);
        drawYAxis(g, mapper);
            for(int idx=0; idx < this.nGraph; idx++){
                this.graphs[idx].draw(g, mapper);
            }
        }

  
    Graph[] graphs = new Graph[NUM_GRAPHS]; //support Maximum 20 graphs
    int nGraph = 0;

    private void _addGraph(Graph graph){
        if(this.nGraph >= NUM_GRAPHS) return; //do nothing if it contains more then maximum graph
        this.graphs[this.nGraph++] = graph;
        this.getViewPort().combine(graph.getViewport());
    }
     public void addGraph(Graph graph, Color color){
        this._addGraph(graph);
        graph.edgeColor = color;
        
    }
    
    public void addGraph(Graph graph, Color color, int line_weight){
        this._addGraph(graph);
        graph.line_weight = line_weight;
        graph.edgeColor = color;
    }
    
    protected void drawXAxis (Graphics g, SpaceMapping mapper) {
        Graphics2D g2 = (Graphics2D) g;

        //Draw horizontal line at the bottom ( yMin)
        Point2D devXLeft = mapper.logic2Device(new Point2D(this.getViewPort().getxMin(), this.getViewPort().getyMin()));
        Point2D devXRight = mapper.logic2Device(new Point2D(this.getViewPort().getxMax(), this.getViewPort().getyMin()));

        // make x,y of coordinate of left point and right point
        int x1 = (int) devXLeft.getX(), y1 = (int) devXLeft.getY();
        int x2 = (int) devXRight.getX(), y2 = (int) devXRight.getY();

        //Set color
        g.setColor(Color.BLACK);
        //Thick
        Stroke stroke = new BasicStroke( 2);
        g2.setStroke(stroke);
        //DrawLine
        g2.drawLine(x1,y1,x2,y2);

        //Draw ticks
        double step = 5*mapper.stepx10(); // Step-size
        int numTick = (int) ((this.getViewPort().getxMax() - this.getViewPort().getxMin())/step) + 1;
        double xTick = this.getViewPort().getxMin();

        String sTick = "";
        for (int idx = 0; idx < numTick; idx ++) {
            sTick = String.format("%6.2f",xTick);

            //Draw the position and mark the number
            Point2D devTickPos = mapper.logic2Device(new Point2D(xTick, this.getViewPort().getyMin()));
            x1 = (int) devTickPos.getX(); y1 = (int) devTickPos.getY();
            //Get font
            FontMetrics fm = g.getFontMetrics();

            int sx = x1 - fm.stringWidth(sTick) / 2; //"-" means shifting left
            int sy = y1 + fm.getHeight();

            //draw string in black
            g.setColor(Color.black);
            g.drawString(sTick, sx, sy);
            x2 = x1; y2 = y1 + 5;

            //draw tick in blue
            g.setColor(Color.blue);
            stroke = new BasicStroke(1);
            g2.setStroke(stroke);
            g2.drawLine(x1, y1, x2, y2);

            xTick += step;


        }
    }

    protected void drawYAxis (Graphics g, SpaceMapping mapper) {
        Graphics2D g2 = (Graphics2D) g;

        //Draw vertical line on the left xMin
        Point2D devYbottom = mapper.logic2Device(new Point2D (this.getViewPort().getxMin(), this.getViewPort().getyMin()));
        Point2D devYTop = mapper.logic2Device(new Point2D(this.getViewPort().getxMin(), this.getViewPort().getyMax()));

        // make x,y of coordinate of top point and right point
        int x1 = (int) devYTop.getX(), y1 = (int) devYTop.getY();
        int x2 = (int) devYbottom.getX(), y2 = (int) devYbottom.getY();

        //Set Color
        g.setColor(Color.BLACK);
        //Thick
        Stroke stroke = new BasicStroke(2);
        g2.setStroke(stroke);
        //DrawLine
        g2.drawLine(x1,y1,x2,y2);

        //Draw Ticks
        double step = 5*mapper.stepy10();
        int numTick = (int) ((this.getViewPort().getyMax() - this.getViewPort().getyMin())/step) + 1;
        double yTick = this.getViewPort().getyMin();

        String sTick = "";
        for (int idx = 0; idx < numTick; idx ++) {
            sTick = String.format("%5.2f", yTick);

            Point2D devTickPos = mapper.logic2Device(this.getViewPort().getxMin(), yTick);
            x1 = (int) devTickPos.getX();  y1 = (int) devTickPos.getY();
            FontMetrics fm = g.getFontMetrics();

            int sx = x1 - fm.stringWidth(sTick) - 10; //"-" means shifting left
            int sy = y1 + fm.getAscent()/2;

            //draw string in black
            g.setColor(Color.black);
            g.drawString(sTick, sx, sy);
            x2 = x1 - 10; y2 = y1 ;

            //draw tick in blue
            g.setColor(Color.blue);
            stroke = new BasicStroke(1);
            g2.setStroke(stroke);
            g2.drawLine(x1, y1, x2, y2);

            yTick += step;
        }


    }
}
