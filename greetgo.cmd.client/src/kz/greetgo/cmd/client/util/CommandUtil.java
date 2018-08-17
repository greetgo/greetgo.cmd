package kz.greetgo.cmd.client.util;

public class CommandUtil {
  public static String calcName(Class<?> aClass) {
    if (!aClass.getSimpleName().startsWith("Command")) {
      throw new RuntimeException("Command class must starts with 'Command'");
    }
    return aClass.getSimpleName().substring("Command".length()).toLowerCase();
  }
}
