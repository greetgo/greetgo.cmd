package kz.greetgo.cmd.core.errors;

import java.nio.file.Path;

import static kz.greetgo.cmd.core.errors.ErrorUtil.IGNORING_LINES;

public class No_MYBATIS_DAO_DATABASES_TXT extends NoGreetgoParamFile {

  public No_MYBATIS_DAO_DATABASES_TXT(Path greetgoParamFile) {
    super(ErrorUtil.getCommonMessage(greetgoParamFile, text()));
  }

  private static String text() {
    return "" +
      "#\n" +
      "#  This parameter file defines names of using databases.\n" +
      "#  \n" +
      "#  It must contains each name in one line. Names must starts with uppercase\n" +
      "#  \n" +
      "#  " + IGNORING_LINES + "\n" +
      "#\n" +
      "Postgres\n" +
      "Oracle\n" +
      "Mysql\n";
  }
}
