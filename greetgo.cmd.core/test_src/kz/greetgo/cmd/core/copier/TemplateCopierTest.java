package kz.greetgo.cmd.core.copier;

import kz.greetgo.util.RND;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.fest.assertions.api.Assertions.assertThat;

@SuppressWarnings("OptionalGetWithoutIsPresent")
public class TemplateCopierTest {

  Path fromDir;
  Path toDir;

  @BeforeMethod
  public void createRootDir() {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
    String rnd = RND.intStr(4);
    Date now = new Date();

    Path testBase = Paths.get("build")
      .resolve("tests_data")
      .resolve("TemplateCopierTest");

    fromDir = testBase.resolve("t-" + sdf.format(now) + "-" + rnd + "-from");
    toDir = testBase.resolve("t-" + sdf.format(now) + "-" + rnd + "-to");

    toDir.toFile().mkdirs();
  }

  private void file(String fileName, String text) throws IOException {
    file(fileName, text.getBytes(UTF_8));
  }

  private void file(String fileName, byte[] bytes) throws IOException {
    Path path = fromDir.resolve(fileName);
    path.toFile().getParentFile().mkdirs();
    Files.write(path, bytes);
  }

  private Optional<String> to(String toFileName) throws IOException {
    return toBytes(toFileName).map(bytes -> new String(bytes, UTF_8));
  }

  private Optional<byte[]> toBytes(String toFileName) throws IOException {
    File file = toDir.resolve(toFileName).toFile();
    if (!file.exists()) { return Optional.empty(); }
    return Optional.of(Files.readAllBytes(file.toPath()));
  }

  @Test
  public void copy_testSkipping() throws Exception {

    file("hello.txt", "hello");
    file("dir/status.txt", "ok");
    file("dir/sub_dir/deep/deep_file.txt", "bool bool bool");
    file(".hidden/hidden_file.txt", "secret");
    file("sk1.modifier.txt", "skip=true");
    file("sk1/status1.txt", "hi1");
    file("sk2.modifier.txt", "skip=true");
    file("sk2/status2.txt", "hi2");

    file("dir/for_skip.txt.modifier.txt", "skip=yes");
    file("dir/for_skip.txt", "ok");

    TemplateCopier.of()
      .from(fromDir)
      .to(toDir)
      .copy()
    ;

    assertThat(to("hello.txt").get()).isEqualTo("hello");
    assertThat(to("dir/status.txt").get()).isEqualTo("ok");
    assertThat(to("dir/sub_dir/deep/deep_file.txt").get()).isEqualTo("bool bool bool");
    assertThat(to(".hidden/hidden_file.txt").get()).isEqualTo("secret");
    assertThat(to("sk1/status1.txt").isPresent()).describedAs("sk1/status1.txt must be skipped").isFalse();
    assertThat(to("sk2/status2.txt").isPresent()).describedAs("sk2/status2.txt must be skipped").isFalse();
    assertThat(to("sk1.modifier.txt").isPresent()).describedAs("sk1.modifier.txt must be skipped").isFalse();
    assertThat(to("sk2.modifier.txt").isPresent()).describedAs("sk2.modifier.txt must be skipped").isFalse();

    assertThat(to("dir/for_skip.txt.modifier.txt").isPresent())
      .describedAs("dir/for_skip.txt.modifier.txt must be skipped")
      .isFalse();
    assertThat(to("dir/for_skip.txt").isPresent())
      .describedAs("dir/for_skip.txt must be skipped")
      .isFalse();
  }

  @Test
  public void copy_renameDir() throws Exception {

    file("top_dir/dir/sub_dir/a_file.txt", "hello");
    file("top_dir/dir.modifier.txt", "rename-to={PROJECT_NAME}-hi");

    TemplateCopier.of()
      .from(fromDir)
      .to(toDir)
      .setVariable("PROJECT_NAME", "test_name")
      .copy()
    ;

    assertThat(to("top_dir/dir/sub_dir/a_file.txt").isPresent()).isFalse();
    assertThat(to("top_dir/test_name-hi/sub_dir/a_file.txt").isPresent()).isTrue();
    assertThat(to("top_dir/test_name-hi/sub_dir/a_file.txt").get()).isEqualTo("hello");
  }

  @Test
  public void copy_binaryCopy() throws Exception {

    byte[] bytes = RND.byteArray(1000);

    file("top_dir/dir/sub_dir/bin_file", bytes);

    TemplateCopier.of()
      .from(fromDir)
      .to(toDir)
      .copy()
    ;

    assertThat(toBytes("top_dir/dir/sub_dir/bin_file").get()).isEqualTo(bytes);
  }

  @Test
  public void copy_withReplaceIn() throws IOException {
    file("dir/file.txt", "\n"
      + "\n"
      + "///MODIFY replace saturn\\d+ {PROJECT_NAME}-name\n"
      + "It is saturn327 hello world\n");

    TemplateCopier.of()
      .from(fromDir)
      .to(toDir)
      .setVariable("PROJECT_NAME", "test-project")
      .copy()
    ;

    assertThat(to("dir/file.txt").get()).isEqualTo("\n"
      + "\n"
      + "It is test-project-name hello world\n");
  }

  @Test
  public void copy_withSomeReplaceIn() throws IOException {
    file("dir/file.txt", "\n"
      + "\n"
      + "///MODIFY replace saturn\\d+ {PROJECT_NAME}-name\n"
      + "///MODIFY replace hello {ASD}\n"
      + "It is saturn327 hello saturn11 world-hello\n");

    TemplateCopier.of()
      .from(fromDir)
      .to(toDir)
      .setVariable("PROJECT_NAME", "test-project")
      .setVariable("ASD", "moon")
      .copy()
    ;

    assertThat(to("dir/file.txt").get()).isEqualTo("\n"
      + "\n"
      + "It is test-project-name moon test-project-name world-moon\n");
  }


  @Test
  public void copy_makeTypeTxt() throws IOException {
    file("dir/file.with.unknown.extension.modifier.txt", "bin-status=txt");
    file("dir/file.with.unknown.extension", "\n"
      + "\n"
      + "///MODIFY replace saturn\\d+ {PROJECT_NAME}-name\n"
      + "///MODIFY replace hello {ASD}\n"
      + "It is saturn327 hello saturn11 world-hello\n");

    TemplateCopier.of()
      .from(fromDir)
      .to(toDir)
      .setVariable("PROJECT_NAME", "test-project")
      .setVariable("ASD", "moon")
      .copy()
    ;

    assertThat(to("dir/file.with.unknown.extension").get()).isEqualTo("\n"
      + "\n"
      + "It is test-project-name moon test-project-name world-moon\n");
  }
}
