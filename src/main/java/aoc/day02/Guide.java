package aoc.day02;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Guide {
  private final List<Row> rows = new ArrayList<>();
  private static final int FIRST_COLUMN_POSITION = 0;
  private static final int SECOND_COLUMN_POSITION = 2;

  record Row(String row) {

    // A = ROCK = 1
    // B = PAPER = 2
    // C = SCISSORS = 3
    private int opponent() {
      return row.charAt(FIRST_COLUMN_POSITION) - 'A' + 1;
    }

    // X = ROCK = 1
    // Y = PAPER = 2
    // Z = SCISSORS = 3
    private int player() {
      return row.charAt(SECOND_COLUMN_POSITION) - 'X' + 1;
    }

    // X = LOSE = 0
    // Y = DRAW = 3
    // Z = WIN = 6
    private int outcome() {
      return (row.charAt(SECOND_COLUMN_POSITION) - 'X') * 3;
    }

    public int calculateScoreWithPlayerColumn() {
      return player() + calculateOutcomeScore();
    }

    public int calculateScoreWithOutcomeColumn() {
      return calculateShapeScore() + outcome();
    }

    // ROCK, PAPER, SCISSORS = 1, 2, 3
    private int calculateShapeScore() {
      // ROCK
      if (((outcome() / 3) + opponent()) % 3 == 2)
        return 1;
      // PAPER
      else if (((outcome() / 3) + opponent()) % 3 == 0)
        return 2;
      // SCISSORS
      else
        return 3;
    }

    // WIN, DRAW, LOSE = 6, 3, 0
    private int calculateOutcomeScore() {
      // WIN
      if ((player() - opponent() + 3) % 3 == 1)
        return 6;
      // DRAW
      else if (player() - opponent() == 0)
        return 3;
      // LOSE
      else
        return 0;
    }

  }

  public List<Row> getRows() {
    return rows;
  }

  public static Guide getGuideFromFile(String pathname) {
    try {
      File file = new File(pathname);
      return getGuideFromInput(file);
    }
    catch (FileNotFoundException ex) {
      System.out.println("File not found!");
    }

    return new Guide();
  }

  private static Guide getGuideFromInput(File file) throws FileNotFoundException {
    Scanner input = new Scanner(file);
    Guide guide = new Guide();
    while (input.hasNextLine()) {
      guide.rows.add(new Row(input.nextLine()));
    }
    input.close();
    return guide;
  }

}
