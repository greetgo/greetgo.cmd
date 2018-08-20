package kz.greetgo.cmd.core.errors;

import java.nio.file.Path;

import static kz.greetgo.cmd.core.errors.ErrorUtil.IGNORING_LINES;

public class No_MYBATIS_DAO_PACKAGE_TXT extends NoGreetgoParamFile {
  public No_MYBATIS_DAO_PACKAGE_TXT(Path greetgoParamFile) {
    super(ErrorUtil.getCommonMessage(greetgoParamFile, text()));
  }

  private static String text() {
    return "" +
      "#\n" +
      "#  This parameter file defines a location of mybatis dao interface file.\n" +
      "#  \n" +
      "#  It must contains a row with two parameters separated with space:\n" +
      "#  \n" +
      "#  * The first parameter is path to sources dir relative project root.\n" +
      "#  \n" +
      "#  * The second parameter is path to java-file.\n" +
      "#  \n" +
      "#  " + IGNORING_LINES + "\n" +
      "#\n" +
      "sandbox.register/src     kz/greetgo/sandbox/register/dao";
  }
}
