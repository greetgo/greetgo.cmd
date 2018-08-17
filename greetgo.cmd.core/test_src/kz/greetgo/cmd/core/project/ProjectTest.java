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
import static kz.greetgo.cmd.core.util.PathUtil.DOT_GREETGO;
import static kz.greetgo.cmd.core.util.PathUtil.PROJECT_NAME_TXT;
import static org.fest.assertions.api.Assertions.assertThat;

public class ProjectTest {

  Path rootProjectDir;
  String projectName;

  @BeforeMethod
  public void createTestProject() throws Exception {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

    projectName = RND.str(10);

    rootProjectDir = Paths.get("build")
      .resolve("tests_data")
      .resolve("ProjectTest")
      .resolve("project-" + sdf.format(new Date()) + "-" + RND.intStr(4));

    Path projectFileName = rootProjectDir.resolve(DOT_GREETGO).resolve(PROJECT_NAME_TXT);
    projectFileName.toFile().getParentFile().mkdirs();
    Files.write(projectFileName, projectName.getBytes(UTF_8));
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
}