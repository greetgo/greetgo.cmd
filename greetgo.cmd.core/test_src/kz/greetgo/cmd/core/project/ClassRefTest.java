package kz.greetgo.cmd.core.project;

import kz.greetgo.cmd.core.for_tests.SomeInterface;
import kz.greetgo.cmd.core.for_tests.SomeTestingClass;
import kz.greetgo.depinject.core.Bean;
import kz.greetgo.util.RND;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ClassRefTest {

  Path rootDir;

  @BeforeMethod
  public void setUp() {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

    rootDir = Paths.get("build")
      .resolve("tests_data")
      .resolve("ClassRefTest")
      .resolve("t-" + sdf.format(new Date()) + "-" + RND.intStr(4));

  }

  @Test
  public void normalCreateFile() {
    PackageRef packageRef = new PackageRef(rootDir, "", "kz/greetgo/asd");
    ClassRef classRef = new ClassRef(packageRef, "TestGenClass");
    classRef.content.prn(1, "private String name;");
    classRef.content.pr(1, "private ").cl(SomeTestingClass.class).prn(" someTestingField;");
    classRef.content.pr(1, "private ").cl(Date.class).prn(" dateField;");

    classRef.beforeClass.pr("@").cl(Bean.class).prn();

    classRef.implement(SomeInterface.class);

    classRef.save();
  }
}