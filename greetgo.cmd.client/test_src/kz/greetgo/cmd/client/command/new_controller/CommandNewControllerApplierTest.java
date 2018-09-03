package kz.greetgo.cmd.client.command.new_controller;

import kz.greetgo.cmd.client.test_data.TestControllerMarker;
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
import kz.greetgo.cmd.core.project.Project;
import kz.greetgo.cmd.core.util.ProjectParams;
import kz.greetgo.util.RND;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import static java.nio.charset.StandardCharsets.UTF_8;
import static kz.greetgo.cmd.core.util.ProjectParams.*;
import static org.fest.assertions.api.Assertions.assertThat;

public class CommandNewControllerApplierTest {

  Path rootProjectDir;
  String projectName;

  @BeforeMethod
  public void createTestProject() {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

    projectName = "prj" + RND.intStr(10);

    rootProjectDir = Paths.get("build")
      .resolve("tests_data")
      .resolve(CommandNewControllerApplierTest.class.getSimpleName())
      .resolve("project-" + sdf.format(new Date()) + "-" + RND.intStr(4));
  }

  private void projectParam(String fileName, String value) throws Exception {
    Path projectFileName = rootProjectDir.resolve(ProjectParams.DOT_GREETGO).resolve(fileName);
    projectFileName.toFile().getParentFile().mkdirs();
    Files.write(projectFileName, value.getBytes(UTF_8));
  }

  @Test
  public void execute() throws Exception {
    projectParam(PROJECT_NAME_TXT, projectName);
    projectParam(CONTROLLER_PACKAGE_TXT, "sandbox.controller/src kz/greetgo/sandbox/controller/controller");
    projectParam(REGISTER_INTERFACE_PACKAGE_TXT, "sandbox.controller/src kz/greetgo/sandbox/controller/register");
    projectParam(CONTROLLER_MARKER_INTERFACE_TXT, TestControllerMarker.class.getName());
    projectParam(REGISTER_IMPL_PACKAGE_TXT, "sandbox.register/src kz/greetgo/sandbox/register/impl");
    projectParam(MYBATIS_DAO_PACKAGE_TXT, "sandbox.register/src kz/greetgo/sandbox/register/dao");
    projectParam(MYBATIS_DAO_DATABASES_TXT, "Postgres\nOracle\nMysql\nMssql\nDb2");
    projectParam(REGISTER_IMPL_TEST_PACKAGE_TXT, "sandbox.register/test_src kz/greetgo/sandbox/register/impl");
    projectParam(REGISTER_IMPL_TEST_BEFORE_CLASS_TXT, "@~kz.greetgo.depinject.testng.ContainerConfig~" +
      "(~kz.greetgo.sandbox.register.test.util.BeanConfigTests~.class)\n" +
      "@~java.lang.SuppressWarnings~(\"asd\")\n");
    projectParam(REGISTER_IMPL_TEST_EXTENDS_TXT, "kz.greetgo.super_pup_er.TestParent");

    projectParam(MYBATIS_TEST_DAO_PACKAGE_TXT, "sandbox.register/test_src kz/greetgo/sandbox/register/test/beans/dao");

    Project project = Project.openProject(rootProjectDir);

    CommandNewControllerApplier a = new CommandNewControllerApplier(project, "HelloWorld");

    a.execute();

    ex("sandbox.controller/src/kz/greetgo/sandbox/controller/controller/HelloWorldController.java");
    ex("sandbox.controller/src/kz/greetgo/sandbox/controller/register/HelloWorldRegister.java");
    ex("sandbox.register/src/kz/greetgo/sandbox/register/impl/HelloWorldRegisterImpl.java");
    ex("sandbox.register/src/kz/greetgo/sandbox/register/dao/HelloWorldDao.java");
    ex("sandbox.register/src/kz/greetgo/sandbox/register/dao/postgres/HelloWorldDaoPostgres.java");
    ex("sandbox.register/src/kz/greetgo/sandbox/register/dao/oracle/HelloWorldDaoOracle.java");
    ex("sandbox.register/src/kz/greetgo/sandbox/register/dao/mysql/HelloWorldDaoMysql.java");
    ex("sandbox.register/src/kz/greetgo/sandbox/register/dao/mssql/HelloWorldDaoMssql.java");
    ex("sandbox.register/src/kz/greetgo/sandbox/register/dao/db2/HelloWorldDaoDb2.java");
    ex("sandbox.register/test_src/kz/greetgo/sandbox/register/impl/HelloWorldRegisterImplTest.java");
    ex("sandbox.register/test_src/kz/greetgo/sandbox/register/test/beans/dao/HelloWorldTestDao.java");
    ex("sandbox.register/test_src/kz/greetgo/sandbox/register/test/beans/dao/postgres/HelloWorldTestDaoPostgres.java");
    ex("sandbox.register/test_src/kz/greetgo/sandbox/register/test/beans/dao/oracle/HelloWorldTestDaoOracle.java");
    ex("sandbox.register/test_src/kz/greetgo/sandbox/register/test/beans/dao/mysql/HelloWorldTestDaoMysql.java");
    ex("sandbox.register/test_src/kz/greetgo/sandbox/register/test/beans/dao/mssql/HelloWorldTestDaoMssql.java");
    ex("sandbox.register/test_src/kz/greetgo/sandbox/register/test/beans/dao/db2/HelloWorldTestDaoDb2.java");
  }

  private void ex(String fileSubPath) {
    assertThat(rootProjectDir.resolve(fileSubPath).toFile())
      .describedAs("MUST exists file:\n    file=" + fileSubPath + ",\n    rootProjectDir = " + rootProjectDir + "\n")
      .exists();
  }


  @Test
  public void execute_subPackage() throws Exception {
    projectParam(PROJECT_NAME_TXT, projectName);
    projectParam(CONTROLLER_PACKAGE_TXT, "sandbox.controller/src kz/greetgo/sandbox/controller/controller");
    projectParam(REGISTER_INTERFACE_PACKAGE_TXT, "sandbox.controller/src kz/greetgo/sandbox/controller/register");
    projectParam(CONTROLLER_MARKER_INTERFACE_TXT, TestControllerMarker.class.getName());
    projectParam(REGISTER_IMPL_PACKAGE_TXT, "sandbox.register/src kz/greetgo/sandbox/register/impl");
    projectParam(MYBATIS_DAO_PACKAGE_TXT, "sandbox.register/src kz/greetgo/sandbox/register/dao");
    projectParam(MYBATIS_DAO_DATABASES_TXT, "Postgres\nOracle\nMysql\nMssql\nDb2");
    projectParam(REGISTER_IMPL_TEST_PACKAGE_TXT, "sandbox.register/test_src kz/greetgo/sandbox/register/impl");
    projectParam(REGISTER_IMPL_TEST_BEFORE_CLASS_TXT, "@~kz.greetgo.depinject.testng.ContainerConfig~" +
      "(~kz.greetgo.sandbox.register.test.util.BeanConfigTests~.class)\n" +
      "@~java.lang.SuppressWarnings~(\"asd\")\n");
    projectParam(REGISTER_IMPL_TEST_EXTENDS_TXT, "kz.greetgo.super_pup_er.TestParent");

    projectParam(MYBATIS_TEST_DAO_PACKAGE_TXT, "sandbox.register/test_src kz/greetgo/sandbox/register/test/beans/dao");

    Project project = Project.openProject(rootProjectDir);

    CommandNewControllerApplier a = new CommandNewControllerApplier(project, "some.sub.pack.HelloWorld");

    a.execute();

    ex("sandbox.controller/src/kz/greetgo/sandbox/controller/controller/some/sub/pack/HelloWorldController.java");
    ex("sandbox.controller/src/kz/greetgo/sandbox/controller/register/some/sub/pack/HelloWorldRegister.java");
    ex("sandbox.register/src/kz/greetgo/sandbox/register/impl/some/sub/pack/HelloWorldRegisterImpl.java");
    ex("sandbox.register/src/kz/greetgo/sandbox/register/dao/some/sub/pack/HelloWorldDao.java");
    ex("sandbox.register/src/kz/greetgo/sandbox/register/dao/some/sub/pack/postgres/HelloWorldDaoPostgres.java");
    ex("sandbox.register/src/kz/greetgo/sandbox/register/dao/some/sub/pack/oracle/HelloWorldDaoOracle.java");
    ex("sandbox.register/src/kz/greetgo/sandbox/register/dao/some/sub/pack/mysql/HelloWorldDaoMysql.java");
    ex("sandbox.register/src/kz/greetgo/sandbox/register/dao/some/sub/pack/mssql/HelloWorldDaoMssql.java");
    ex("sandbox.register/src/kz/greetgo/sandbox/register/dao/some/sub/pack/db2/HelloWorldDaoDb2.java");
    ex("sandbox.register/test_src/kz/greetgo/sandbox/register/impl/some/sub/pack/HelloWorldRegisterImplTest.java");
    ex("sandbox.register/test_src/kz/greetgo/sandbox/register/test/beans/dao/some/sub/pack/HelloWorldTestDao.java");
    ex("sandbox.register/test_src/kz/greetgo/sandbox/register/test/beans/dao/some/sub/pack/postgres/HelloWorldTestDaoPostgres.java");
    ex("sandbox.register/test_src/kz/greetgo/sandbox/register/test/beans/dao/some/sub/pack/oracle/HelloWorldTestDaoOracle.java");
    ex("sandbox.register/test_src/kz/greetgo/sandbox/register/test/beans/dao/some/sub/pack/mysql/HelloWorldTestDaoMysql.java");
    ex("sandbox.register/test_src/kz/greetgo/sandbox/register/test/beans/dao/some/sub/pack/mssql/HelloWorldTestDaoMssql.java");
    ex("sandbox.register/test_src/kz/greetgo/sandbox/register/test/beans/dao/some/sub/pack/db2/HelloWorldTestDaoDb2.java");
  }

  @Test(expectedExceptions = No_CONTROLLER_PACKAGE_TXT.class)
  public void execute_no_CONTROLLER_PACKAGE_TXT() throws Exception {
    //    projectParam(CONTROLLER_PACKAGE_TXT, "sandbox.controller/src kz/greetgo/sandbox/controller/controller");
    projectParam(REGISTER_INTERFACE_PACKAGE_TXT, "sandbox.controller/src kz/greetgo/sandbox/controller/register");
    projectParam(CONTROLLER_MARKER_INTERFACE_TXT, TestControllerMarker.class.getName());
    projectParam(REGISTER_IMPL_PACKAGE_TXT, "sandbox.register/src kz/greetgo/sandbox/register/impl");
    projectParam(MYBATIS_DAO_PACKAGE_TXT, "sandbox.register/src kz/greetgo/sandbox/register/dao");
    projectParam(MYBATIS_DAO_DATABASES_TXT, "Postgres\nOracle\nMysql\nMssql\nDb2");
    projectParam(REGISTER_IMPL_TEST_PACKAGE_TXT, "sandbox.register/test_src kz/greetgo/sandbox/register/impl");
    projectParam(REGISTER_IMPL_TEST_BEFORE_CLASS_TXT, "@~kz.greetgo.depinject.testng.ContainerConfig~" +
      "(~kz.greetgo.sandbox.register.test.util.BeanConfigTests~.class)\n" +
      "@~java.lang.SuppressWarnings~(\"asd\")\n");
    projectParam(REGISTER_IMPL_TEST_EXTENDS_TXT, "kz.greetgo.super_pup_er.TestParent");

    projectParam(MYBATIS_TEST_DAO_PACKAGE_TXT, "sandbox.register/test_src kz/greetgo/sandbox/register/test/beans/dao");

    Project project = Project.openProject(rootProjectDir);

    CommandNewControllerApplier a = new CommandNewControllerApplier(project, "HelloWorld");

    a.execute();
  }

  @Test(expectedExceptions = No_REGISTER_INTERFACE_PACKAGE_TXT.class)
  public void execute_no_REGISTER_INTERFACE_PACKAGE_TXT() throws Exception {
    projectParam(CONTROLLER_PACKAGE_TXT, "sandbox.controller/src kz/greetgo/sandbox/controller/controller");
    //    projectParam(REGISTER_INTERFACE_PACKAGE_TXT, "sandbox.controller/src kz/greetgo/sandbox/controller/register");
    projectParam(CONTROLLER_MARKER_INTERFACE_TXT, TestControllerMarker.class.getName());
    projectParam(REGISTER_IMPL_PACKAGE_TXT, "sandbox.register/src kz/greetgo/sandbox/register/impl");
    projectParam(MYBATIS_DAO_PACKAGE_TXT, "sandbox.register/src kz/greetgo/sandbox/register/dao");
    projectParam(MYBATIS_DAO_DATABASES_TXT, "Postgres\nOracle\nMysql\nMssql\nDb2");
    projectParam(REGISTER_IMPL_TEST_PACKAGE_TXT, "sandbox.register/test_src kz/greetgo/sandbox/register/impl");
    projectParam(REGISTER_IMPL_TEST_BEFORE_CLASS_TXT, "@~kz.greetgo.depinject.testng.ContainerConfig~" +
      "(~kz.greetgo.sandbox.register.test.util.BeanConfigTests~.class)\n" +
      "@~java.lang.SuppressWarnings~(\"asd\")\n");
    projectParam(REGISTER_IMPL_TEST_EXTENDS_TXT, "kz.greetgo.super_pup_er.TestParent");

    projectParam(MYBATIS_TEST_DAO_PACKAGE_TXT, "sandbox.register/test_src kz/greetgo/sandbox/register/test/beans/dao");

    Project project = Project.openProject(rootProjectDir);

    CommandNewControllerApplier a = new CommandNewControllerApplier(project, "HelloWorld");

    a.execute();
  }

  @Test(expectedExceptions = No_CONTROLLER_MARKER_INTERFACE_TXT.class)
  public void execute_no_CONTROLLER_MARKER_INTERFACE_TXT() throws Exception {
    projectParam(CONTROLLER_PACKAGE_TXT, "sandbox.controller/src kz/greetgo/sandbox/controller/controller");
    projectParam(REGISTER_INTERFACE_PACKAGE_TXT, "sandbox.controller/src kz/greetgo/sandbox/controller/register");
    //    projectParam(CONTROLLER_MARKER_INTERFACE_TXT, TestControllerMarker.class.getName());
    projectParam(REGISTER_IMPL_PACKAGE_TXT, "sandbox.register/src kz/greetgo/sandbox/register/impl");
    projectParam(MYBATIS_DAO_PACKAGE_TXT, "sandbox.register/src kz/greetgo/sandbox/register/dao");
    projectParam(MYBATIS_DAO_DATABASES_TXT, "Postgres\nOracle\nMysql\nMssql\nDb2");
    projectParam(REGISTER_IMPL_TEST_PACKAGE_TXT, "sandbox.register/test_src kz/greetgo/sandbox/register/impl");
    projectParam(REGISTER_IMPL_TEST_BEFORE_CLASS_TXT, "@~kz.greetgo.depinject.testng.ContainerConfig~" +
      "(~kz.greetgo.sandbox.register.test.util.BeanConfigTests~.class)\n" +
      "@~java.lang.SuppressWarnings~(\"asd\")\n");
    projectParam(REGISTER_IMPL_TEST_EXTENDS_TXT, "kz.greetgo.super_pup_er.TestParent");

    projectParam(MYBATIS_TEST_DAO_PACKAGE_TXT, "sandbox.register/test_src kz/greetgo/sandbox/register/test/beans/dao");

    Project project = Project.openProject(rootProjectDir);

    CommandNewControllerApplier a = new CommandNewControllerApplier(project, "HelloWorld");

    a.execute();
  }

  @Test(expectedExceptions = No_REGISTER_IMPL_PACKAGE_TXT.class)
  public void execute_no_REGISTER_IMPL_PACKAGE_TXT() throws Exception {
    projectParam(CONTROLLER_PACKAGE_TXT, "sandbox.controller/src kz/greetgo/sandbox/controller/controller");
    projectParam(REGISTER_INTERFACE_PACKAGE_TXT, "sandbox.controller/src kz/greetgo/sandbox/controller/register");
    projectParam(CONTROLLER_MARKER_INTERFACE_TXT, TestControllerMarker.class.getName());
    //    projectParam(REGISTER_IMPL_PACKAGE_TXT, "sandbox.register/src kz/greetgo/sandbox/register/impl");
    projectParam(MYBATIS_DAO_PACKAGE_TXT, "sandbox.register/src kz/greetgo/sandbox/register/dao");
    projectParam(MYBATIS_DAO_DATABASES_TXT, "Postgres\nOracle\nMysql\nMssql\nDb2");
    projectParam(REGISTER_IMPL_TEST_PACKAGE_TXT, "sandbox.register/test_src kz/greetgo/sandbox/register/impl");
    projectParam(REGISTER_IMPL_TEST_BEFORE_CLASS_TXT, "@~kz.greetgo.depinject.testng.ContainerConfig~" +
      "(~kz.greetgo.sandbox.register.test.util.BeanConfigTests~.class)\n" +
      "@~java.lang.SuppressWarnings~(\"asd\")\n");
    projectParam(REGISTER_IMPL_TEST_EXTENDS_TXT, "kz.greetgo.super_pup_er.TestParent");

    projectParam(MYBATIS_TEST_DAO_PACKAGE_TXT, "sandbox.register/test_src kz/greetgo/sandbox/register/test/beans/dao");

    Project project = Project.openProject(rootProjectDir);

    CommandNewControllerApplier a = new CommandNewControllerApplier(project, "HelloWorld");

    a.execute();
  }

  @Test(expectedExceptions = No_MYBATIS_DAO_PACKAGE_TXT.class)
  public void execute_no_MYBATIS_DAO_PACKAGE_TXT() throws Exception {
    projectParam(CONTROLLER_PACKAGE_TXT, "sandbox.controller/src kz/greetgo/sandbox/controller/controller");
    projectParam(REGISTER_INTERFACE_PACKAGE_TXT, "sandbox.controller/src kz/greetgo/sandbox/controller/register");
    projectParam(CONTROLLER_MARKER_INTERFACE_TXT, TestControllerMarker.class.getName());
    projectParam(REGISTER_IMPL_PACKAGE_TXT, "sandbox.register/src kz/greetgo/sandbox/register/impl");
    //    projectParam(MYBATIS_DAO_PACKAGE_TXT, "sandbox.register/src kz/greetgo/sandbox/register/dao");
    projectParam(MYBATIS_DAO_DATABASES_TXT, "Postgres\nOracle\nMysql\nMssql\nDb2");
    projectParam(REGISTER_IMPL_TEST_PACKAGE_TXT, "sandbox.register/test_src kz/greetgo/sandbox/register/impl");
    projectParam(REGISTER_IMPL_TEST_BEFORE_CLASS_TXT, "@~kz.greetgo.depinject.testng.ContainerConfig~" +
      "(~kz.greetgo.sandbox.register.test.util.BeanConfigTests~.class)\n" +
      "@~java.lang.SuppressWarnings~(\"asd\")\n");
    projectParam(REGISTER_IMPL_TEST_EXTENDS_TXT, "kz.greetgo.super_pup_er.TestParent");

    projectParam(MYBATIS_TEST_DAO_PACKAGE_TXT, "sandbox.register/test_src kz/greetgo/sandbox/register/test/beans/dao");

    Project project = Project.openProject(rootProjectDir);

    CommandNewControllerApplier a = new CommandNewControllerApplier(project, "HelloWorld");

    a.execute();
  }

  @Test(expectedExceptions = No_MYBATIS_DAO_DATABASES_TXT.class)
  public void execute_no_MYBATIS_DAO_DATABASES_TXT() throws Exception {
    projectParam(CONTROLLER_PACKAGE_TXT, "sandbox.controller/src kz/greetgo/sandbox/controller/controller");
    projectParam(REGISTER_INTERFACE_PACKAGE_TXT, "sandbox.controller/src kz/greetgo/sandbox/controller/register");
    projectParam(CONTROLLER_MARKER_INTERFACE_TXT, TestControllerMarker.class.getName());
    projectParam(REGISTER_IMPL_PACKAGE_TXT, "sandbox.register/src kz/greetgo/sandbox/register/impl");
    projectParam(MYBATIS_DAO_PACKAGE_TXT, "sandbox.register/src kz/greetgo/sandbox/register/dao");
    //    projectParam(MYBATIS_DAO_DATABASES_TXT, "Postgres\nOracle\nMysql\nMssql\nDb2");
    projectParam(REGISTER_IMPL_TEST_PACKAGE_TXT, "sandbox.register/test_src kz/greetgo/sandbox/register/impl");
    projectParam(REGISTER_IMPL_TEST_BEFORE_CLASS_TXT, "@~kz.greetgo.depinject.testng.ContainerConfig~" +
      "(~kz.greetgo.sandbox.register.test.util.BeanConfigTests~.class)\n" +
      "@~java.lang.SuppressWarnings~(\"asd\")\n");
    projectParam(REGISTER_IMPL_TEST_EXTENDS_TXT, "kz.greetgo.super_pup_er.TestParent");

    projectParam(MYBATIS_TEST_DAO_PACKAGE_TXT, "sandbox.register/test_src kz/greetgo/sandbox/register/test/beans/dao");

    Project project = Project.openProject(rootProjectDir);

    CommandNewControllerApplier a = new CommandNewControllerApplier(project, "HelloWorld");

    a.execute();
  }

  @Test(expectedExceptions = No_REGISTER_IMPL_TEST_PACKAGE_TXT.class)
  public void execute_no_REGISTER_IMPL_TEST_PACKAGE_TXT() throws Exception {
    projectParam(CONTROLLER_PACKAGE_TXT, "sandbox.controller/src kz/greetgo/sandbox/controller/controller");
    projectParam(REGISTER_INTERFACE_PACKAGE_TXT, "sandbox.controller/src kz/greetgo/sandbox/controller/register");
    projectParam(CONTROLLER_MARKER_INTERFACE_TXT, TestControllerMarker.class.getName());
    projectParam(REGISTER_IMPL_PACKAGE_TXT, "sandbox.register/src kz/greetgo/sandbox/register/impl");
    projectParam(MYBATIS_DAO_PACKAGE_TXT, "sandbox.register/src kz/greetgo/sandbox/register/dao");
    projectParam(MYBATIS_DAO_DATABASES_TXT, "Postgres\nOracle\nMysql\nMssql\nDb2");
    //    projectParam(REGISTER_IMPL_TEST_PACKAGE_TXT, "sandbox.register/test_src kz/greetgo/sandbox/register/impl");
    projectParam(REGISTER_IMPL_TEST_BEFORE_CLASS_TXT, "@~kz.greetgo.depinject.testng.ContainerConfig~" +
      "(~kz.greetgo.sandbox.register.test.util.BeanConfigTests~.class)\n" +
      "@~java.lang.SuppressWarnings~(\"asd\")\n");
    projectParam(REGISTER_IMPL_TEST_EXTENDS_TXT, "kz.greetgo.super_pup_er.TestParent");

    projectParam(MYBATIS_TEST_DAO_PACKAGE_TXT, "sandbox.register/test_src kz/greetgo/sandbox/register/test/beans/dao");

    Project project = Project.openProject(rootProjectDir);

    CommandNewControllerApplier a = new CommandNewControllerApplier(project, "HelloWorld");

    a.execute();
  }

  @Test(expectedExceptions = No_REGISTER_IMPL_TEST_BEFORE_CLASS_TXT.class)
  public void execute_no_REGISTER_IMPL_TEST_BEFORE_CLASS_TXT() throws Exception {
    projectParam(CONTROLLER_PACKAGE_TXT, "sandbox.controller/src kz/greetgo/sandbox/controller/controller");
    projectParam(REGISTER_INTERFACE_PACKAGE_TXT, "sandbox.controller/src kz/greetgo/sandbox/controller/register");
    projectParam(CONTROLLER_MARKER_INTERFACE_TXT, TestControllerMarker.class.getName());
    projectParam(REGISTER_IMPL_PACKAGE_TXT, "sandbox.register/src kz/greetgo/sandbox/register/impl");
    projectParam(MYBATIS_DAO_PACKAGE_TXT, "sandbox.register/src kz/greetgo/sandbox/register/dao");
    projectParam(MYBATIS_DAO_DATABASES_TXT, "Postgres\nOracle\nMysql\nMssql\nDb2");
    projectParam(REGISTER_IMPL_TEST_PACKAGE_TXT, "sandbox.register/test_src kz/greetgo/sandbox/register/impl");
    //    projectParam(REGISTER_IMPL_TEST_BEFORE_CLASS_TXT, "@~kz.greetgo.depinject.testng.ContainerConfig~" +
    //      "(~kz.greetgo.sandbox.register.test.util.BeanConfigTests~.class)\n" +
    //      "@~java.lang.SuppressWarnings~(\"asd\")\n");
    projectParam(REGISTER_IMPL_TEST_EXTENDS_TXT, "kz.greetgo.super_pup_er.TestParent");

    projectParam(MYBATIS_TEST_DAO_PACKAGE_TXT, "sandbox.register/test_src kz/greetgo/sandbox/register/test/beans/dao");

    Project project = Project.openProject(rootProjectDir);

    CommandNewControllerApplier a = new CommandNewControllerApplier(project, "HelloWorld");

    a.execute();
  }

  @Test(expectedExceptions = No_REGISTER_IMPL_TEST_EXTENDS_TXT.class)
  public void execute_no_REGISTER_IMPL_TEST_EXTENDS_TXT() throws Exception {
    projectParam(CONTROLLER_PACKAGE_TXT, "sandbox.controller/src kz/greetgo/sandbox/controller/controller");
    projectParam(REGISTER_INTERFACE_PACKAGE_TXT, "sandbox.controller/src kz/greetgo/sandbox/controller/register");
    projectParam(CONTROLLER_MARKER_INTERFACE_TXT, TestControllerMarker.class.getName());
    projectParam(REGISTER_IMPL_PACKAGE_TXT, "sandbox.register/src kz/greetgo/sandbox/register/impl");
    projectParam(MYBATIS_DAO_PACKAGE_TXT, "sandbox.register/src kz/greetgo/sandbox/register/dao");
    projectParam(MYBATIS_DAO_DATABASES_TXT, "Postgres\nOracle\nMysql\nMssql\nDb2");
    projectParam(REGISTER_IMPL_TEST_PACKAGE_TXT, "sandbox.register/test_src kz/greetgo/sandbox/register/impl");
    projectParam(REGISTER_IMPL_TEST_BEFORE_CLASS_TXT, "@~kz.greetgo.depinject.testng.ContainerConfig~" +
      "(~kz.greetgo.sandbox.register.test.util.BeanConfigTests~.class)\n" +
      "@~java.lang.SuppressWarnings~(\"asd\")\n");
    //    projectParam(REGISTER_IMPL_TEST_EXTENDS_TXT, "kz.greetgo.super_pup_er.TestParent");

    projectParam(MYBATIS_TEST_DAO_PACKAGE_TXT, "sandbox.register/test_src kz/greetgo/sandbox/register/test/beans/dao");

    Project project = Project.openProject(rootProjectDir);

    CommandNewControllerApplier a = new CommandNewControllerApplier(project, "HelloWorld");

    a.execute();
  }

  @Test(expectedExceptions = No_MYBATIS_TEST_DAO_PACKAGE_TXT.class)
  public void execute_no_MYBATIS_TEST_DAO_PACKAGE_TXT() throws Exception {
    projectParam(CONTROLLER_PACKAGE_TXT, "sandbox.controller/src kz/greetgo/sandbox/controller/controller");
    projectParam(REGISTER_INTERFACE_PACKAGE_TXT, "sandbox.controller/src kz/greetgo/sandbox/controller/register");
    projectParam(CONTROLLER_MARKER_INTERFACE_TXT, TestControllerMarker.class.getName());
    projectParam(REGISTER_IMPL_PACKAGE_TXT, "sandbox.register/src kz/greetgo/sandbox/register/impl");
    projectParam(MYBATIS_DAO_PACKAGE_TXT, "sandbox.register/src kz/greetgo/sandbox/register/dao");
    projectParam(MYBATIS_DAO_DATABASES_TXT, "Postgres\nOracle\nMysql\nMssql\nDb2");
    projectParam(REGISTER_IMPL_TEST_PACKAGE_TXT, "sandbox.register/test_src kz/greetgo/sandbox/register/impl");
    projectParam(REGISTER_IMPL_TEST_BEFORE_CLASS_TXT, "@~kz.greetgo.depinject.testng.ContainerConfig~" +
      "(~kz.greetgo.sandbox.register.test.util.BeanConfigTests~.class)\n" +
      "@~java.lang.SuppressWarnings~(\"asd\")\n");
    projectParam(REGISTER_IMPL_TEST_EXTENDS_TXT, "kz.greetgo.super_pup_er.TestParent");
    //    projectParam(MYBATIS_TEST_DAO_PACKAGE_TXT, "sandbox.register/test_src kz/greetgo/sandbox/register/test/beans/dao");

    Project project = Project.openProject(rootProjectDir);

    CommandNewControllerApplier a = new CommandNewControllerApplier(project, "HelloWorld");

    a.execute();
  }
}