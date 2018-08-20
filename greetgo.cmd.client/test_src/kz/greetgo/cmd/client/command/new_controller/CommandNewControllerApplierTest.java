package kz.greetgo.cmd.client.command.new_controller;

import kz.greetgo.cmd.client.test_data.TestControllerMarker;
import kz.greetgo.cmd.core.project.Project;
import kz.greetgo.util.RND;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import static java.nio.charset.StandardCharsets.UTF_8;
import static kz.greetgo.cmd.core.util.PathUtil.*;

public class CommandNewControllerApplierTest {

  Path rootProjectDir;
  String projectName;

  @BeforeMethod
  public void createTestProject() throws Exception {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

    projectName = "prj" + RND.intStr(10);

    rootProjectDir = Paths.get("build")
      .resolve("tests_data")
      .resolve(CommandNewControllerApplierTest.class.getSimpleName())
      .resolve("project-" + sdf.format(new Date()) + "-" + RND.intStr(4));

    projectParam(PROJECT_NAME_TXT, projectName);
    projectParam(CONTROLLER_PACKAGE_TXT, "sandbox.controller/src kz/greetgo/sandbox/controller");
    projectParam(REGISTER_INTERFACE_PACKAGE_TXT, "sandbox.controller/src kz/greetgo/sandbox/register");
    projectParam(CONTROLLER_MARKER_INTERFACE_TXT, TestControllerMarker.class.getName());
  }

  private void projectParam(String fileName, String value) throws Exception {
    Path projectFileName = rootProjectDir.resolve(DOT_GREETGO).resolve(fileName);
    projectFileName.toFile().getParentFile().mkdirs();
    Files.write(projectFileName, value.getBytes(UTF_8));
  }

  @Test
  public void testName() {
    Project project = Project.openProject(rootProjectDir);

    CommandNewControllerApplier a = new CommandNewControllerApplier(project, "Test");

    a.execute();
  }
}