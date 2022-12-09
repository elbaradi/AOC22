package aoc.day09;

public class Instruction {

  private final Character direction;
  private final int steps;

  Instruction(String line) {
    String[] parts = line.split(" ");
    direction = parts[0].charAt(0);
    steps = Integer.parseInt(parts[1]);
  }

  public Character getDirection() {
    return direction;
  }

  public int getSteps() {
    return steps;
  }

}
