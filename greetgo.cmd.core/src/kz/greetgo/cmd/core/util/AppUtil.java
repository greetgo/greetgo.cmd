package kz.greetgo.cmd.core.util;

import java.nio.file.Path;
import java.nio.file.Paths;

public class AppUtil {
  public static String usedCommand() {
    return System.getenv("USED_COMMAND");
  }

  public static Path currentWorkingDir() {
    return Paths.get(System.getenv("CURRENT_WORKING_DIR"));
  }

  public static String version() {
    return AppUtil.class.getPackage().getSpecificationVersion();
  }

  public static String gitId() {
    return AppUtil.class.getPackage().getImplementationVersion();
  }

}
