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

public class Barrier extends Circle {
  private Point goal;

  // constructor
  public Barrier(Board b,int ballSize){
    super(b);
    double updateRad = ballSize + Math.random()*50 - 25;
    while ( updateRad < 3 ){
      updateRad = ballSize + Math.random()*40 - 25;
    }
    setRadius(updateRad); 
    cover();
    v /=2;
    goal = null;
  }

  public void move(){
    // if there is no goal, make one
    if ( goal == null ){
      goal = board.getGrid().getRandomPoint(); 
    }
    // call super class move method
    super.move(goal,1);
    // if the barrier is close to the goal, set goal to null
    if ( Math.abs(rx - goal.getX()) < v &&
         Math.abs(ry - goal.getY()) < v ){
            goal = null;
    }
  }
}
