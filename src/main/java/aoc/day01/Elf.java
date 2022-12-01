package aoc.day01;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Elf {

  private final List<Item> inventory = new ArrayList<>();

  private record Item(int calories) {

    public int getCaloricValue() {
      return calories;
    }

  }

  public void addItemToInventory(int caloricValueOfItem) {
    inventory.add(new Item(caloricValueOfItem));
  }

  public int getCaloricValueOfInventory() {
    return inventory.stream().mapToInt(Item::getCaloricValue).sum();
  }

  public static List<Elf> getElvesFromFile(String pathname) {
    try {
      File file = new File(pathname);
      return getElvesFromInput(file);
    }
    catch (FileNotFoundException ex) {
      System.out.println("File not found!");
    }

    return Collections.emptyList();
  }

  private static List<Elf> getElvesFromInput(File file) throws FileNotFoundException {
    Scanner input = new Scanner(file);
    List<Elf> elves = new ArrayList<>();
    Elf elf = new Elf();
    elves.add(elf);
    while (input.hasNextLine()) {
      String line = input.nextLine();
      if (line.equals("")) {
        elf = new Elf();
        elves.add(elf);
      }
      else {
        elf.addItemToInventory(
            Integer.parseInt(line));
      }
    }
    input.close();
    return elves;
  }

}
