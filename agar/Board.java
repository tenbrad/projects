import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import javax.swing.Timer;
import javax.swing.JOptionPane;
import javax.swing.JFrame;
import javax.swing.Action;
import javax.swing.KeyStroke;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.JPanel;

public class Board extends JPanel implements ActionListener {
    // board fields
    private final int DELAY = 30;
    private ArrayList<Ball> balls;
    private ArrayList<Barrier> wall;
    private Timer timer;
    private int numBalls, numBarriers;
    private int dir;
    private Grid grid;
    private boolean gameover;
    private int width,height;
    private int ballSize;
    
    // getters and setters
    public Grid getGrid(){ return grid; }

    public boolean gameOver() { return gameover; }

    private boolean isWall(Circle c){
      return wall.contains(c);
    }
    
    private boolean isPlayer(Circle c){
      return balls.contains(c);
    }


    // constructor
    public Board(int width, int height){
      this.width = width;
      this.height = height;
      init();
    }
    
    // initialization method - to be used when board is made and
    //   when restarting the board
    private void init(){
      gameover = false;
      grid = new Grid(width,height);
      numBalls = 1;
      numBarriers = 5 + (int) (Math.random()*5);
      dir = 1;
      wall = new ArrayList<Barrier>();
      balls = new ArrayList<Ball>();
      for (int i = 0; i < numBalls; ++i){
        balls.add(new Ball(this));
      }
      updateBallSize();
      for (int i = 0; i < numBarriers; ++i){
        wall.add(new Barrier(this,ballSize));
      }
      initBoard();
    }

    // add barrier and update barrier count
    private void addBarriers(){
      wall.add(new Barrier(this,ballSize));
      ++numBarriers;
    }

    // add ball and update number of balls and total ball size
    private void addBalls(){
      balls.add(new Ball(this));
      ++numBalls;
      updateBallSize();
    }

    private void updateBallSize(){
      ballSize = 0;
      for (int i = 0; i < numBalls; ++i ){
        ballSize += balls.get(i).getRadius();
      }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < numBalls; ++i){
          balls.get(i).draw(g);
        }
        for (int i = 0; i < numBarriers; ++i){
          wall.get(i).draw(g);
        }
       //grid.draw(g);
    }

    
    // method to check if any balls need to consumed and keep counts accurate
    private void checkConsumed(){
      boolean ballConsumed = false;
      // check if any player balls are touching a barrier
      for (int i = 0; i < numBalls; ++i){
        Ball player = balls.get(i);
        for (int j = 0; j < numBarriers; ++j){
          Barrier curr = wall.get(j);
          if ( player.isTouching(curr) ){
            boolean consumed = consume(player,curr);
            if ( consumed ){
              j -= 1;   // since there is now one less barrier
              ballConsumed = true;
            } else {
              ballConsumed = true;
              break;
            }
          }
        }
      }
      if (ballConsumed){
        updateBallSize();
      }
      for (int i = 0; i < numBarriers; ++i){
        Barrier wall1 = wall.get(i);
        for (int j = i + 1; j < numBarriers; ++j){
          Barrier wall2 = wall.get(j);
          if ( wall1.isTouching(wall2) ){
            boolean consumed = consume(wall1,wall2);
            // if wall1 consumed, break inner loop
            if ( consumed ){
              break;
            }
            // otherwise, decrease index by one since one less ball exists
            else {
              j -= 1;
            }
          }
        }
      }
    }

    // method to run consumption when two balls touch
    private boolean consume(Circle c, Circle d){
      boolean consumed = c.consume(d);
      Circle ballConsumed;
      if ( consumed ) ballConsumed = d;
      else ballConsumed = c;
      if ( isWall(ballConsumed) ){
        wall.remove(ballConsumed);
        ballConsumed.uncover();
        --numBarriers;
      } else {
        --numBalls;
        balls.remove(ballConsumed);
        ballConsumed.uncover();
        if ( numBalls == 0 ){
          gameover = true;
        }
      }
      return consumed;
    }


    @Override
    public void actionPerformed(ActionEvent e){
      // move all balls
      for (int i = 0; i < numBalls; ++i){
        balls.get(i).move();
      }
      // possibly add a barrier
      if ( (int)(Math.random()*100) == 0 ) {
        addBarriers();
      }
      // if add a barrier is there are none
      if ( numBarriers == 0 ){
        addBarriers();
      }
      // move all barriers
      for (int i = 0; i < numBarriers; ++i){
        wall.get(i).move();
      }
      // check if any ball must be consumed
      checkConsumed();
      // redraw the board
      repaint();
      // if the player has no balls, give game over message
      if ( gameover ){
        gameOverMessage();
      }
    }

    private void gameOverMessage(){
      timer.stop();
      int n = JOptionPane.showConfirmDialog(this,"GAME OVER\nWould you like to play again?","GAME OVER",JOptionPane.YES_NO_OPTION);
      if ( n == 0 ){
        init();
      } else {
        JFrame frame = (JFrame) getTopLevelAncestor();
        frame.dispose();
      }
    }
    
    private void restartMessage(){
      timer.stop();
      int n = JOptionPane.showConfirmDialog(this,"Pause\nWould you like to restart the game?","PAUSE",JOptionPane.YES_NO_OPTION);
      if ( n == 0 ){
        init();
      }
      timer.start();
    }
    
    // control the splitting of a ball
    private void playerSplit(){
      ArrayList<Ball> temp = new ArrayList<Ball>();
      for (int i = 0; i < balls.size() ; ++i){
        balls.get(i).uncover();
        temp.add(new Ball(this,balls.get(i)));
        temp.add(new Ball(this,balls.get(i)));
      }
      balls = temp;
      numBalls = balls.size();
    }
    
    // method to initialize listeners and timer
    private void initBoard(){
      timer = new Timer(DELAY, this);
      timer.start();
      getInputMap().put(KeyStroke.getKeyStroke("SPACE"),"split");
      getActionMap().put("split",new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playerSplit();
            }
        });
      getInputMap().put(KeyStroke.getKeyStroke('r'),"restart");
      getActionMap().put("restart",new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
              restartMessage();      
            }
        });
    }
}
