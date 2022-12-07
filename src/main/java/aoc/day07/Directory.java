package aoc.day07;

import java.util.ArrayList;
import java.util.List;

public class Directory {

  private final String name;

  private final Directory parent;
  private final List<Directory> subdirectories = new ArrayList<>();
  private final List<File> files = new ArrayList<>();

  record File(String name, int size) {
    public String name() {
      return name;
    }

    public int size() {
      return size;
    }
  }

  Directory(String dir, Directory parentDir) {
    this.name = dir;
    this.parent = parentDir;
  }

  public String getName() {
    return name;
  }

  public List<Directory> getSubdirectories() {
    return subdirectories;
  }

  public List<File> getFiles() {
    return files;
  }

  public Directory getParent() {
    return parent;
  }

  public int getSize() {
    return files.stream().mapToInt(File::size).sum() + subdirectories.stream().mapToInt(Directory::getSize).sum();
  }
}


