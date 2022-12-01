package aoc.day01;

import java.util.Collections;

import static aoc.day01.Elf.getElvesFromFile;

public class Solver {

  private static final String INPUT_PATHNAME_TEMPLATE = "src/resources/input_day_##.txt";
  private static final String DAY = "01";

  public static void main(String[] args) {
    // Problem #1
    int highestCalorieCount =
        getElvesFromFile(getInputFilePathname()).stream()
            .map(Elf::getCaloricValueOfInventory)
            .reduce(0, Integer::max);

    System.out.println("Solution Problem #1: " + highestCalorieCount);

    // Problem #2
    int calorieCountOfTopThree =
        getElvesFromFile(getInputFilePathname()).stream()
            .map(Elf::getCaloricValueOfInventory)
            .sorted(Collections.reverseOrder())
            .limit(3)
            .reduce(0, Integer::sum);

    System.out.println("Solution Problem #2: " + calorieCountOfTopThree);

  }

  public static String getInputFilePathname() {
    return INPUT_PATHNAME_TEMPLATE.replace("##", DAY);
  }

}
