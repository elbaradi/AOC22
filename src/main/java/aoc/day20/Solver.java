package aoc.day20;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

class Solver {

  private static final String INPUT_PATHNAME_TEMPLATE = "src/resources/input_day_##.txt";
  private static final String DAY = "20";

  public static void main(String[] args) {

    List<String> lines = getRawInput();
    LinkedList<Value> encryptedValues = getEncryptedValues(lines);

    // Consider creating circular linked list
    // Problem #1
    LinkedList<Value> decryptedValues = decrypt(encryptedValues);
    int size = decryptedValues.size();
    Value zero = decryptedValues.stream().filter(e -> e.value() == 0).findFirst().get();
    int base = decryptedValues.indexOf(zero);
    long sumOfCoordinates = decryptedValues.get((base + 1_000) % size).value() + decryptedValues.get((base + 2_000) % size).value() + decryptedValues.get((base + 3_000) % size).value();

    System.out.println("Solution Problem #1: " + sumOfCoordinates);

    // Problem #2
    long decryptionKey = 811589153L;
    LinkedList<Value> encryptedValuesWithKey = encryptedValues.stream().map(e -> new Value(e.id(), e.value() * decryptionKey)).collect(
        Collectors.toCollection(LinkedList::new));

    decryptedValues = decrypt(encryptedValuesWithKey);

    for (int i = 0; i < 9; i++)
      decryptedValues = decrypt(encryptedValuesWithKey, decryptedValues);

    base = decryptedValues.indexOf(zero);
    sumOfCoordinates = decryptedValues.get((base + 1_000) % size).value() + decryptedValues.get((base + 2_000) % size).value() + decryptedValues.get((base + 3_000) % size).value();

    System.out.println("Solution Problem #2: " + sumOfCoordinates);
  }

  private static LinkedList<Value> decrypt(LinkedList<Value> encryptedValues) {
    LinkedList<Value> decryptedValues = new LinkedList<>(encryptedValues);

    int size = encryptedValues.size() - 1;

    for (Value e : encryptedValues) {
      long index = decryptedValues.indexOf(e);
      decryptedValues.remove(e);
      index = (index + e.value()) % size;
      if (e.value() < 0 && index <= 0)
        index += size;
      decryptedValues.add((int) index, e);
    }

    return decryptedValues;
  }

  private static LinkedList<Value> decrypt(LinkedList<Value> encryptedValues, LinkedList<Value> mixedValues) {
    LinkedList<Value> decryptedValues = new LinkedList<>(mixedValues);

    int size = encryptedValues.size() - 1;

    for (Value e : encryptedValues) {
      long index = decryptedValues.indexOf(e);
      decryptedValues.remove(e);
      index = (index + e.value()) % size;
      if (e.value() < 0 && index <= 0)
        index += size;
      decryptedValues.add((int) index, e);
    }

    return decryptedValues;
  }

  private static LinkedList<Value> getEncryptedValues(List<String> lines) {
    LinkedList<Value> values = new LinkedList<>();

    int i = 0;
    for (String line : lines) {
      values.add(new Value(i, Long.parseLong(line)));
      i++;
    }

    return values;
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
