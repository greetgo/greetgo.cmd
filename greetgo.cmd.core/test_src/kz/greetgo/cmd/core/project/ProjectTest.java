package kz.greetgo.cmd.core.project;

import kz.greetgo.util.RND;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import static java.nio.charset.StandardCharsets.UTF_8;
import static kz.greetgo.cmd.core.util.ProjectParams.CONTROLLER_PACKAGE_TXT;
import static kz.greetgo.cmd.core.util.ProjectParams.DOT_GREETGO;
import static kz.greetgo.cmd.core.util.ProjectParams.PROJECT_NAME_TXT;
import static org.fest.assertions.api.Assertions.assertThat;

public class ProjectTest {

  Path rootProjectDir;
  String projectName;

  @BeforeMethod
  public void createTestProject() throws Exception {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

    projectName = "prj" + RND.intStr(10);

    rootProjectDir = Paths.get("build")
      .resolve("tests_data")
      .resolve("ProjectTest")
      .resolve("project-" + sdf.format(new Date()) + "-" + RND.intStr(4));


    projectParam(PROJECT_NAME_TXT, projectName);
    projectParam(CONTROLLER_PACKAGE_TXT, "sandbox.controller/src kz/greetgo/sandbox/controller");
  }

  private void projectParam(String fileName, String value) throws Exception {
    Path projectFileName = rootProjectDir.resolve(DOT_GREETGO).resolve(fileName);
    projectFileName.toFile().getParentFile().mkdirs();
    Files.write(projectFileName, value.getBytes(UTF_8));
  }

  @Test
  public void getName() {
    Project project = Project.openProject(rootProjectDir);

    //
    //
    String projectName = project.getName();
    //
    //

    assertThat(projectName).isEqualTo(this.projectName);
  }

  @Test
  public void getControllerPackageRef() {
    Project project = Project.openProject(rootProjectDir);

    //
    //
    String projectName = project.getName();
    //
    //

    assertThat(projectName).isEqualTo(this.projectName);
  }

  @Test
  public void printController() {
    Project project = Project.openProject(rootProjectDir);

    PackageRef controllerPackageRef = project.getControllerPackageRef();

    ClassRef controllerRef = controllerPackageRef.createClassRef("AsdController");

    controllerRef.save();
  }
}
