package kz.greetgo.cmd.core.util;

import kz.greetgo.cmd.core.errors.CannotFindDirWithFile;

import java.io.File;
import java.nio.file.Path;

public class PathUtil {
  public static Path findRoot() {
    return findDirWithFile(ProjectParams.DOT_GREETGO, AppUtil.currentWorkingDir());
  }

  public static Path findDirWithFile(String simpleFileName, Path sourceDir) {

    File current = sourceDir.toFile();

    while (true) {
      if (current.toPath().resolve(simpleFileName).toFile().exists()) {
        return current.toPath().toAbsolutePath();
      }

      current = current.getParentFile();

      if (current == null) {
        throw new CannotFindDirWithFile(simpleFileName, sourceDir);
      }
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
