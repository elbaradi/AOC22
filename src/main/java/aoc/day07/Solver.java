package aoc.day07;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

class Solver {

  private static final String INPUT_PATHNAME_TEMPLATE = "src/resources/input_day_##.txt";
  private static final String DAY = "07";

  public static void main(String[] args) {

    List<String> lines = getRawInput();
    List<Directory> directories = parseRawDirectories(lines);

    // Problem #1
    int totalSizeOfDirectoriesNoLargerThanOneHundredThousand =
        directories.stream().filter(dir -> dir.getSize() <= 100_000).mapToInt(Directory::getSize).sum();
    System.out.println("Solution Problem #1: " + totalSizeOfDirectoriesNoLargerThanOneHundredThousand);

    // Problem #2
    int totalDiskSpace = 70_000_000;
    int neededUnusedSpace = 30_000_000;
    int currentlyUsedSpace = directories.stream().mapToInt(Directory::getSize).max().orElse(0); // size of root dir
    int diskSpaceNeededToBeFreed = neededUnusedSpace - (totalDiskSpace - currentlyUsedSpace);

    Directory smallestDirectoryThatCanBeDeletedToFreeUpDiskSpace = directories.stream()
        .filter(dir -> dir.getSize() >= diskSpaceNeededToBeFreed)
        .min(Comparator.comparing(Directory::getSize))
        .orElse(null);

    System.out.println("Solution Problem #2: " + smallestDirectoryThatCanBeDeletedToFreeUpDiskSpace.getSize());
  }

  private static List<Directory> parseRawDirectories(List<String> lines) {
    Directory currentParent = null;
    Directory current = null;

    List<Directory> directories = new ArrayList<>();

    for (String line : lines) {
      String[] tokens = line.split(" ");
      if (line.startsWith("$ cd ..")) {
        current = currentParent;
        currentParent = current.getParent();
      }
      else if (line.startsWith("$ cd")) {
        Directory subdir = new Directory(tokens[2], current);
        if (current != null) {
          current.getSubdirectories().add(subdir);
        }
        currentParent = current;
        directories.add(subdir);
        current = subdir;
      }
      else if (line.startsWith("$ ls")) ;
      else if (line.startsWith("dir")) ;
      else {
        current.getFiles().add(new Directory.File(tokens[1], Integer.parseInt(tokens[0])));
      }
    }

    return directories;
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
