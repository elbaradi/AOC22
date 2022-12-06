package aoc.day06;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

class Solver {

  private static final String INPUT_PATHNAME_TEMPLATE = "src/resources/input_day_##.txt";
  private static final String DAY = "06";

  public static void main(String[] args) throws IOException {

    try (BufferedReader reader = new BufferedReader(new FileReader(getInputPathname()))) {

      String datastream = reader.readLine();

      // Problem #1
      int firstMarkerWithFourCharacterWindow = detectFirstMarker(datastream, 4);
      System.out.println("Solution Problem #1: " + firstMarkerWithFourCharacterWindow);

      // Problem #2
      int firstMarkerWithFourteenCharacterWindow = detectFirstMarker(datastream, 14);
      System.out.println("Solution Problem #2: " + firstMarkerWithFourteenCharacterWindow);
    }
  }

  public static int detectFirstMarker(String datastream, int window) {
    // initialize map to store unique characters with their most recent occurrence
    Map<Character, Integer> uniqueChars = new HashMap<>();

    // loop over characters in buffer
    for (int i = 0; i < datastream.length(); i++) {
      // add current character to map of unique characters
      Character currentChar = datastream.charAt(i);
      uniqueChars.put(currentChar, i);

      // check if set contains 14 unique characters
      if (uniqueChars.size() == window) {
        return i + 1;
      }

      // remove oldest char from map
      // if the window is at full length
      // and if the oldest char is unique in the window
      if (i >= window) {
        Character firstCharInWindow = datastream.charAt(i - (window - 1));
        if (uniqueChars.get(firstCharInWindow) == i - (window - 1)) {
          uniqueChars.remove(firstCharInWindow);
        }
      }
    }

    return -1;
  }

  public static String getInputPathname() {
    return INPUT_PATHNAME_TEMPLATE.replace("##", DAY);
  }

}
