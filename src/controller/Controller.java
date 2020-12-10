package controller;

import interfaces.IController;
import interfaces.IDungeon;
import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;


public class Controller implements IController {
  private final Readable in;
  private final Appendable out;

  /**
   * Constructor for the ControllerImpl class.
   *
   * @param in  The input stream.
   * @param out The output stream.
   */
  public Controller(Readable in, Appendable out) {
    this.in = in;
    this.out = out;
  }

  @Override
  public void start(IDungeon game) throws IOException, IllegalArgumentException {
    Objects.requireNonNull(game);
    String direction;
    int distance;
    try (Scanner scan = new Scanner(this.in)) {
      while (!game.getAdventuresEnd()) {
        System.out.println("Shoot or Move? ");
        switch (scan.next()) {
          case "Shoot":
            System.out.println("Towards direction(E/W/N/S)? ");
            direction = scan.next();
            System.out.println("No. of caves? ");
            distance = scan.nextInt();
            game.shoot(direction, distance);
            this.out.append(game.getShotResult());
            break;
          case "Move":
            System.out.println("Where to? ");
            direction = scan.next();
            game.move(direction);
            this.out.append(game.getAdventurerLocation());
            this.out.append(" ");
            break;
          case "Quit":
            return;
          default:
            break;
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
