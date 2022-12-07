package aoc.day02;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

record Game(char opponentRPS, char advice) {

  public int calculateScoreWhenPlayerMoveIsAdvised() {
    if (player().beats(opponent())) {
      return 6 + player().score();
    }
    else if (player().draws(opponent())) {
      return 3 + player().score();
    }
    else {
      return player().score();
    }
  }

  public int calculateScoreWhenOutcomeIsAdvised() {
    try {
      return switch (advice) {
        case 'X' -> opponent().beats().getField("score").getInt(null);
        case 'Y' -> 3 + opponent().draws().getField("score").getInt(null);
        case 'Z' -> 6 + opponent().losesTo().getField("score").getInt(null);
        default -> throw new IllegalArgumentException("Invalid character: " + advice);
      };
    } catch (NoSuchFieldException | IllegalAccessException e) {
      System.err.println("Error calculating score when outcome is advised: " + e.getMessage());
      return 0;
    }
  }

  private Shape opponent() {
    return Shape.of(opponentRPS);
  }

  private Shape player() {
    return Shape.of(advice);
  }

  public static List<Game> getGamesFromFile(String pathname) {
    try {
      File file = new File(pathname);
      return getGamesFromInput(file);
    }
    catch (FileNotFoundException ex) {
      System.out.println("File not found!");
    }

    return Collections.emptyList();
  }

  private static List<Game> getGamesFromInput(File file) throws FileNotFoundException {
    Scanner input = new Scanner(file);
    List<Game> games = new ArrayList<>();
    while (input.hasNextLine()) {
      char opponent = input.next().charAt(0);
      char advice = input.next().charAt(0);
      games.add(new Game(opponent, advice));
    }
    input.close();
    return games;
  }

}
