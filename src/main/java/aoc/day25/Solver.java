package aoc.day25;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.pow;

class Solver {

  private static final String INPUT_PATHNAME_TEMPLATE = "src/resources/input_day_##.txt";
  private static final String DAY = "25";

  public static void main(String[] args) {

    List<String> numbersInSnafu = getRawInput();
    List<Long> numbersInDecimal = parseSnafuToDecimal(numbersInSnafu);

    // Problem #1
    long sumInDecimal = numbersInDecimal.stream().mapToLong(Long::longValue).sum();
    String sumInSnafu = parseDecimalToSnafu(sumInDecimal);

    System.out.println("Solution Problem #1: " + sumInSnafu);

  }

  // Decimal to Base 5 but different
  private static String parseDecimalToSnafu(long decimal) {

    long adjustedDecimal = decimal;
    int snafuSize = 0;

    for (int i = 0; adjustedDecimal / pow(5, i) >= 1; i++) {
      adjustedDecimal += 2 * pow(5, i);
      snafuSize++;
    }

    adjustedDecimal -= 2 * pow(5, snafuSize - 1);

    if (adjustedDecimal == 0)
      return "0";

    String snafu = "";
    while (adjustedDecimal > 0 && adjustedDecimal / 5 > 0) {
      switch ((int) (adjustedDecimal % 5)) {
        case 0 -> snafu = "=" + snafu;
        case 1 -> snafu = "-" + snafu;
        case 2 -> snafu = "0" + snafu;
        case 3 -> snafu = "1" + snafu;
        case 4 -> snafu = "2" + snafu;
      }
      adjustedDecimal = adjustedDecimal / 5;
    }

    switch ((int) (adjustedDecimal % 5)) {
      case 1 -> snafu = "1" + snafu;
      case 2 -> snafu = "2" + snafu;
    }

    return snafu;
  }

  private static List<Long> parseSnafuToDecimal(List<String> snafuNumbers) {

    List<Long> decimalNumbers = new ArrayList<>();

    for (String snafu : snafuNumbers) {
      long number = 0L;
      int size = snafu.length();
      for (int i = 0; i < size; i++) {
        number +=
            switch (snafu.charAt(i)) {
              case '=' -> -2 * pow(5, size - 1 - i);
              case '-' -> -pow(5, size - 1 - i);
              case '0' -> 0;
              case '1' -> pow(5, size - 1 - i);
              case '2' -> 2 * pow(5, size - 1 - i);
              default -> throw new IllegalArgumentException("Invalid character!");
            };
        }
      decimalNumbers.add(number);
      }

    return decimalNumbers;
  }

  private static List<String> getRawInput() {
    List<String> lines = new ArrayList<>();
    try (BufferedReader reader = new BufferedReader(new FileReader(getInputPathname()))) {
      reader.lines().forEach(lines::add);
    }
    catch (IOException e) {
      throw new RuntimeException("Retrieving input went wrong!");
    }
    return lines;
  }

  public static String getInputPathname() {
    return INPUT_PATHNAME_TEMPLATE.replace("##", DAY);
  }

}
