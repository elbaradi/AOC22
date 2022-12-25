package aoc;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

// Runs all solvers subsequently, summarizing their computation time
public class All {
  public static void main(String[] args) {
    double totalTime = 0;
    for (int i = 1; i <= 25; i++) {
      String solverClass = String.format("aoc.day%02d.Solver", i);
      try {
        Class<?> solver = Class.forName(solverClass);
        Method mainMethod = solver.getDeclaredMethod("main", String[].class);
        mainMethod.setAccessible(true);

        System.out.printf("## Day %02d ##%n", i);
        long startTime = System.currentTimeMillis();
        mainMethod.invoke(null, (Object) args);
        long endTime = System.currentTimeMillis();

        long elapsedTime = endTime - startTime;
        System.out.println(solverClass + " took " + elapsedTime + "ms to run\n");

        totalTime += elapsedTime;
      } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
        e.printStackTrace();
      }
    }

    String totalTimeInSecondsString = String.format("%.1f", totalTime / 1_000);
    System.out.println("All solutions combined took " + totalTimeInSecondsString + "s to to compute");
  }
}
