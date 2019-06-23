public class Square {
  private boolean isBomb;
  private boolean isMarked;
  private boolean isRevealed;
  private int bombsNear;
  //int x, y; //for implemention of GUI
  public Square() {
    this.isBomb = this.isMarked = this.isRevealed = false;
    this.bombsNear = 0;
  }
  public String toString() {
    //ANSI colours allow for the text colour of the console to be changed
    final String ANSI_bright_red = "\u001b[31;1m";
    final String ANSI_bright_yellow = "\u001b[33;1m";
    final String ANSI_reset = "\u001b[0m";
    String[] numColours = {"\u001b[37m", "\u001B[34m", "\u001B[32m", "\u001b[31m", "\u001B[35m", "\u001B[33m", "\u001b[34;1m", "\u001b[30;1m", "\u001B[36m"}; //blue, green, red, purple, yellow, bright blue, gray, cyan (colour scheme mostly based off of classic minesweeper)
    String output = "•";
    if (this.isRevealed) {
      if (this.isBomb)
        output = ANSI_bright_red + "B" + ANSI_reset;
      else {
        output = numColours[this.bombsNear] + Integer.toString(this.bombsNear) + ANSI_reset;
      }
    }
    else if (this.isMarked) {
      output = ANSI_bright_yellow + "M" + ANSI_reset; 
    }
    return output;
  }
  //Accessor methods
  public boolean getBomb() {
    return this.isBomb;
  }
  public boolean getMarked() {
    return this.isMarked;
  }
  public boolean getRevealed() {
    return this.isRevealed;
  }
  public int getNumber () {
    return this.bombsNear;
  }
  //Mutator methods
  public void putBomb() {
    this.isBomb = true;
  }
  public void mark() {
    this.isMarked = true;
  }
  public void unmark() {
    this.isMarked = false;
  }
  public void reveal() {
    this.isRevealed = true;
  }
  public void setNumber(int n) {
    this.bombsNear = n;
  }
}
