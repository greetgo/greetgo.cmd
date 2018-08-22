package kz.greetgo.cmd.core.util;

import kz.greetgo.cmd.core.errors.LeftExitCodeOfCommand;

import java.util.List;

public class CmdResult {
  public final List<String> stdOut;
  public final List<String> stdErr;
  public final String cmd;
  public final int exitCode;

  private CmdResult(List<String> stdOut, List<String> stdErr, String cmd, int exitCode) {
    this.stdOut = stdOut;
    this.stdErr = stdErr;
    this.cmd = cmd;
    this.exitCode = exitCode;
  }

  public static CmdResult of(String cmd, int exitCode, List<String> stdOut, List<String> stdErr) {
    return new CmdResult(stdOut, stdErr, cmd, exitCode);
  }

  public CmdResult ok() {
    if (exitCode != 0) {
      throw new LeftExitCodeOfCommand(exitCode, cmd);
    }
    return this;
  }
}
