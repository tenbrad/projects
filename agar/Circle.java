import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public abstract class Circle{
  private double PI = 3.1415926535;
  protected double rx, ry;     // position
  protected double vx, vy;     // velocity
  protected double v;          // overall velocity
  protected double radius;   // radius
  private double inc;
  protected final double tol;
  protected Board board;
  private Cell prev;
  private Color colour;        // colour
  private ArrayList<Cell> covering;

  public Circle(Board b){//double x, double y, double r){
    board = b;
    radius = 7.0 + Math.random()*20;
    rx = Math.random()*1000;
    ry = Math.random()*500;
    while (!board.getGrid().moveable(this,(int)rx,(int)ry)){
      radius = 7.0 + Math.random()*20;
      rx = Math.random()*1000;
      ry = Math.random()*500;
    }
    vx = 0;
    vy = 0; 
    inc = 1;
    tol = radius/2;
    v = 600/(radius/5);
    colour = new Color((int)(Math.random()*255),
                        (int)(Math.random()*255),
                        (int)(Math.random()*255));
    covering = new ArrayList<Cell>();
  }
  
  public double getX() { return rx; }
  public double getY() { return ry; }
  public double getRadius() { return radius; }

  protected void setRadius(double r) { radius = r; }

  public boolean consume(Circle c){
    int bSize = (int) c.radius;
    int ballSize = (int) radius;
    boolean bigger = ballSize > bSize;
    if ( bigger ){
      add(c);
    } else {
      c.add(this);
    }
    return bigger;
  }

  public void add(Circle c){
    double oldA = PI*radius*radius;
    double consumedA = PI*c.radius*c.radius;
    radius = Math.sqrt((oldA + consumedA)/PI);
  }  
  
  protected void uncover(){
    for (int i = 0; i < covering.size(); ++i){
      covering.get(i).uncover();
    }
    covering.clear();
  }
  
  protected void cover(){
    Grid g = board.getGrid();
    g.cover(this,true);
  }

  public void addCoveredCell(Cell c){
    covering.add(c);
  }

  protected void space(int x, int y){
    Grid g = board.getGrid();
    g.moveable(this,x,y);
  }

  public void draw(Graphics g){
    g.setColor(colour);
    g.fillOval((int)(rx-radius),(int)(ry-radius),(int)radius*2,(int)radius*2);
    g.setColor(Color.gray);
  }
  
  //public abstract void move(Point p,int dir);
  public void move(Point p,int dir){
    if ( p != null ){
      uncover();
      double x = p.getX();
      double y = p.getY();
      double dx = rx - x;
      double dy = ry - y;
      double distance = Math.sqrt(dx*dx + dy*dy);
      double angle = Math.atan((y-ry)/(x-rx));
      if ( ! Double.isNaN(angle) ){
        if ( distance < v/radius ){
          vx = distance * Math.cos(angle) *dir;
          vy = distance * Math.sin(angle) *dir; 
        }
        else {
          vx = v/radius * Math.cos(angle) *dir;
          vy = v/radius * Math.sin(angle) *dir;
        }
      }
      // adjust direction based on cast rule
      if ( y - ry > 0 && x - rx > 0 ){
      }
      else if ( y - ry > 0 && x - rx < 0 ){
        vx = -vx;
        vy = -vy;
      }
      else if ( y - ry < 0 && x - rx > 0 ){
      }
      else if ( y - ry < 0 && x - rx < 0 ){
        vx = -vx;
        vy = -vy;
      }
      
      // if the shortest path is clear
      //if ( board.getGrid().moveable(this,(int)(vx+rx),(int)(vy+ry)) ){
        // alter x position
        rx = rx + vx;

        // alter y position
        ry = ry + vy;
      // if not, find path around barrier
       // }
/*      else { 
        Cell begin = board.getGrid().getCloseCell((int)rx,(int)ry);
        Cell end = board.getGrid().getCloseCell(p.getX(),p.getY());
        if ( begin.getX() != end.getX() || begin.getY() != end.getY() ){
          Search path = new Search(begin,end,radius,v,board.getGrid());
          Cell moveTo = path.getDestination();
          if ( prev != moveTo ){
            prev = begin;
            rx = moveTo.getX();
            ry = moveTo.getY();
          }
        }
      }
*/
      cover();
    }
  }

  public final boolean isTouching(Circle that){
    double dx = that.rx - rx;
    double dy = that.ry - ry;
    double dist = that.radius + radius;
    return (Math.hypot(dx,dy) < dist);
  }
}
