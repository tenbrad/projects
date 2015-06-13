
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

// class to perform A* searches
public class Search{
  
  // static fields to simplify search
  private static ArrayList<Cell> openList = new ArrayList<Cell>();    // list of cells to be searched
  private static ArrayList<Cell> closeList = new ArrayList<Cell>();   // list of searched cells
  private static Map<Cell,Search> searches = new HashMap<Cell,Search>();  // map to associate cells with their search
  private static Search best;               // the cell which is closest to the end cell
  private static int closest;               // distance the best cell is from the end
  private static boolean met;               // boolean for whether the end has been met
  private static Grid grid;                 // the grid for the current game for finding cells
  private static Cell end;                  // Cell the search is attempting to reach
  private static int r;                     // radius of circle being moved
  private static int speed;                        // speed of circle being moved
  
  // private fields
  private Cell curr;                        // Cell the searach is at
  private Cell parent;                      // Cell that came before this cell
  private int actualDistance;               // 
  private int nextNode;
  private int numRoutes;

  // base constructor, sets static fields
  public Search(Cell begin, Cell end, double radius, double speed, Grid g){
    // set static fields for this search
    openList.clear();
    closeList.clear();
    searches.clear();
    met = false;
    closest = -1;
    best = null;
    this.end = end;
    this.speed = (int)speed /2;
    r = (int)radius;
    // set non-static fields for this search element
    this.curr = begin;
    curr.setCost(0);
    calculate();
    parent = begin;
    grid = g;
    addToOpen(begin);
    searches.put(begin,this);
    
    // and the neighbours to the openList
    addRoutes();
    
    // perform a search until the open list is empty or the end is met
    while ( openList.size() != 0 && !met ){
      search();
    }

    // if there is no best move, use the current location
    if ( best == null) {
      best = this;
    }
    // reset all cells for next search
    for (int i = 0; i < openList.size() ; ++i){
      openList.get(i).reset();
    }
    for (int i = 0; i < closeList.size() ; ++i){
      closeList.get(i).reset();
    }
  }
  
  // subsequent search constructors
  public Search(Search s, Cell p){
    parent = s.curr;
    curr = p;
    addToOpen(p);
    searches.put(p,this);
    int cost = s.curr.getCost();
    
    // determine cost from previous
    int prev = (int)(Math.abs(curr.getX() - parent.getX()) +
                  Math.abs(curr.getY() - parent.getY()))/grid.getGridSize();
    if ( prev == 1 ) p.setCost(cost + 10);
    else if ( prev == 2 ) p.setCost(cost + 14);
    calculate();
  }

  // gettors
  public Cell getDestination() { return best.curr; }
  private boolean isOpen(Cell c) { return openList.contains(c); }
  private boolean isClosed(Cell c) { return closeList.contains(c); }

  // sets the estimated cost based on Manhattan distance
  private void calculate(){
    curr.setEstimate((int)(Math.abs(curr.getX() - end.getX()) +
                  Math.abs(curr.getY() - end.getY()))*10);
  }

  // recalculates the cost of Cell c based on the distance using this Search
  private void recalculate(Cell c){
    // count cells between c and curr
    int prev = (int) (Math.abs(c.getX() - curr.getX()) +
                      Math.abs(c.getY() - curr.getY()))/grid.getGridSize();
    // add cost of curr and the cost to this cell
    int temp = curr.getCost();
    if ( prev == 1 ) temp += 10;
    else if ( prev == 2 ) temp += 14;
    // if the new cost is lower than c's current cost, update it
    if ( c.getCost() > temp ){
      c.setCost(temp);
      Search s = searches.get(c);
      s.parent = curr;
      // remove and add c to the openList
      openList.remove(c);
      addToOpen(c);
    }
  }
  
  // updates the lists of Cells with the neighbours of the current search
  private void addRoutes(){
    numRoutes = curr.getNumNeighbours();
    for ( int i = 0; i < numRoutes; ++i){
      Cell next = curr.getNeighbour(i);
      // if the neighbour is on the closed list, it has been searched
      if ( isClosed(next) ){
        //do nothing
      }
      // if it's on the open list, recalculate it's cost from this cell
      else if ( isOpen(next) ){
        recalculate(next);
      // else the cell is new to the search algorithm, add it
      } else {
        Search nexti = new Search(this, next);
      }
    }
  }
  
  // adds c to the openList, stores cells from lowest to highest cost
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

  // performs one search iteration
  private void search(){
    if ( openList.size() > 0 ) {

      // get first element on openList, put on closedList
      Cell current = openList.get(0);
      openList.remove(current);
      closeList.add(current);
      Search s = searches.get(current);
     
      // check if there is space, null is returned if there is space 
      Circle space = current.isSpace(current.getX(),current.getY(),r);
      current.uncheck(current.getX(),current.getY(),r);
      // check only if tehre is space to move to
      if ( space == null ){
        // if the end has been reached, mark it
        if ( current.getX() == end.getX() && current.getY() == end.getY() ) {
          best = s;
          met = true;
          closest = 0;
        // if this search is within the distance the cell can move
        } else if ( speed > current.getCost() ){
          // add neighbours to the search
          s.addRoutes();
          // if this cell is the closest, mark it
          if ( closest == -1 || current.getEstimate() < closest ){
            closest = current.getEstimate();
            best = s;
          }
        }
      }
    }
  }
}
