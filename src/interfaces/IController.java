package interfaces;

import java.io.IOException;

/** The operations allowable for a controller.*/
public interface IController {
  /**
   * Motivate the start of the maze game's game.controller.
   *
   * @throws IOException if an I/O error occurs.
   */
  void start(IDungeon game) throws IOException;
}
