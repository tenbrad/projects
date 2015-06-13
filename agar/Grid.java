import java.awt.Point;
import java.util.ArrayList;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;

// class to control the area the balls are on
//  specically used for determining collisions easily
public class Grid{
  // fields
  private Cell points[][];
  private int gridSize;
  private int lat, lon;

  // constructor
  Grid(int width, int height){
    gridSize = 4;
    lat = height/gridSize + 1;
    lon = width/gridSize + 1;
    points = new Cell[lon][lat];
    for (int i = 0; i < lon; ++i){
      for (int j = 0; j < lat; ++j){
        points[i][j] = new Cell(i*gridSize,j*gridSize);
      }
    }
    
    // add neighbours for observer pattern
    for (int i = 0; i < lon; ++i){
      for (int j = 0; j < lat; ++j){
        Cell c = points[i][j];
        if (i > 0) c.addNeighbour(points[i-1][j]);
        if (i > 0 && j + 1 < lat) c.addNeighbour(points[i-1][j+1]);
        if (j + 1 < lat) c.addNeighbour(points[i][j+1]);
        if (i + 1 < lon && j + 1 < lat) c.addNeighbour(points[i+1][j+1]);
        if (i + 1 < lon) c.addNeighbour(points[i+1][j]);
        if (i + 1 < lon && j > 0) c.addNeighbour(points[i+1][j-1]);
        if (j > 0) c.addNeighbour(points[i][j-1]);
        if (i > 0 && j > 0) c.addNeighbour(points[i-1][j-1]);
      }
    }
  }

  // getters/setters/checkers
  public boolean isCovered(int x, int y){
    return points[x][y].isCovered();
  }

  public Point getPoint(int x, int y) {
    Point p = new Point(x*gridSize,y*gridSize);
    return p;
  }

  public int getGridSize() { return gridSize; }

  // method to return a random point on the grid
  public Point getRandomPoint(){
    Point p = null;
    while ( p == null ){
      int i =(int)(Math.random()*lon);
      int j =(int)(Math.random()*lat);
      p = getPoint(i,j);
    }
    return p;
  }
  
  // method to return a cell which is close to the postion requested
  public Cell getCloseCell(double x, double y){
    int cx = (int)(x/gridSize);
    int cy = (int)(y/gridSize);
    return points[cx][cy];
  }

  // check to see if there is space for c at location (x,y)
  //   if there is, return null
  //   otherwise, return the Circle blocking the move
  public Circle moveable(Circle c,int x, int y){
    int cx = (int)(c.getX());
    int cy = (int)(c.getY());
    int cr = (int) c.getRadius();
    Circle temp = points[x/gridSize][y/gridSize].isSpace(x,y,cr);
    points[x/gridSize][y/gridSize].uncheck(x,y,cr);
    return temp;
  }

  // cover or uncover the space covered by Circle c
  public void cover(Circle c, boolean covering){
    int cx = (int)(c.getX());
    int cy = (int)(c.getY());
    int cr = (int) c.getRadius();
    points[cx/gridSize][cy/gridSize].cover(covering,c,cx,cy,cr);
  }

  // the grid drawn with points to represent cells
  //    red cells are covered, black cells are not covered
  public void draw(Graphics g){
    for (int i = 0; i < lon; i += 3){
      for (int j = 0; j < lat; j += 3){
        if ( points[i][j].isCovered() )
          g.setColor(Color.red);
        else g.setColor(Color.black);
        g.fillOval(i*gridSize,j*gridSize,3,3);
      }
    }
    g.setColor(Color.gray);
  }
}
