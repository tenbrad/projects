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

public class Barrier extends Circle {
  private Point goal;
  // constructor

  public Barrier(Board b){
    super(b);
    setRadius(5.0 + Math.random()*25); 
    cover();
    v /=2;
    goal = null;
  }
  
/*  public void draw(Graphics g){
    g.setColor(Color.red);
    g.fillOval((int)(rx-radius),(int)(ry-radius),(int)radius*2,(int)radius*2);
  }*/

  public void move(){
    if ( goal == null ){
      goal = board.getGrid().getRandomPoint(); 
//      goal = new Point(temp.getX(), temp.getY());
    }
    super.move(goal,1);
    if ( Math.abs(rx - goal.getX()) < v/2 && Math.abs(ry - goal.getY()) < v/2 ){
      goal = null;
    }
  }
}
