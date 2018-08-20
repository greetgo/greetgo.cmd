package kz.greetgo.cmd.core.project;

import kz.greetgo.cmd.core.errors.No_CONTROLLER_MARKER_INTERFACE_TXT;
import kz.greetgo.cmd.core.errors.No_CONTROLLER_PACKAGE_TXT;
import kz.greetgo.cmd.core.errors.No_MYBATIS_DAO_DATABASES_TXT;
import kz.greetgo.cmd.core.errors.No_MYBATIS_DAO_PACKAGE_TXT;
import kz.greetgo.cmd.core.errors.No_MYBATIS_TEST_DAO_PACKAGE_TXT;
import kz.greetgo.cmd.core.errors.No_REGISTER_IMPL_PACKAGE_TXT;
import kz.greetgo.cmd.core.errors.No_REGISTER_IMPL_TEST_BEFORE_CLASS_TXT;
import kz.greetgo.cmd.core.errors.No_REGISTER_IMPL_TEST_EXTENDS_TXT;
import kz.greetgo.cmd.core.errors.No_REGISTER_IMPL_TEST_PACKAGE_TXT;
import kz.greetgo.cmd.core.errors.No_REGISTER_INTERFACE_PACKAGE_TXT;
import kz.greetgo.cmd.core.util.ProjectParams;
import kz.greetgo.cmd.core.util.ReadResult;

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
    return readValue(paramFile(PROJECT_NAME_TXT)).get();
  }

  public PackageRef getControllerPackageRef() {
    ReadResult<PackageRef> packageRef = getPackageRef(CONTROLLER_PACKAGE_TXT);
    if (packageRef.isValueAbsent()) {
      throw new No_CONTROLLER_PACKAGE_TXT(paramFile(CONTROLLER_PACKAGE_TXT));
    }
    return packageRef.get();
  }

  public PackageRef getRegisterInterfacePackageRef() {
    ReadResult<PackageRef> packageRef = getPackageRef(REGISTER_INTERFACE_PACKAGE_TXT);
    if (packageRef.isValueAbsent()) {
      throw new No_REGISTER_INTERFACE_PACKAGE_TXT(paramFile(REGISTER_INTERFACE_PACKAGE_TXT));
    }
    return packageRef.get();
  }

  public String getControllerMarkerInterface() {
    ReadResult<String> result = readValue(paramFile(CONTROLLER_MARKER_INTERFACE_TXT));
    if (result.isFileAbsent()) {
      throw new No_CONTROLLER_MARKER_INTERFACE_TXT(paramFile(CONTROLLER_MARKER_INTERFACE_TXT));
    }
    return result.getOr(null);
  }

  public PackageRef getRegisterImplPackageRef() {
    ReadResult<PackageRef> result = getPackageRef(REGISTER_IMPL_PACKAGE_TXT);
    if (result.isValueAbsent()) {
      throw new No_REGISTER_IMPL_PACKAGE_TXT(paramFile(REGISTER_IMPL_PACKAGE_TXT));
    }
    return result.get();
  }

  public List<String> getMybatisDaoDatabases() {
    ReadResult<List<String>> result = ProjectParams.readLines(paramFile(MYBATIS_DAO_DATABASES_TXT));
    if (result.isValueAbsent()) {
      throw new No_MYBATIS_DAO_DATABASES_TXT(paramFile(MYBATIS_DAO_DATABASES_TXT));
    }
    return result.get();
  }

  public PackageRef getMybatisDaoPackageRef() {
    ReadResult<PackageRef> result = getPackageRef(MYBATIS_DAO_PACKAGE_TXT);
    if (result.isValueAbsent()) {
      throw new No_MYBATIS_DAO_PACKAGE_TXT(paramFile(MYBATIS_DAO_PACKAGE_TXT));
    }
    return result.get();
  }

  public PackageRef getRegisterImplTestPackageRef() {
    ReadResult<PackageRef> result = getPackageRef(REGISTER_IMPL_TEST_PACKAGE_TXT);
    if (result.isValueAbsent()) {
      throw new No_REGISTER_IMPL_TEST_PACKAGE_TXT(paramFile(REGISTER_IMPL_TEST_PACKAGE_TXT));
    }
    return result.get();
  }

  public List<String> getRegisterImplTestBeforeClass() {
    ReadResult<List<String>> result = ProjectParams.readLines(paramFile(REGISTER_IMPL_TEST_BEFORE_CLASS_TXT));
    if (result.isFileAbsent()) {
      throw new No_REGISTER_IMPL_TEST_BEFORE_CLASS_TXT(paramFile(REGISTER_IMPL_TEST_BEFORE_CLASS_TXT));
    }
    return result.getOr(null);
  }

  public String getRegisterImplTestExtends() {
    ReadResult<String> result = readValue(paramFile(REGISTER_IMPL_TEST_EXTENDS_TXT));
    if (result.isFileAbsent()) {
      throw new No_REGISTER_IMPL_TEST_EXTENDS_TXT(paramFile(REGISTER_IMPL_TEST_EXTENDS_TXT));
    }
    return result.getOr(null);
  }

  public PackageRef getMybatisTestDaoPackageRef() {
    ReadResult<PackageRef> result = getPackageRef(MYBATIS_TEST_DAO_PACKAGE_TXT);
    if (result.isValueAbsent()) {
      throw new No_MYBATIS_TEST_DAO_PACKAGE_TXT(paramFile(MYBATIS_TEST_DAO_PACKAGE_TXT));
    }
    return result.get();
  }

  private ReadResult<PackageRef> getPackageRef(String fileName) {
    ReadResult<String> content = readValue(paramFile(fileName));
    if (content.isValueAbsent()) return content.cast();
    String[] parts = content.get().split("\\s+");
    return ReadResult.of(new PackageRef(root, parts[0], parts[1]));
  }

  private Path paramFile(String fileName) {
    return root.resolve(DOT_GREETGO).resolve(fileName);
  }
}
