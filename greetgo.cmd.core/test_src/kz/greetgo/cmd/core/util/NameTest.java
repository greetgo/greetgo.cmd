package kz.greetgo.cmd.core.util;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Optional;

import static org.fest.assertions.api.Assertions.assertThat;

public class NameTest {

  @Test
  public void parse_simpleName_subPackageName___hasSubPackage() {

    Name name = Name.parse("///some//sub/pack\\\\HelloWorld");

    assertThat(name).isNotNull();
    assertThat(name.simpleName()).isEqualTo("HelloWorld");
    assertThat(name.subPackageName()).isEqualTo(Optional.of("some.sub.pack"));

  }

  @Test
  public void parse_simpleName_subPackageName___hasSubPackage_usingDots() {

    Name name = Name.parse("..another.cool..pack..GoodByWorld");

    assertThat(name).isNotNull();
    assertThat(name.simpleName()).isEqualTo("GoodByWorld");
    assertThat(name.subPackageName()).isEqualTo(Optional.of("another.cool.pack"));

  }

  @Test
  public void parse_simpleName_subPackageName___noSubPackage() {

    Name name = Name.parse("ClientParameter");

    assertThat(name).isNotNull();
    assertThat(name.simpleName()).isEqualTo("ClientParameter");
    assertThat(name.subPackageName()).isEqualTo(Optional.empty());

  }

  @Test
  public void parse_renameTo_bothWithSubPackages() {

    Name name = Name.parse("first.sub.BaseClass");

    //
    //
    Name anotherName = name.renameTo("more.sub1.sub2.AnotherClass");
    //
    //

    assertThat(anotherName).isNotNull();
    assertThat(anotherName.simpleName()).isEqualTo("AnotherClass");
    assertThat(anotherName.subPackageName()).isEqualTo(Optional.of("first.sub.more.sub1.sub2"));
  }

  @Test
  public void parse_renameTo_anotherWithSubPackages() {

    Name name = Name.parse("BaseClass");

    //
    //
    Name anotherName = name.renameTo("more.sub1.sub2.AnotherClass");
    //
    //

    assertThat(anotherName).isNotNull();
    assertThat(anotherName.simpleName()).isEqualTo("AnotherClass");
    assertThat(anotherName.subPackageName()).isEqualTo(Optional.of("more.sub1.sub2"));
  }

  @Test
  public void parse_renameTo_baseWithSubPackages() {

    Name name = Name.parse("first.sub.BaseClass");

    //
    //
    Name anotherName = name.renameTo("AnotherClass");
    //
    //

    assertThat(anotherName).isNotNull();
    assertThat(anotherName.simpleName()).isEqualTo("AnotherClass");
    assertThat(anotherName.subPackageName()).isEqualTo(Optional.of("first.sub"));
  }

  @Test
  public void parse_renameTo_bothWithoutSubPackages() {

    Name name = Name.parse("BaseClass");

    //
    //
    Name anotherName = name.renameTo("AnotherClass");
    //
    //

    assertThat(anotherName).isNotNull();
    assertThat(anotherName.simpleName()).isEqualTo("AnotherClass");
    assertThat(anotherName.subPackageName()).isEqualTo(Optional.empty());
  }

  @Test
  public void nameWithSlashes_withSubPackage() {

    Name name = Name.parse("///some//sub/sub2...pack\\\\HelloWorld..");

    //
    //
    String nameWithSlashes = name.nameWithSlashes();
    //
    //

    assertThat(nameWithSlashes).isEqualTo("some/sub/sub2/pack/HelloWorld");

  }


  @Test
  public void nameWithSlashes_withoutSubPackage() {

    Name name = Name.parse("SuperSinus");

    //
    //
    String nameWithSlashes = name.nameWithSlashes();
    //
    //

    assertThat(nameWithSlashes).isEqualTo("SuperSinus");

  }

  @Test
  public void slashesPackageAndNameWithMinuses_withSubpackage() {
    Name name = Name.parse("some.sub.sub2.pack.AClassName");

    //
    //
    String actual = name.slashesPackageAndNameWithMinuses();
    //
    //

    assertThat(actual).isEqualTo("some/sub/sub2/pack/a-class-name");
  }

  @Test
  public void slashesPackageAndNameWithMinuses_withoutSubpackage() {
    Name name = Name.parse("SomeAnotherClassFBI");

    //
    //
    String actual = name.slashesPackageAndNameWithMinuses();
    //
    //

    assertThat(actual).isEqualTo("some-another-class-fbi");
  }

  @Test
  public void killSubPackage_withSubPackage() {

    Name name = Name.parse("some.sub.sub2.pack.CoolClass");

    //
    //
    Name nameWithoutPackage = name.killSubPackage();
    //
    //

    assertThat(nameWithoutPackage).isNotNull();
    assertThat(nameWithoutPackage.simpleName()).isEqualTo("CoolClass");
    assertThat(nameWithoutPackage.nameWithSlashes()).isEqualTo("CoolClass");
  }

  @Test
  public void killSubPackage_withoutSubPackage() {

    Name name = Name.parse("CoolClass");

    //
    //
    Name nameWithoutPackage = name.killSubPackage();
    //
    //

    assertThat(nameWithoutPackage).isNotNull();
    assertThat(nameWithoutPackage.simpleName()).isEqualTo("CoolClass");
    assertThat(nameWithoutPackage.nameWithSlashes()).isEqualTo("CoolClass");

    assertThat(nameWithoutPackage).isSameAs(name);
  }

  @DataProvider
  public Object[][] appendToNameDataProvider() {
    return new Object[][]{
      {"some.sub.sub2.pack.CoolClass", "Hello", "CoolClassHello", false},
      {"CoolClass", "ByBy", "CoolClassByBy", false},
      {"some.sub.sub2.pack.CoolClass", null, "CoolClass", true},
      {"CoolClass", null, "CoolClass", true},
    };
  }

  @Test(dataProvider = "appendToNameDataProvider")
  public void appendToName(String fullName, String suffix, String expectedName, boolean checkIsSame) {
    Name name = Name.parse(fullName);

    //
    //
    Name newName = name.appendToName(suffix);
    //
    //

    assertThat(newName).isNotNull();
    assertThat(newName.simpleName()).isEqualTo(expectedName);
    assertThat(newName.subPackageName()).isEqualTo(name.subPackageName());

    if (checkIsSame) {
      assertThat(newName).isSameAs(name);
    }
  }
}
