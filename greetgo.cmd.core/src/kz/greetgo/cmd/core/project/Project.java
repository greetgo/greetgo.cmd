package kz.greetgo.cmd.core.project;

import kz.greetgo.cmd.core.util.FileUtil;

import java.nio.file.Path;

import static kz.greetgo.cmd.core.util.PathUtil.CONTROLLER_PACKAGE_TXT;
import static kz.greetgo.cmd.core.util.PathUtil.DOT_GREETGO;
import static kz.greetgo.cmd.core.util.PathUtil.PROJECT_NAME_TXT;

public class Project {
  private final Path root;

  private Project(Path root) {
    this.root = root;
  }

  public static Project openProject(Path root) {
    return new Project(root);
  }

  public String getName() {
    return FileUtil.readParamFile(root.resolve(DOT_GREETGO).resolve(PROJECT_NAME_TXT));
  }

  public PackageRef getControllerPackageRef() {
    String content = FileUtil.readParamFile(root.resolve(DOT_GREETGO).resolve(CONTROLLER_PACKAGE_TXT));
    String[] parts = content.split("\\s+");
    return new PackageRef(root, parts[0], parts[1]);
  }
}
