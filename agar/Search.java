
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class Search{
  private static ArrayList<Cell> openList = new ArrayList<Cell>();
  private static ArrayList<Cell> closeList = new ArrayList<Cell>();
  private static Map<Cell,Search> searches = new HashMap<Cell,Search>();
  private static Search moveTo;
  private static Search best;
  private static boolean met;
  private static int closest;
  private Cell curr;
  private Cell end;
  private Cell parent;
  private Grid grid;
  private int r;
  private int speed;
  private int actualDistance;
  private int nextNode;
  private int numRoutes;

  public Search(Cell begin, Cell end, double radius, double speed, Grid g){
    openList.clear();
    closeList.clear();
    searches.clear();
    r = (int)radius;
    this.curr = begin;
    this.end = end;
    this.speed = (int)speed /2;
    curr.setCost(0);
    calculate();
    parent = begin;
    grid = g;
    addToOpen(begin);
    searches.put(begin,this);
    met = false;
    closest = -1;
    best = null;
    moveTo = null;
    
    addRoutes();

    while ( openList.size() != 0 && !met ){
      search();
    }

    if ( moveTo == null && best == null) {
      moveTo = this;
    } else if ( moveTo == null ){
      moveTo = best;
    }
  }
  
  public Search(Search s, Cell p){
    parent = s.curr;
    r = s.r;
    end = s.end;
    curr = p;
    speed = s.speed;
    addToOpen(p);
    searches.put(p,this);
    grid = s.grid;
    int cost = s.curr.getCost();
    
    // determine cost from previous
    int prev = (int)(Math.abs(curr.getX() - parent.getX()) +
                  Math.abs(curr.getY() - parent.getY()))/grid.getGridSize();
    if ( prev == 1 ) p.setCost(cost + 10);
    else if ( prev == 2 ) p.setCost(cost + 14);
    //  else System.out.println("Error in Search");
    calculate();
  }

  public Cell getDestination() { return moveTo.curr; }

  private void calculate(){
    curr.setEstimate((int)(Math.abs(curr.getX() - end.getX()) +
                  Math.abs(curr.getY() - end.getY()))*10);
  }

  private void recalculate(Cell c){
    int prev = (int) (Math.abs(c.getX() - curr.getX()) +
                      Math.abs(c.getY() - curr.getY()))/grid.getGridSize();
    int temp = curr.getCost();
    if ( prev == 1 ) temp += 10;
    else if ( prev == 2 ) temp += 14;
    if ( c.getCost() > temp ){
      c.setCost(temp);
      Search s = searches.get(c);
      s.parent = curr;
      openList.remove(c);
      addToOpen(c);
    }
  }
  
  private boolean isOpen(Cell c) { return openList.contains(c); }
  private boolean isClosed(Cell c) { return closeList.contains(c); }

  private void addRoutes(){
    numRoutes = curr.getNumNeighbours();
    for ( int i = 0; i < numRoutes; ++i){
      Cell next = curr.getNeighbour(i);
      if ( isClosed(next) ){
        //do nothing
      }
      else if ( isOpen(next) ){
        // calculate
        recalculate(next);
      } else {
        Search nexti = new Search(this, next);
      }
    }
  }

  private void addToOpen(Cell c){
    int num = c.getTotal();
    int j = openList.size();
    if ( j - 1 > 0 ){
      --j;
      int value = openList.get(j).getTotal();
      while ( j > 0 && value > num ){
        --j;
        value = openList.get(j).getTotal();
      }
    }
    openList.add(j,c);
  }

  private void search(){
    if ( openList.size() > 0 ) {

      // get first element on openList, put on closedList
      Cell current = openList.get(0);
      openList.remove(current);
      closeList.add(current);
      Search s = searches.get(current);
      
      boolean space = current.isSpace(current.getX(),current.getY(),r);
      current.uncheck(current.getX(),current.getY(),r);
      if ( space ){
        // if the end has been reached, mark it
        if ( current.getX() == end.getX() && current.getY() == end.getY() ) {
          moveTo = s;
          met = true;
          closest = 0;
        // if max distance have been met and this node is closest to the end, it's the best
        } else if ( speed > current.getCost() ){
          s.addRoutes();
          if ( closest == -1 || current.getEstimate() < closest ){
            closest = current.getEstimate();
            best = s;
          }
        }
      }
    }
  }
}
