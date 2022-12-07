package aoc.day02;

import static aoc.day02.Game.getGamesFromFile;

class Solver {

  private static final String INPUT_PATHNAME_TEMPLATE = "src/resources/input_day_##.txt";
  private static final String DAY = "02";

  public static void main(String[] args) {
    // Problem #1
    int totalScoreWhenPlayerMoveIsAdvisedInSecondColumn =
        getGamesFromFile(getFilePathname()).stream()
            .mapToInt(Game::calculateScoreWhenPlayerMoveIsAdvised)
            .sum();

    System.out.println("Solution Problem #1: " + totalScoreWhenPlayerMoveIsAdvisedInSecondColumn);

    // Problem #2
      int totalScoreWhenOutcomeIsAdvisedInSecondColumn =
          getGamesFromFile(getFilePathname()).stream()
              .mapToInt(Game::calculateScoreWhenOutcomeIsAdvised)
              .sum();

      System.out.println("Solution Problem #2: " + totalScoreWhenOutcomeIsAdvisedInSecondColumn);

  }

  public static String getFilePathname() {
    return INPUT_PATHNAME_TEMPLATE.replace("##", DAY);
  }

}
