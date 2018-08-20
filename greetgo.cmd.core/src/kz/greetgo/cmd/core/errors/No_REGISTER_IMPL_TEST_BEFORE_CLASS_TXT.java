package kz.greetgo.cmd.core.errors;

import java.nio.file.Path;

import static kz.greetgo.cmd.core.errors.ErrorUtil.IGNORING_LINES;

public class No_REGISTER_IMPL_TEST_BEFORE_CLASS_TXT extends NoGreetgoParamFile {
  public No_REGISTER_IMPL_TEST_BEFORE_CLASS_TXT(Path greetgoParamFile) {
    super(ErrorUtil.getCommonMessage(greetgoParamFile, text()));
  }

  private static String text() {
    return "" +
      "#\n" +
      "#  This parameter file defines a annotations before register test class.\n" +
      "#  \n" +
      "#  All lines would be copied. Tildes (~) border full class names to convert to short form\n" +
      "#  \n" +
      "#  " + IGNORING_LINES + "\n" +
      "#\n" +
      "@~kz.greetgo.depinject.testng.ContainerConfig~" +
      "(~kz.greetgo.sandbox.register.test.util.BeanConfigTests~.class)\n" +
      "@~java.lang.SuppressWarnings~(\"asd\")\n";
  }
}
