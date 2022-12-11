package aoc.day11;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

class Solver {

  private static final String INPUT_PATHNAME_TEMPLATE = "src/resources/input_day_##.txt";
  private static final String DAY = "11";

  public static void main(String[] args) {

    List<String> lines = getRawInput();
    List<Monkey> monkeys = parseMonkeys(lines);
    final long effectiveMaxWorry = monkeys.stream()
        .mapToLong(Monkey::getTest)
        .reduce(1, Math::multiplyExact);

    // Problem #1
    List<Monkey> monkeysAfterTwentyRounds = parseMonkeys(lines);

    for (int i = 0; i < 20; i++) {
      monkeysAfterTwentyRounds.forEach(
          monkey -> monkey.takeTurn(true, effectiveMaxWorry));
    }

    System.out.println("Solution Problem #1: " + getMonkeyBusiness(monkeysAfterTwentyRounds));

    // Problem #2
    List<Monkey> monkeysAfterTenThousandRounds = parseMonkeys(lines);

    for (int i = 0; i < 10_000; i++) {
      monkeysAfterTenThousandRounds.forEach(
          monkey -> monkey.takeTurn(false, effectiveMaxWorry));
    }

    System.out.println("Solution Problem #2: " + getMonkeyBusiness(monkeysAfterTenThousandRounds));

  }

  private static long getMonkeyBusiness(List<Monkey> monkeys) {
    long monkeyBusiness;

    monkeyBusiness = monkeys
        .stream()
        .sorted(Comparator.comparing(one -> -one.getInspectionCount()))
        .limit(2)
        .mapToLong(Monkey::getInspectionCount)
        .reduce(1, Math::multiplyExact);

    return monkeyBusiness;
  }

  private static List<Monkey> parseMonkeys(List<String> lines) {
    List<Monkey> monkeys = new ArrayList<>();
    Monkey monkey = new Monkey();

    for (String line : lines) {
      String[] parts = line.split(" ");
      if (line.startsWith("Monkey"))
        monkey = new Monkey();
      else if (line.startsWith("Starting items:")) {
        List<Long> items = new ArrayList<>();
        Arrays.stream(parts)
            .skip(2)
            .map(part -> part.replace(",", ""))
            .forEach(item -> items.add(Long.parseLong(item)));
        monkey.setItems(items);
      } else if (line.startsWith("Operation:")) {
        if (parts[4].equals("*"))
          monkey.setOperator(Operation.valueOf("TIMES"));
        else if (parts[4].equals("+"))
          monkey.setOperator(Operation.valueOf("PLUS"));
        monkey.setTerm(parts[5]);
      } else if (line.startsWith("Test:")) {
        monkey.setTest(Long.parseLong(parts[3]));
      } else if (line.startsWith("If true:")) {
        monkey.setSuccess(Integer.parseInt(parts[5]));
      } else if (line.startsWith("If false:")) {
        monkey.setFail(Integer.parseInt(parts[5]));
      } else if (line.isEmpty())
        monkeys.add(monkey);
    }
    monkeys.add(monkey);

    for (Monkey m : monkeys) {
      m.setMonkeyFail(monkeys.get(m.getFail()));
      m.setMonkeySuccess(monkeys.get(m.getSuccess()));
    }

    return monkeys;
  }

  private static List<String> getRawInput() {
    List<String> lines = new ArrayList<>();
    try (BufferedReader reader = new BufferedReader(new FileReader(getInputPathname()))) {
      reader.lines().map(String::stripLeading).forEach(lines::add);
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
