package aoc.day09;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class Solver {

  private static final String INPUT_PATHNAME_TEMPLATE = "src/resources/input_day_##.txt";
  private static final String DAY = "09";

  public static void main(String[] args) {

    List<String> lines = getRawInput();
    List<Instruction> instructions = parseInstructions(lines);

    // Problem #1
    Rope ropeWithTwoKnots = new Rope(new Pos(0, 0), 2);
    instructions.forEach(
        instruction -> ropeWithTwoKnots.moveRope(
            instruction.getDirection(),
            instruction.getSteps()
        )
    );
    long uniquePositionsVisitedByTailOfShortRope =
        ropeWithTwoKnots.getTailHistory().stream()
            .distinct()
            .count();

    System.out.println("Solution Problem #1: " + uniquePositionsVisitedByTailOfShortRope);

    // Problem #2
    Rope ropeWithTenKnots = new Rope(new Pos(0, 0), 10);
    instructions.forEach(
        instruction -> ropeWithTenKnots.moveRope(
            instruction.getDirection(),
            instruction.getSteps()
        )
    );
    long uniquePositionsVisitedByTailOfLongRope =
        ropeWithTenKnots.getTailHistory().stream()
            .distinct()
            .count();

    System.out.println("Solution Problem #2: " + uniquePositionsVisitedByTailOfLongRope);
  }

  private static List<Instruction> parseInstructions(List<String> lines) {
    List<Instruction> instructions = new ArrayList<>();

    lines.forEach(line -> instructions.add(new Instruction(line)));

    return instructions;
  }

  private static List<String> getRawInput() {
    List<String> lines = new ArrayList<>();
    try (BufferedReader reader = new BufferedReader(new FileReader(getInputPathname()))) {
      reader.lines().forEach(lines::add);
    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
    return lines;
  }

  public static String getInputPathname() {
    return INPUT_PATHNAME_TEMPLATE.replace("##", DAY);
  }

}
