package kz.greetgo.cmd.core.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static java.nio.charset.StandardCharsets.UTF_8;

public class FileUtil {
  public static void strToFile(String str, File toFile) {
    toFile.getParentFile().mkdirs();
    try {
      Files.write(toFile.toPath(), str.getBytes(UTF_8));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
