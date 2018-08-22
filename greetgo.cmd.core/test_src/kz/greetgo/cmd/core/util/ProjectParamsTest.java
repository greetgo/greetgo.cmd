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

public class ProjectParamsTest {

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
    ReadResult actualValue = ProjectParams.readValue(filePath);
    //
    //

    assertThat(actualValue.get()).isEqualTo(value);
  }

  @Test
  public void readParamFile_emptyFile() throws Exception {
    Path filePath = rootDir.resolve("a-file.txt");
    filePath.toFile().getParentFile().mkdirs();
    filePath.toFile().createNewFile();

    //
    //
    ReadResult actualValue = ProjectParams.readValue(filePath);
    //
    //

    assertThat(actualValue.isFileAbsent()).isFalse();
    assertThat(actualValue.isValueAbsent()).isTrue();
  }

  @Test
  public void readParamFile_noFile() {
    //
    //
    ReadResult actualValue = ProjectParams.readValue(Paths.get(RND.str(10)));
    //
    //

    assertThat(actualValue.isFileAbsent()).isTrue();
    assertThat(actualValue.isValueAbsent()).isTrue();
  }
}
