package kz.greetgo.cmd.core.project;

import java.nio.file.Path;

public class PackageRef {
  public final Path root;
  public final String sourcePath;
  public final String packagePath;

  public PackageRef(Path root, String sourcePath, String packagePath) {
    this.root = root;
    this.sourcePath = sourcePath;
    this.packagePath = packagePath;
  }

  public ClassRef createClassRef(String controllerName) {
    return new ClassRef(root.resolve(sourcePath), packagePath, controllerName);
  }
}
