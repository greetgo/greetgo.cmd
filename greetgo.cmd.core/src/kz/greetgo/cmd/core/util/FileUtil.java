package kz.greetgo.cmd.core.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileUtil {
  public static String readParamFile(Path pathToFile) {
    String value = readParamFileNull(pathToFile);
    return value == null ? "" : value;
  }

  public static String readParamFileNull(Path pathToFile) {
    if (!pathToFile.toFile().exists()) return null;
    try {
      List<String> stringList = Files.readAllLines(pathToFile);

      for (String line : stringList) {
        String trimmedLine = line.trim();
        if (trimmedLine.length() == 0) continue;
        if (trimmedLine.startsWith("#")) continue;
        return trimmedLine;
      }

      return null;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

  }
}
