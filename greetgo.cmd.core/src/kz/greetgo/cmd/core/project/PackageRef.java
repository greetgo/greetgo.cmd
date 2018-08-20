package kz.greetgo.cmd.core.project;

import java.nio.file.Path;

import static kz.greetgo.cmd.core.util.PathUtil.toPoints;

public class PackageRef {
  public final Path root;
  public final String sourcePath;
  public final String packagePath;

  public PackageRef(Path root, String sourcePath, String packagePath) {
    this.root = root;
    this.sourcePath = sourcePath;
    this.packagePath = packagePath;
  }

  public Path finishRoot() {
    return root.resolve(sourcePath).resolve(packagePath);
  }

  public PackageRef subPackage(String subPackage) {
    String packageName = toPoints(packagePath) + "." + toPoints(subPackage);
    String packagePath = packageName.replace('.', '/');
    return new PackageRef(root, sourcePath, packagePath);
  }

  public ClassRef createClassRef(String name) {
    return new ClassRef(this, name);
  }
}
