package aoc.day02;

import static aoc.day02.Guide.getGuideFromFile;

class Solver {

  private static final String INPUT_PATHNAME_TEMPLATE = "src/resources/input_day_##.txt";
  private static final String DAY = "02";

  public static void main(String[] args) {
    // Problem #1
    int totalScoreWhenPlayerIsSecondColumn =
        getGuideFromFile(getFilePathname()).getRows().stream()
                .mapToInt(Guide.Row::calculateScoreWithPlayerColumn)
                .sum();

    System.out.println("Solution Problem #1: " + totalScoreWhenPlayerIsSecondColumn);

    // Problem #2
    int totalScoreWhenOutcomeIsSecondColumn =
        getGuideFromFile(getFilePathname()).getRows().stream()
            .mapToInt(Guide.Row::calculateScoreWithOutcomeColumn)
            .sum();

    System.out.println("Solution Problem #2: " + totalScoreWhenOutcomeIsSecondColumn);

  }

  public static String getFilePathname() {
    return INPUT_PATHNAME_TEMPLATE.replace("##", DAY);
  }

}
