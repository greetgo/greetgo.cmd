package kz.greetgo.cmd.core.project;

import kz.greetgo.cmd.core.util.ProjectParams;

import java.nio.file.Path;
import java.util.List;

import static kz.greetgo.cmd.core.util.ProjectParams.*;

public class Project {
  private final Path root;

  private Project(Path root) {
    this.root = root;
  }

  public static Project openProject(Path root) {
    return new Project(root);
  }

  public String getName() {
    return readValue(paramFile(PROJECT_NAME_TXT));
  }

  public PackageRef getControllerPackageRef() {
    return getPackageRef(CONTROLLER_PACKAGE_TXT);
  }

  private PackageRef getPackageRef(String fileName) {
    String content = readValue(paramFile(fileName));
    String[] parts = content.split("\\s+");
    return new PackageRef(root, parts[0], parts[1]);
  }

  private Path paramFile(String fileName) {
    return root.resolve(DOT_GREETGO).resolve(fileName);
  }

  public String getControllerMarkerInterface() {
    return readValueOrNull(paramFile(CONTROLLER_MARKER_INTERFACE_TXT));
  }

  public PackageRef getRegisterInterfacePackageRef() {
    return getPackageRef(REGISTER_INTERFACE_PACKAGE_TXT);
  }

  public PackageRef getRegisterImplPackageRef() {
    return getPackageRef(REGISTER_IMPL_PACKAGE_TXT);
  }

  public PackageRef getMybatisDaoPackageRef() {
    return getPackageRef(MYBATIS_DAO_PACKAGE_TXT);
  }

  public List<String> getMybatisDaoDatabases() {
    return ProjectParams.readLines(paramFile(MYBATIS_DAO_DATABASES_TXT));
  }
}
