package kz.greetgo.cmd.client.command;

import kz.greetgo.cmd.client.command.new_controller.CommandNewController;

import java.io.PrintStream;
import java.util.List;

public class CommandNew extends CommandAbstract {
  @Override
  public void printShortHelpTo(PrintStream out) {
    out.println("  " + usedCommand + " " + name());
    out.println("      Creates new something. To see what type '" + usedCommand + ' ' + name() + "'");
  }

  @Override
  public int exec(List<String> argList) {
    if (argList.size() == 0) {
      return usage();
    }

    String subCommand = argList.get(0);
    if ("controller".equals(subCommand) || "c".equals(subCommand)) {
      CommandNewController newController = new CommandNewController();
      newController.usage = this::usage;
      return newController.exec(argList.subList(1, argList.size()));
    }

    System.err.println("Unknown sub command " + subCommand);
    System.err.println();
    return usage();
  }

  private int usage() {
    System.err.println("Usage:");
    System.err.println();
    System.err.println("  " + usedCommand + " " + name() + " controller <Name>");
    System.err.println("  or");
    System.err.println("  " + usedCommand + " " + name() + " c <Name>");
    System.err.println("      ");
    System.err.println("      Creates new controller with name <Name>Controller");
    System.err.println("      ");
    System.err.println("      Note <Name> MUST starts with BIG letter");
    System.err.println("      Note <Name> MUST NOT contains 'Controller' at the end");
    System.err.println();
    return 1;
  }
}
