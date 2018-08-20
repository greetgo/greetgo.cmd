package kz.greetgo.cmd.core.errors;

import java.nio.file.Path;

import static kz.greetgo.cmd.core.errors.ErrorUtil.IGNORING_LINES;

public class No_REGISTER_IMPL_TEST_EXTENDS_TXT extends NoGreetgoParamFile {
  public No_REGISTER_IMPL_TEST_EXTENDS_TXT(Path greetgoParamFile) {
    super(ErrorUtil.getCommonMessage(greetgoParamFile, text()));
  }

  private static String text() {
    return "" +
      "#\n" +
      "#  This parameter file defines class name extends register impl test class.\n" +
      "#  \n" +
      "#  It must contains a row full class name.\n" +
      "#  \n" +
      "#  " + IGNORING_LINES + "\n" +
      "#\n" +
      "kz.greetgo.sandbox.register.test.util.ParentTestNg";
  }
}
