package kz.greetgo.cmd.core.errors;

public class LeftExitCodeOfCommand extends RuntimeException {
  public LeftExitCodeOfCommand(int exitCode, String cmd) {
    super(message(exitCode, cmd));
  }

  private static String message(int exitCode, String cmd) {
    return "Error exit code " + exitCode + " of executed command `" + cmd + "'";
  }
}
