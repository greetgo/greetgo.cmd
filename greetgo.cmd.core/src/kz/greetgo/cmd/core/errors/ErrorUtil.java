package kz.greetgo.cmd.core.errors;

import kz.greetgo.cmd.core.util.FileUtil;

import java.io.File;
import java.nio.file.Path;

public class ErrorUtil {

  public static final String IGNORING_LINES = "If a line empty or starts with # then it is ignored";

  public static String getCommonMessage(Path greetgoParamFile, String text) {
    String dirFullName = greetgoParamFile.toFile().getParentFile().getAbsolutePath();

    File templateFile = new File(greetgoParamFile.toFile().getAbsolutePath() + ".template.txt");
    if (!templateFile.exists()) {
      FileUtil.strToFile(text, templateFile);
    }

    return "No or corrupted file `" + greetgoParamFile.getFileName() + "' in directory:\n\n"
      + "    " + dirFullName + "\n\n"
      + "    or\n\n"
      + "    PROJECT_ROOT/.greetgo\n\n"
      + "Please open file `" + templateFile.getName() + "' in this directory, correct it like you need" +
      " and rename it to `" + greetgoParamFile.getFileName() + "'";
  }
}
