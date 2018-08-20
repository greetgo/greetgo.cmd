package kz.greetgo.cmd.client.command.new_controller;

import kz.greetgo.cmd.client.test_data.TestControllerMarker;
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

    Project project = Project.openProject(rootProjectDir);

    CommandNewControllerApplier a = new CommandNewControllerApplier(project, "Test");

    a.execute();
  }
}