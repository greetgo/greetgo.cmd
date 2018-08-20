package kz.greetgo.cmd.core.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ProjectParams {
  public static final String DOT_GREETGO = ".greetgo";
  public static final String PROJECT_NAME_TXT = "project-name.txt";
  public static final String CONTROLLER_MARKER_INTERFACE_TXT = "controller-marker-interface.txt";
  public static final String CONTROLLER_PACKAGE_TXT = "controller-package.txt";
  public static final String REGISTER_INTERFACE_PACKAGE_TXT = "register-interface-package.txt";
  public static final String REGISTER_IMPL_PACKAGE_TXT = "register-impl-package.txt";
  public static final String MYBATIS_DAO_PACKAGE_TXT = "mybatis-dao-package.txt";
  public static final String MYBATIS_DAO_DATABASES_TXT = "mybatis-dao-databases.txt";

  public static final String REGISTER_IMPL_TEST_PACKAGE_TXT = "register-impl-test-package.txt";
  public static final String REGISTER_IMPL_TEST_BEFORE_CLASS_TXT = "register-impl-test-before-class.txt";
  public static final String REGISTER_IMPL_TEST_EXTENDS_TXT = "register-impl-test-extends.txt";

  public static final String MYBATIS_TEST_DAO_PACKAGE_TXT = "mybatis-test-dao-package.txt";

  public static String readValue(Path pathToFile) {
    String value = readValueOrNull(pathToFile);
    return value == null ? "" : value;
  }

  public static String readValueOrNull(Path pathToFile) {
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

  public static List<String> readLines(Path pathToFile) {
    if (!pathToFile.toFile().exists()) return null;
    try {
      List<String> stringList = Files.readAllLines(pathToFile);
      List<String> ret = new ArrayList<>();

      for (String line : stringList) {
        String trimmedLine = line.trim();
        if (trimmedLine.length() == 0) continue;
        if (trimmedLine.startsWith("#")) continue;
        ret.add(trimmedLine);
      }

      return ret;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

  }
}
