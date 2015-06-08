import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import javax.swing.Action;
import javax.swing.KeyStroke;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

// the ball class runs as the ball controlled by the player mouse
public class Ball extends Circle {
  static private ArrayList<Ball> group; // balls in the same group
  // basic constructor
  public Ball(Board b){
    super(b);
    cover();
    radius = 25;
    group = new ArrayList<Ball>();
  }
  
  // constructor meant for splitting ball
  public Ball(Board b, Ball ball){
    super(b, ball);
    cover();
    group = new ArrayList<Ball>();
  }

  // method to move the ball on the screen
  public void move(){
    // Find mouse position
    Point p = board.getMousePosition();
    if ( p != null ){
      // uncover grid of ball
      uncover();
      changeDir(p,v,1);
      Circle present = board.getGrid().moveable(this,(int)(rx+vx),(int)(ry+vy));
      // Ball is over the mouse
      if ( Math.sqrt(vx*vx + vy*vy) < radius/5 ){
        // do nothing 
      }
      // The ball will move into empty space or a ball which is not part of itself
      else if ( present == null || !group.contains(present) ){
        rx += vx;
        ry += vy;
      }
      // ball needs to avoid obstacle
      else {
        moveAround(p);
      }
      // recover grid
      cover();
    }
    
  }
}
