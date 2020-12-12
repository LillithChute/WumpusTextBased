import controller.Controller;
import interfaces.IDungeon;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import model.Game;

/** The main class that kicks off the application.*/
public class Main {
  /**
   * The main method is used to start the game by input arguments.
   *
   * @param args The input type of game version.
   */
  public static void main(String[] args) {
    IDungeon game = createGame();

    System.out.println(game.getAdventurerLocation());

    try {
      new Controller(new InputStreamReader(System.in), System.out).start(game);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Generate the dungeon by input the dimensions, type, and adventurer's start position.
   */
  public static IDungeon createGame() {
    Scanner scan = new Scanner(System.in);
    System.out.println("Input the number of rows: ");
    int rows = scan.nextInt();
    System.out.println("Input the number of columns: ");
    int cols = scan.nextInt();

    // possible number of walls
    int upperBound = (rows - 1) * cols + (cols - 1) * rows - rows * cols + 1;
    System.out.println("Input the number of walls (0 - " + upperBound + "): ");
    int remainingWalls = scan.nextInt();

    if (remainingWalls > rows * (cols - 1) + (rows - 1) * cols - rows * cols + 1) {
      throw new IllegalArgumentException("Wrong number of remaining walls.");
    }

    System.out.println("Is the dungeon perfect(true/false)?");
    boolean isPerfect = scan.nextBoolean();
    System.out.println("Is the dungeon wrapping(true/false)?");
    boolean isWrapping = scan.nextBoolean();
    System.out.println("Input the percentage of bats (A number between 0 and 1): ");
    double batPercent = Double.parseDouble(scan.next());
    System.out.println("Input the percentage of pits (A number between 0 and 1): ");
    double pitPercent = Double.parseDouble(scan.next());
    System.out.println("Input the number of arrows: ");
    int arrows = scan.nextInt();
    IDungeon game =
        new Game(
            rows, cols, remainingWalls, isPerfect, isWrapping, batPercent, pitPercent, arrows, 1);

    if (game.checkUnwinnable()) {
      System.out.println("This game cannot be won.");
    } else {
      System.out.println("This is a winnable game.");
    }

    return game;
  }
}
