package kz.greetgo.cmd.core.project;

import kz.greetgo.cmd.core.util.FileUtil;

import java.nio.file.Path;

import static kz.greetgo.cmd.core.util.PathUtil.*;

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
    return getPackageRef(CONTROLLER_PACKAGE_TXT);
  }

  private PackageRef getPackageRef(String fileName) {
    String content = FileUtil.readParamFile(root.resolve(DOT_GREETGO).resolve(fileName));
    String[] parts = content.split("\\s+");
    return new PackageRef(root, parts[0], parts[1]);
  }

  public String getControllerMarkerInterface() {
    return FileUtil.readParamFileNull(root.resolve(DOT_GREETGO).resolve(CONTROLLER_MARKER_INTERFACE_TXT));
  }

  public PackageRef getRegisterInterfacePackageRef() {
    return getPackageRef(REGISTER_INTERFACE_PACKAGE_TXT);
  }
}
