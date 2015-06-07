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
    private final int DELAY = 30;
    private ArrayList<Ball> balls;
    private ArrayList<Barrier> wall;
    private Timer timer;
    private int numBalls, numBarriers;
    private int dir;
    private Grid grid;
    private boolean gameover;
    private int width,height;
    
    public Grid getGrid(){ return grid;}

    public Board(int width, int height){
      this.width = width;
      this.height = height;
      init();
    }
    
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
      for (int i = 0; i < numBarriers; ++i){
        wall.add(new Barrier(this));
      }
      initBoard();
    }

    private void addBarriers(){
      wall.add(new Barrier(this));
      ++numBarriers;
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

    private void initBoard(){
      timer = new Timer(DELAY, this);
      timer.start();
      getInputMap().put(KeyStroke.getKeyStroke("SPACE"),"split");
      getActionMap().put("split",new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dir = -dir;
            }
        });
      getInputMap().put(KeyStroke.getKeyStroke('r'),"restart");
      getActionMap().put("restart",new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
              timer.stop();
              int n = JOptionPane.showConfirmDialog(null,"RESTART?\nAre you sure you want to restart?","Restart",JOptionPane.YES_NO_OPTION);
              if ( n == 0 ){
                init();
              }
              timer.start();           
            }
        });
    }
    

    private void checkConsumed(){
      for (int i = 0; i < numBalls; ++i){
        Ball player = balls.get(i);
        for (int j = 0; j < numBarriers; ++j){
          Barrier curr = wall.get(j);
          if ( player.isTouching(curr) ){
            boolean consumed = consume(player,curr);
            if ( consumed ){
              j -= 1;   // since there is now one less barrier
            } else {
              break;
            }
          }
        }
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
        gameover = true;
        if ( numBalls == 0 ){
          gameover = true;
        }
      }
      return consumed;
    }

    private boolean isWall(Circle c){
      return wall.contains(c);
    }
    
    private boolean isPlayer(Circle c){
      return balls.contains(c);
    }

    @Override
    public void actionPerformed(ActionEvent e){
      Point p = getMousePosition();
      for (int i = 0; i < numBalls; ++i){
        balls.get(i).move(p,dir);
      }
      if ( (int)(Math.random()*100) == 0 ) {
        addBarriers();
      }
      if ( numBarriers == 0 ){
        addBarriers();
      }
      for (int i = 0; i < numBarriers; ++i){
        wall.get(i).move();
      }
      checkConsumed();
      repaint();
      if ( gameover ){
        System.out.println("GAME OVER");
        timer.stop();
        int n = JOptionPane.showConfirmDialog(this,"GAME OVER\nWould you like to play again?","GAME OVER",JOptionPane.YES_NO_OPTION);
        if ( n == 0 ){
          init();
        } else {
          JFrame frame = (JFrame) getTopLevelAncestor();
          frame.dispose();
        }
      }
    }
    
    public boolean gameOver() { return gameover; }
}
