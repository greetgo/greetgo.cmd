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
  public void copy_withReplaceIn_startedWithSpaces() throws IOException {
    file("dir/file.txt", "\n"
        + "\n"
        + "   ///MODIFY replace saturn\\d+ {PROJECT_NAME}-name\n"
        + "It is saturn327 hello world\n");

    TemplateCopier.of()
        .from(fromDir)
        .to(toDir)
        .setVariable("PROJECT_NAME", "test-project")
        .copy()
    ;

    assertThat(to("dir/file.txt").get())
        .describedAs("Not works ///MODIFY with spaces in start of line")
        .isEqualTo(""
            + "\n"
            + "\n"
            + "It is test-project-name hello world\n");
  }

  @Test
  public void copy_withReplaceIn_startedInXmlComment() throws IOException {
    file("dir/file.txt", "\n"
        + "\n"
        + "  <!-- ///MODIFY replace saturn\\d+ {PROJECT_NAME}-name  -->\n"
        + "It is saturn327 hello world\n");

    TemplateCopier.of()
        .from(fromDir)
        .to(toDir)
        .setVariable("PROJECT_NAME", "test-project")
        .copy()
    ;

    assertThat(to("dir/file.txt").get())
        .describedAs("Not works ///MODIFY in xml comment")
        .isEqualTo(""
            + "\n"
            + "\n"
            + "It is test-project-name hello world\n");
  }

  @Test
  public void copy_withReplaceIn_startedInXmlComment2() throws IOException {
    file("dir/file.txt", "\n"
        + "\n"
        + "  <!-- ///MODIFY replace sandbox {PROJECT_NAME}  --> \n"
        + "  <param name=\"File\" value=\"${user.home}/sandbox.d/logs/server.log\"/>\n" +
        "Hello world\n");

    TemplateCopier.of()
        .from(fromDir)
        .to(toDir)
        .setVariable("PROJECT_NAME", "Stone_In_the_World")
        .copy()
    ;

//    System.out.println(to("dir/file.txt").get());

    assertThat(to("dir/file.txt").get())
        .describedAs("Not works ///MODIFY in xml comment")
        .isEqualTo(""
            + "\n"
            + "\n"
            + "  <param name=\"File\" value=\"${user.home}/Stone_In_the_World.d/logs/server.log\"/>\n" +
            "Hello world\n");
  }


  @Test
  public void copy_withReplaceIn_startedInXmlComment3() throws IOException {
    String content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
        "<!DOCTYPE log4j:configuration SYSTEM \"log4j.dtd\">\n" +
        "\n" +
        "<log4j:configuration xmlns:log4j=\"http://jakarta.apache.org/log4j/\" debug=\"false\">\n" +
        "  <appender name=\"CONSOLE\" class=\"org.apache.log4j.ConsoleAppender\">\n" +
        "    <errorHandler class=\"org.apache.log4j.helpers.OnlyOnceErrorHandler\"/>\n" +
        "    <param name=\"Target\" value=\"System.out\"/>\n" +
        "    <param name=\"Threshold\" value=\"TRACE\"/>\n" +
        "    <!-- ///MODIFY replace sandbox {PROJECT_NAME} -->\n" +
        "    <layout class=\"kz.greetgo.sandbox.controller.logging.MyLog4jLayout\">\n" +
        "      <param name=\"LoggerNameCut\" value=\"2\"/>\n" +
        "    </layout>\n" +
        "    <!--\n" +
        "    <layout class=\"org.apache.log4j.PatternLayout\">\n" +
        "      <param name=\"ConversionPattern\" value=\"%d %-5p [%c{1}] %m%n\"/>\n" +
        "    </layout>\n" +
        "    -->\n" +
        "  </appender>\n" +
        "  <appender name=\"SERVER\" class=\"org.apache.log4j.RollingFileAppender\">\n" +
        "    <!-- ///MODIFY replace sandbox {PROJECT_NAME} -->\n" +
        "    <param name=\"File\" value=\"${user.home}/sandbox.d/logs/server.log\"/>\n" +
        "    <param name=\"Threshold\" value=\"INFO\"/>\n" +
        "    <param name=\"Append\" value=\"true\"/>\n" +
        "    <param name=\"MaxFileSize\" value=\"100MB\"/>\n" +
        "    <param name=\"MaxBackupIndex\" value=\"100\"/>\n" +
        "    <!-- ///MODIFY replace sandbox {PROJECT_NAME} -->\n" +
        "    <layout class=\"kz.greetgo.sandbox.controller.logging.MyLog4jLayout\">\n" +
        "      <param name=\"LoggerNameCut\" value=\"0\"/>\n" +
        "    </layout>\n" +
        "  </appender>\n" +
        "\n" +
        "  <appender name=\"DIRECT_SQL\" class=\"org.apache.log4j.RollingFileAppender\">\n" +
        "    <!-- ///MODIFY replace sandbox {PROJECT_NAME} -->\n" +
        "    <param name=\"File\" value=\"${user.home}/sandbox.d/logs/DIRECT_SQL.log\"/>\n" +
        "    <param name=\"Threshold\" value=\"INFO\"/>\n" +
        "    <param name=\"Append\" value=\"true\"/>\n" +
        "    <param name=\"MaxFileSize\" value=\"100MB\"/>\n" +
        "    <param name=\"MaxBackupIndex\" value=\"100\"/>\n" +
        "    <!-- ///MODIFY replace sandbox {PROJECT_NAME} -->\n" +
        "    <layout class=\"kz.greetgo.sandbox.controller.logging.MyLog4jLayout\">\n" +
        "      <param name=\"LoggerNameCut\" value=\"0\"/>\n" +
        "    </layout>\n" +
        "  </appender>\n" +
        "\n" +
        "  <category name=\"org.apache\">\n" +
        "    <priority value=\"INFO\"/>\n" +
        "  </category>\n" +
        "  <!-- ///MODIFY replace sandbox {PROJECT_NAME} -->\n" +
        "  <category name=\"kz.greetgo.sandbox.register.test.beans.develop\">\n" +
        "    <priority value=\"INFO\"/>\n" +
        "    <appender-ref ref=\"CONSOLE\"/>\n" +
        "  </category>\n" +
        "\n" +
        "  <category name=\"TRACE\">\n" +
        "    <priority value=\"TRACE\"/>\n" +
        "  </category>\n" +
        "\n" +
        "  <category name=\"SQL\">\n" +
        "    <priority value=\"TRACE\"/>\n" +
        "    <!--<appender-ref ref=\"CONSOLE\"/>-->\n" +
        "  </category>\n" +
        "\n" +
        "  <category name=\"DIRECT_SQL\">\n" +
        "    <priority value=\"TRACE\"/>\n" +
        "    <appender-ref ref=\"DIRECT_SQL\"/>\n" +
        "  </category>\n" +
        "  <!-- ///MODIFY replace sandbox {PROJECT_NAME} -->\n" +
        "  <category name=\"kz.greetgo.sandbox.register.impl\">\n" +
        "    <priority value=\"TRACE\"/>\n" +
        "    <!--<appender-ref ref=\"CONSOLE\"/>-->\n" +
        "  </category>\n" +
        "\n" +
        "  <root>\n" +
        "    <priority value=\"INFO\"/>\n" +
        "    <appender-ref ref=\"SERVER\"/>\n" +
        "  </root>\n" +
        "\n" +
        "</log4j:configuration>\n";

    file("dir/file.txt", content);

    String expectedContent = content
        .replaceAll("\\n\\s+<!-- ///MODIFY replace sandbox \\{PROJECT_NAME} -->\\s*\\n", "\n")
        .replaceAll("sandbox", "Stone_In_the_World");

    TemplateCopier.of()
        .from(fromDir)
        .to(toDir)
        .setVariable("PROJECT_NAME", "Stone_In_the_World")
        .copy()
    ;

//    System.out.println("--------------------------------------- EXPECTED   ------------------------------------------");
//    System.out.println(expectedContent);
//    System.out.println("--------------------------------------- ACTUAL     ------------------------------------------");
//    System.out.println(to("dir/file.txt").get());
//    System.out.println("---------------------------------------------------------------------------------------------");
    assertThat(to("dir/file.txt").get()).isEqualTo(expectedContent);
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
