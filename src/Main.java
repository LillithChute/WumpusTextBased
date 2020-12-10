import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import controller.Controller;
import interfaces.IDungeon;
import model.Game;

public class Main {
  /**
   * The main method is used to start either version of the maze game by input arguments.
   *
   * @param args The input type of game version.
   */
  public static void main(String[] args) {
    IDungeon game = createMaze();

    System.out.println(game.getAdventurerLocation());

    try {
      new Controller(new InputStreamReader(System.in), System.out).start(game);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Generate the maze by input the dimension of the maze, maze type, and player's start position.
   */
  public static IDungeon createMaze() {
    Scanner scan = new Scanner(System.in);
    System.out.println("Input the rows number of the maze: ");
    int rows = scan.nextInt();
    System.out.println("Input the columns number of the maze: ");
    int cols = scan.nextInt();
    System.out.println("Input the wall number of the maze: ");
    int remains = scan.nextInt();
    System.out.println("The maze is Perfect(true/false)?");
    boolean isPerfect = scan.nextBoolean();
    System.out.println("The maze is Wrappping(true/false)?");
    boolean isWrapping = scan.nextBoolean();
    System.out.println("Input the percentage of bats: ");
    double batPercent = Double.parseDouble(scan.next());
    System.out.println("Input the percentage of pits: ");
    double pitPercent = Double.parseDouble(scan.next());
    System.out.println("Input the number of arrows: ");
    int arrows = scan.nextInt();
    IDungeon game =
        new Game(
            rows, cols, remains, isPerfect, isWrapping, batPercent, pitPercent, arrows, 1);
    return game;
  }
}
