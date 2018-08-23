package kz.greetgo.cmd.client.command;

import kz.greetgo.cmd.core.errors.SimpleExit;

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
      String specificationVersion = getClass().getPackage().getSpecificationVersion();
      System.out.println("version = " + specificationVersion);
      String implementationVersion = getClass().getPackage().getImplementationVersion();
      System.out.println("git = " + implementationVersion);
      System.out.println("USED_COMMAND = " + System.getenv("USED_COMMAND"));
      System.out.println("CURRENT_WORKING_DIR = " + System.getenv("CURRENT_WORKING_DIR"));
      return;
    }

    System.err.println("While parameters not works");
    throw new SimpleExit(1);
  }
}
