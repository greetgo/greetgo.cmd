package kz.greetgo.cmd.core.util;

import kz.greetgo.util.RND;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.fest.assertions.api.Assertions.assertThat;

public class FileUtilTest {

  Path rootDir;

  @BeforeMethod
  public void setUp() {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

    rootDir = Paths.get("build")
      .resolve("tests_data")
      .resolve("FileUtilTest")
      .resolve("t-" + sdf.format(new Date()) + "-" + RND.intStr(4));
  }

  @Test
  public void readParamFile_ok() throws Exception {
    String value = RND.str(10);
    String data = "#some comment\n   \n" + value + "\n#another comment";
    Path filePath = rootDir.resolve("a-file.txt");
    filePath.toFile().getParentFile().mkdirs();
    Files.write(filePath, data.getBytes(UTF_8));

    //
    //
    String actualValue = FileUtil.readParamFile(filePath);
    //
    //

    assertThat(actualValue).isEqualTo(value);
  }

  @Test
  public void readParamFile_emptyFile() throws Exception {
    Path filePath = rootDir.resolve("a-file.txt");
    filePath.toFile().getParentFile().mkdirs();
    filePath.toFile().createNewFile();

    //
    //
    String actualValue = FileUtil.readParamFile(filePath);
    //
    //

    assertThat(actualValue).isEmpty();
  }

  @Test
  public void readParamFile_noFile() {
    //
    //
    String actualValue = FileUtil.readParamFile(Paths.get(RND.str(10)));
    //
    //

    assertThat(actualValue).isEmpty();
  }
}
