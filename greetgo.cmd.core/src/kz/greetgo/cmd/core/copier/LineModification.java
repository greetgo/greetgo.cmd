package kz.greetgo.cmd.core.copier;

import java.io.File;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LineModification {
  private final String modificationCode;

  public LineModification(String modificationCode) {
    this.modificationCode = modificationCode;
  }


  public static LineModification extract(String line) {
    String cleanLine = clearLine(line);

    if (cleanLine == null) {
      return null;
    }

    if (cleanLine.startsWith("///MODIFY ")) {
      return new LineModification(cleanLine.substring("///MODIFY ".length()).trim());
    }

    if (cleanLine.startsWith("###MODIFY ")) {
      return new LineModification(cleanLine.substring("###MODIFY ".length()).trim());
    }

    return null;
  }

  private static String clearLine(String line) {
    String trimmedLine = line.trim();
    if (trimmedLine.startsWith("<!--") && trimmedLine.endsWith("-->")) {
      return trimmedLine.substring(4, trimmedLine.length() - 3).trim();
    }
    return trimmedLine;
  }

  public String modifyStr(String nextLine, int line, File fromFile, Function<String,String> valueUpdater) {
    String[] split = modificationCode.split("\\s+");
    if (split.length == 3 && split[0].equals("replace")) {
      String regexp = split[1];
      String replacement = valueUpdater.apply(split[2]);

      Pattern r = Pattern.compile(regexp);

      StringBuffer result = new StringBuffer();
      Matcher matcher = r.matcher(nextLine);
      while (matcher.find()) {
        matcher.appendReplacement(result, replacement);
      }
      matcher.appendTail(result);

      return result.toString();
    }

    throw new RuntimeException("Unknown modify command: `" + modificationCode + "' at line " + line + " in " + fromFile);
  }
}
