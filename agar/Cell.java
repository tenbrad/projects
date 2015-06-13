// class to work of the points of a grid
//  handles neighbours and used for determining paths to travel

public class Cell{
  // private fields of cell
  private int x, y;           // position
  private boolean cover;      // true if covered
  private Cell neighbours[];  // list of neighbouring cells
  private int numNeighbours;  // number f neighbours
  private boolean checked;    // true if cell has been checked
  private int total;          // total cost of travelling through this cell
  private int cost;           // cost so far for travel
  private int estimate;       // estimate of remaining travel based on distance
  private int actual;         // actual cost for this cell
  private Circle covering;    // Circle which is covering this cell

  // constructor
  public Cell(int x, int y){
    this.x = x;
    this.y = y;
    neighbours = new Cell[8];
    numNeighbours = 0;
    cover = false;
    checked = false;
    
  }

  // method to add a neighbour of this cell
  public void addNeighbour(Cell n){
    neighbours[numNeighbours++] = n;
  }
  
  // gettor methods
  public int getTotal() { return total; }
  public int getCost()  { return cost; }
  public int getEstimate() { return estimate; }
  public int getActual() { return actual; }
  public int getX() { return x; }
  public int getY() { return y; }
  public Cell getNeighbour(int i) { return neighbours[i]; }
  public int getNumNeighbours() { return numNeighbours; }
  
  // settor methods
  public void setTotal(int t) { total = t; }
  public void setCost(int c)  {
    cost = c;
    total = cost + estimate;
  }
  public void setEstimate(int e) {
    estimate = e;
    total = cost + estimate;
  }
  public void setActual(int a) { actual = a; } 
  public void setCover(boolean cover) { this.cover = cover; }
  public boolean isCovered() { return cover; }

  // checks if there is space for a circle with radius cr at (cx,cy)
  //    returns null if there is space
  //    returns the circle covering otherwise 
  public Circle isSpace(int cx, int cy, int cr){
    double distance = Math.sqrt((cx-x)*(cx-x) + (cy-y)*(cy-y));
    if (distance > cr +1) return null;
    if (cover) return covering;
    Circle c = null;
    checked = true;
    for (int i = 0; i < numNeighbours && c == null ; ++i){
      if ( !neighbours[i].checked )
        c = neighbours[i].isSpace(cx,cy,cr);
    }
    return c;
  }

  // unchecks checks covered within the given space
  public void uncheck(int cx,int cy,int cr){
    checked = false;
    for (int i = 0; i < numNeighbours; ++i){
      if ( neighbours[i].checked ){
        neighbours[i].uncheck(cx,cy,cr);
      }
    }
  }

  public void uncover(){
    cover = false;
    covering = null;
  }
  
  public void cover(boolean covering, Circle c, int cx, int cy, int cr){
    double distance = Math.sqrt((cx-x)*(cx-x) + (cy-y)*(cy-y));
    if (distance < cr + 1 && (c == this.covering || this.covering == null )){
      cover = covering;
      if ( cover ) {
        this.covering = c;
        c.addCoveredCell(this);
      }
      else this.covering = null;
      for (int i = 0; i < numNeighbours ; ++i){
        if ( neighbours[i].cover != covering ){
          neighbours[i].cover(covering,c,cx,cy,cr);
        }
      }
    }
  }

  // checks if c is a neighbour of this
  public boolean isNeighbour(Cell c){
    for (int i = 0; i < numNeighbours; ++i){
      if ( c == neighbours[i] ) return true;
    }
    return false;
  }

  // calculates the distance between two cells
  public int distance(Cell c){
    int dx = c.x-x;
    int dy = c.y-y;
    return (int) Math.sqrt(dx*dx + dy*dy);
  }
}
