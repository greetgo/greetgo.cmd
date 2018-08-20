package kz.greetgo.cmd.core.util;

import kz.greetgo.cmd.core.errors.CannotFindDirWithFile;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PathUtil {
  public static Path currentWorkingDir() {
    return Paths.get(System.getenv("CURRENT_WORKING_DIR"));
  }

  public static final String DOT_GREETGO = ".greetgo";
  public static final String PROJECT_NAME_TXT = "project-name.txt";
  public static final String CONTROLLER_MARKER_INTERFACE_TXT = "controller-marker-interface.txt";
  public static final String CONTROLLER_PACKAGE_TXT = "controller-package.txt";
  public static final String REGISTER_INTERFACE_PACKAGE_TXT = "register-interface-package.txt";

  public static Path findRoot() {
    return findDirWithFile(DOT_GREETGO, currentWorkingDir());
  }

  public static Path findDirWithFile(String simpleFileName, Path sourceDir) {

    File current = sourceDir.toFile();

    while (true) {
      if (current.toPath().resolve(simpleFileName).toFile().exists()) {
        return current.toPath().toAbsolutePath();
      }

      current = current.getParentFile();

      if (current == null) throw new CannotFindDirWithFile(simpleFileName, sourceDir);
    }
  }

  public static String toPoints(String packagePath) {
    while (packagePath.startsWith("/")
      || packagePath.startsWith("\\")
      || packagePath.startsWith(".")
    ) {
      packagePath = packagePath.substring(1);
    }
    return packagePath.replace('/', '.').replace('\\', '.');
  }
}
