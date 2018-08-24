package kz.greetgo.cmd.client.command;

import kz.greetgo.cmd.core.errors.SimpleExit;
import kz.greetgo.cmd.core.util.AppUtil;

import java.io.PrintStream;
import java.util.List;

public class CommandVersion extends CommandAbstract {

  @Override
  public void printShortHelpTo(PrintStream out) {
    out.println("  " + usedCommand + " " + name());
    out.println("      Shows full version information");
    //    out.println("  " + usedCommand + " " + name() + " help");
    //    out.println("      Shows another variants of using the command");
  }

  @Override
  public void exec(List<String> argList) {
    if (argList.size() == 0) {
      System.out.println("version = " + AppUtil.version());
      System.out.println("gitId = " + AppUtil.gitId());
      System.out.println("USED_COMMAND = " + AppUtil.usedCommand());
      System.out.println("CURRENT_WORKING_DIR = " + AppUtil.currentWorkingDir());
      return;
    }

    System.err.println("While parameters not work");
    throw new SimpleExit(1);
  }
}
