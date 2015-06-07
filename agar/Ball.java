//import StdDraw.java;

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
//import javax.swing.*;
public class Ball extends Circle{
  private Cell prev;
  // constructor
  public Ball(Board b){
    super(b);
    cover();
  }

  public void move(Point p,int dir){
    super.move(p,dir);
    /*
    if ( p != null ){
      uncover();
      double x = p.getX();
      double y = p.getY();
      double angle = Math.atan((y-ry)/(x-rx));
      if ( ! Double.isNaN(angle) ) {
        vx = v/radius * Math.cos(angle) *dir;
        vy = v/radius * Math.sin(angle) *dir;
        
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
      }
      
      // if the shortest path is clear
      if ( board.getGrid().moveable(this,(int)vx,(int)vy) ){
        // alter x position
        rx = rx + vx;

        // alter y position
        ry = ry + vy;
      // if not, find path around barrier
      } else { 
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
      cover();
    }
    */
  }
  
  public void unmove(){
    rx -= vx;
    ry -= vy;
  }

/*  public void draw(Graphics g){
    g.setColor(Color.gray);
    g.fillOval((int)(rx-radius),(int)(ry-radius),(int)radius*2,(int)radius*2);
  }
*/
}
