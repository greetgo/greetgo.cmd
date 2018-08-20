package kz.greetgo.cmd.core.errors;

import java.nio.file.Path;

import static kz.greetgo.cmd.core.errors.ErrorUtil.IGNORING_LINES;

public class No_CONTROLLER_MARKER_INTERFACE_TXT extends NoGreetgoParamFile {
  public No_CONTROLLER_MARKER_INTERFACE_TXT(Path greetgoParamFile) {
    super(ErrorUtil.getCommonMessage(greetgoParamFile, text()));
  }

  private static String text() {
    return "" +
      "#\n" +
      "#  This parameter file defines name of interface to be implemented by controller.\n" +
      "#  \n" +
      "#  It must contains a row with interface name.\n" +
      "#  \n" +
      "#  It can be empty, then controller would be implement nothing.\n" +
      "#  \n" +
      "#  " + IGNORING_LINES + "\n" +
      "#\n" +
      "kz.greetgo.sandbox.controller.util.Controller";
  }
}
