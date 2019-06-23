import java.util.*;

public class Main {
  static Scanner in = new Scanner(System.in);
  public static void main(String[] args) {
    int wins = 0;
    int losses = 0;
    boolean gameLost;
    int[] boardDim;
    int bombTotal;
    Square[][] board;
    System.out.println("------------------------");
    System.out.println("Welcome to DC\'s Minesweeper!");
    askRules();
    System.out.println("------------------------");
    boardDim = askDimensions();
    do {
      System.out.println("-----");
      bombTotal = askBombTotal(boardDim[0], boardDim[1]);
      board = createBoard(boardDim[0], boardDim[1], bombTotal);
      System.out.println("-----");
      System.out.println("Game Start!");
      System.out.println("NOTE: When inputting coordinates, keep in mind that the bottom left square is (1,1).");
      System.out.println();
      gameLost = playGame(board, bombTotal); 
      if (gameLost)
        losses++;
      else 
        wins++;
      System.out.println("Your Record:");
      System.out.println("W: " + wins + "\t L: " + losses);
      System.out.println();
    }
    while (replayInput());
    System.out.println();
    System.out.println("Thanks for playing!");
    System.out.println("Coming soon: DC\'s Minesweeper with a GUI!");
  }
  public static void askRules() {
    System.out.println("Would you like to read the rules? (Y/N)");
    char uChoice = Character.toUpperCase(in.next().charAt(0));
    if (uChoice == 'Y')
      displayRules();
    else if (uChoice != 'N') {
      System.out.println("That is not a valid choice. Please enter \'Y\'or \'N\'");
      askRules();
    }
  }
  public static void displayRules() {
    System.out.println("------------------------");
    System.out.println("Rules: ");
    System.out.println("There is a grid (minimum 7 x 7) of squares which are unrevealed. The number of mines hidden in the grid varies depending on the selected difficulty (also customizable).");
    System.out.println();
    System.out.println("The player may Reveal, Mark, or Unmark a square. If the revealed square does not contain a bomb, it will show a number indicating the number of mines around it in a 3 x 3 range. If the player reveals a bomb, then they lose the game.");
    System.out.println();
    System.out.println("If a \'0\' square is revealed - a square with 0 bombs around it - then all squares around it will be revealed until a number greater than 0 is encountered.");
    System.out.println();
    System.out.println("The goal of the game is to mark the locations of each and every single mine exactly without revealing any bombs. The win will not count if even a single bombless square is incorrectly marked. The player may unmark previously marked squares.");
    System.out.println();
    System.out.println("Legend:");
    System.out.println("•\t- indicates an unrevealed, unmarked square");
    System.out.println("M\t- indicates a marked square");
    System.out.println("B\t- indicates a bomb");
    System.out.println("Number\t- indicates the number of bombs around the square");
  }
  public static int[] askDimensions() {
    int[] dimensions = {10, 7};
    System.out.println("Would you like to customize the dimensions of the minefield (Default: 10 x 7)? (Y/N)");
    char uChoice = Character.toUpperCase(in.next().charAt(0));
    if (uChoice == 'Y') {
      System.out.println("What is your desired width? (7 to 22)");
      dimensions[0] = getIntput(7, 22);
      System.out.println("What is your desired height? (7 to 22)");
      dimensions[1] = getIntput(7, 22);
    }
    else if (uChoice != 'N') {
      System.out.println("That is not a valid choice. Please enter \'Y\'or \'N\'");
      return askDimensions();
    }
    return dimensions;
  }
  public static int getIntput(int min, int max) {
    Scanner scan = new Scanner(System.in); //have to use a new scanner here because otherwise it will bug out and there will be an infinite loop
    int n;
    try {
      n = scan.nextInt();
      if (n > max || n < min) {
        System.out.println("Please enter a number between " + min + " and " + max + "!");
        return getIntput(min, max);
      }
      else
        return n;
    }
    catch (InputMismatchException e) {
      System.out.println("Please enter an integer!");
      return getIntput(min, max);
    }
  }
  public static int askBombTotal(int width, int height) {
    char input;
    int bombTotal;
    int squareTotal = width * height;
    System.out.println("Please choose one of the following difficulties: ");
    System.out.println("Easy\t (8 Bombs)");
    System.out.println("Medium\t (12 Bombs)");
    System.out.println("Hard\t (16 Bombs)");
    System.out.println("Custom");
    input = Character.toUpperCase(in.next().charAt(0));
    if (input == 'E') {
      bombTotal = 8;
    }
    else if (input == 'M') {
      bombTotal = 12;
    }
    else if (input == 'H') {
      bombTotal = 16;
    }
    else if (input == 'C') {
      System.out.println("Please enter the number of bombs you want.");
      bombTotal = getIntput(1, squareTotal-1);
    }
    else {
      System.out.println("\"" + input + "\" is invalid input.");
      return askBombTotal(width, height);
    }
    return bombTotal;
  }
  public static Square[][] createBoard(int width, int height, int bombs) {
    Square[][] board = new Square[height][width];
    for(int i = 0; i < height; i++) { //each object in the 2D array must be initialized
       for(int j = 0; j < width; j++) {
         board[i][j] = new Square();
       }   
    }
    Square selected = new Square();
    //Plant all the bombs in random squares
    int counter = 0;
    while (counter < bombs) { 
      selected = board[rng(0, height-1)][rng(0, width-1)];
      if (!selected.getBomb()) {
        selected.putBomb();
        counter++;
      }
    }
    //Set all the numbers on the squares: used ternary conditional operators to convert boolean into int
    int sum = 0;
    for (int row = height-1; row >= 0; row--) {
      for (int col = 0; col < width; col++) {
        //Need these if statements to avoid ArrayIndexOutOfBounds Exception
        if (row == height-1) { //top side
          sum = ((board[row-1][col].getBomb()) ? 1 : 0);
          if (col != width-1) { //not top right square
          sum += ((board[row][col+1].getBomb()) ? 1 : 0);
          sum += ((board[row-1][col+1].getBomb()) ? 1 : 0);
          }
          if (col != 0) { //not top left square
            sum += (((board[row-1][col-1].getBomb()) ? 1 : 0) + ((board[row][col-1].getBomb()) ? 1 : 0));
          }
        }
        else if (col == width-1) { //right side
          sum = (((board[row][col-1].getBomb()) ? 1 : 0) + ((board[row+1][col-1].getBomb()) ? 1 : 0) + ((board[row+1][col].getBomb()) ? 1 : 0));
          if (row != 0) { //not bottom right square
            sum += (((board[row-1][col].getBomb()) ? 1 : 0) + ((board[row-1][col-1].getBomb()) ? 1 : 0));
          }
        }
        else if (row == 0) { //bottom side
          sum = (((board[row+1][col].getBomb()) ? 1 : 0) + ((board[row+1][col+1].getBomb()) ? 1 : 0) + ((board[row][col+1].getBomb()) ? 1 : 0));
          if (col != 0) { //not bottom left square
            sum += (((board[row][col-1].getBomb()) ? 1 : 0) + ((board[row+1][col-1].getBomb()) ? 1 : 0));
          }
        }
        else if (col == 0) { //left side
          sum = (((board[row+1][col].getBomb()) ? 1 : 0) + ((board[row+1][col+1].getBomb()) ? 1 : 0) + ((board[row][col+1].getBomb()) ? 1 : 0) + ((board[row-1][col+1].getBomb()) ? 1 : 0) + ((board[row-1][col].getBomb()) ? 1 : 0));
        }
        else { //square isn't on the edges
          sum = (((board[row+1][col].getBomb()) ? 1 : 0) + ((board[row+1][col+1].getBomb()) ? 1 : 0) + ((board[row][col+1].getBomb()) ? 1 : 0) + ((board[row-1][col+1].getBomb()) ? 1 : 0) + ((board[row-1][col].getBomb()) ? 1 : 0) + ((board[row-1][col-1].getBomb()) ? 1 : 0) + ((board[row][col-1].getBomb()) ? 1 : 0) + ((board[row+1][col-1].getBomb()) ? 1 : 0));
        }
        board[row][col].setNumber(sum);
        sum = 0;
      }
    }
    return board;
  }
  public static int rng(int min, int max) {
    int range = max - min;
    return (int)(Math.random()*(range+1)+min);
  }
  public static boolean playGame(Square[][] board, int bombTotal) {
    displayBoard(board);
    int x, y;
    char action;
    boolean isValid = true;
    boolean gameLost = false;
    int bombsMarked = 0;
    int wrongMarked = 0;
    do {
      //Square selection
      System.out.println("Select a square by entering its x-value and then y-value on separate lines.");
      x = getIntput(1, board[0].length) - 1;
      y = getIntput(1, board.length) - 1;
      //Action selection
      System.out.println("Please enter the action you wish to do. \'R\' for reveal, \'M\' for mark, and \'U\' for unmark. (\'Q\' to Quit)");
      action = Character.toUpperCase(in.next().charAt(0));
      if (action == 'R') { //Reveal
        if (board[y][x].getRevealed()) {
          System.out.println("The square at (" + (x+1) + ", " + (y+1) + ") is already revealed.");
          isValid = false;
        }
        else {
          board[y][x].reveal();
          //"0" squares and their surroundings should be automatically revealed - it would just be tedious for the player to manually check all the squares that are known to be safe
          if (board[y][x].getNumber() == 0) 
            revealZero(x, y, board);
        }
        //Lose condition
        if (board[y][x].getBomb()) {
          gameLost = true;
        }
      } 
      else if (action == 'M') { //Mark
        if (board[y][x].getRevealed()) {
          System.out.println("You cannot mark an already revealed square.");
          isValid = false;
        }
        else if (board[y][x].getMarked()) {
          System.out.println("The square at (" + (x+1) + ", " + (y+1) + ") is already marked.");
          isValid = false;
        }
        else {
          board[y][x].mark();
          if (board[y][x].getBomb()) 
            bombsMarked++;
          else 
            wrongMarked++;
        }
      }
      else if (action == 'U') { //Unmark
        if (board[y][x].getRevealed() || !board[y][x].getMarked()) {
          System.out.println("The square at (" + (x+1) + ", " + (y+1) + ") is not marked.");
          isValid = false;
        }
        else {
          board[y][x].unmark();
          if (board[y][x].getBomb()) 
            bombsMarked--;
          else
            wrongMarked--;
        }
      }
      else if (action != 'Q') { 
        System.out.println("That is not a valid action.");
        isValid = false;
      }
      if (isValid && action != 'Q') {
        System.out.println();
        displayBoard(board);
        System.out.println();
      }
      isValid = true;
    }
    while (!gameLost && !(bombsMarked == bombTotal && wrongMarked == 0) && action != 'Q');
    //Loop while the player hasn't lost, won, or quit.
    if (action == 'Q') {
      System.out.println("You have quit the game.");
      gameLost = true; 
    }
    else if (gameLost)
      System.out.println("You had revealed a bomb at (" + (x+1) + ", " + (y+1) + ") and lost!");
    else if (!gameLost)
      System.out.println("Congratulations! You have correctly marked all the bombs and won!");
    revealBoard(board);
    return gameLost;
  }
  public static void displayBoard(Square[][] board){
    final String ANSI_cyan_bg = "\u001B[46;1m";
    final String ANSI_black = "\u001B[40m";
    final String ANSI_reset = "\u001b[0m";
    for (int row = board.length-1; row >= 0; row--) {
      System.out.print(ANSI_cyan_bg + ANSI_black + (row + 1) + "\t" + ANSI_reset); //y-axis
      for (int col = 0; col < board[row].length; col++) {
        System.out.print(board[row][col] + "\t");
      }
      System.out.println();
      System.out.println();
    }
    //x-axis
    System.out.print("\t");
    for (int i = 1; i <= board[0].length; i++) {
      System.out.print(ANSI_cyan_bg + ANSI_black + i + "\t");
    }
    System.out.print(ANSI_reset);
    System.out.println();
  }
  public static void revealZero(int x, int y, Square[][] board) { //recursive method to reveal all squares around "0" squares
    //int[] of x_inc and y_inc check the square around the selected square in clockwise fashion
    int[] x_inc = {0, 1, 1, 1, 0, -1, -1, -1};
    int[] y_inc = {1, 1, 0, -1, -1, -1, 0, 1};
    for (int i = 0; i < 8; i++) {
      try {
        if (!board[y + y_inc[i]][x + x_inc[i]].getRevealed()) {
          board[y + y_inc[i]][x + x_inc[i]].reveal();
          if (board[y + y_inc[i]][x + x_inc[i]].getNumber() == 0) 
            revealZero(x + x_inc[i], y + y_inc[i], board);
        }
      }
      catch (ArrayIndexOutOfBoundsException e) {
        //just don't do anything if there isn't a square there
      }
    }
  }
  public static void revealBoard(Square[][] board) {
    for (int y = 0; y < board.length; y++) {
      for (int x = 0; x < board[y].length; x++) {
        board[y][x].reveal();
      }
    }
    displayBoard(board);
  }
  public static boolean replayInput() {
    Scanner scan = new Scanner(System.in); //need another scanner to avoid it bugging out infinitely
    System.out.println("Play Again? (Y/N)");
    char uChoice = Character.toUpperCase(scan.next().charAt(0));
    if (uChoice == 'Y')
      return true;
    else if (uChoice == 'N') {
      return false;
    } 
    else {
      System.out.println("That is not a valid choice. Please enter \'Y\'or \'N\'");
      return replayInput();
    }
  }
}
