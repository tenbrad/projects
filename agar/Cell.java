public class Cell{
  private int x, y;  // position
  private boolean cover;
  private Cell neighbours[];
  private int numNeighbours;
  private boolean checked;
  private int total;
  private int cost;
  private int estimate;
  private int actual;
  private Circle covering;

  public Cell(int x, int y){
    this.x = x;
    this.y = y;
    neighbours = new Cell[8];
    numNeighbours = 0;
    cover = false;
    checked = false;
    
  }

  public void addNeighbour(Cell n){
    neighbours[numNeighbours++] = n;
  }
  
  public int getTotal() { return total; }
  public int getCost()  { return cost; }
  public int getEstimate() { return estimate; }
  public int getActual() { return actual; }
  
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
  
  public int getX() { return x; }
  public int getY() { return y; }
  public void setCover(boolean cover) { this.cover = cover; }
  public boolean isCovered() { return cover; }
  public Cell getNeighbour(int i) { return neighbours[i]; }
  public int getNumNeighbours() { return numNeighbours; }

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

  public boolean isNeighbour(Cell c){
    for (int i = 0; i < numNeighbours; ++i){
      if ( c == neighbours[i] ) return true;
    }
    return false;
  }
  public int distance(Cell c){
    int dx = c.x-x;
    int dy = c.y-y;
    return (int) Math.sqrt(dx*dx + dy*dy);
  }
}
