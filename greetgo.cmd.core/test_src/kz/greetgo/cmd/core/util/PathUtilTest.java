package kz.greetgo.cmd.core.util;

import kz.greetgo.cmd.core.errors.CannotFindDirWithFile;
import kz.greetgo.util.RND;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.fest.assertions.api.Assertions.assertThat;

public class PathUtilTest {

  Path baseDir;

  @BeforeMethod
  public void createBaseDir() {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

    baseDir = Paths.get("build")
      .resolve("tests_data")
      .resolve("PathUtilTest")
      .resolve("t-" + sdf.format(new Date()) + "-" + RND.intStr(10));
  }

  @Test
  public void findDirWithFile_ok() {

    Path level1 = baseDir.resolve("level1");

    level1.resolve("root").toFile().mkdirs();

    Path level2 = level1.resolve("level2");

    Path level3 = level2.resolve("level3");

    level3.toFile().mkdirs();

    //
    //
    Path parentOfRoot = PathUtil.findDirWithFile("root", level3);
    //
    //

    assertThat(parentOfRoot).isEqualTo(level1.toAbsolutePath());
  }

  @Test(expectedExceptions = CannotFindDirWithFile.class)
  public void findDirWithFile_absent() {

    Path level1 = baseDir.resolve("level1");

    Path level2 = level1.resolve("level2");

    Path level3 = level2.resolve("level3");

    level3.toFile().mkdirs();

    //
    //
    PathUtil.findDirWithFile(RND.str(10), level3);
    //
    //
  }
}