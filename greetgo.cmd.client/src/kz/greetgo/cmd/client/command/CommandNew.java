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
  public int exec(List<String> argList, PrintStream out, PrintStream err) {
    if (argList.size() == 0) {
      return usage(out);
    }

    String subCommand = argList.get(0);
    if ("controller".equals(subCommand) || "c".equals(subCommand)) {
      CommandNewController newController = new CommandNewController();
      newController.usage = () -> usage(err);
      return newController.exec(argList.subList(1, argList.size()), out, err);
    }

    err.println("Unknown sub command " + subCommand);
    err.println();
    return usage(err);
  }

  private int usage(PrintStream err) {
    err.println("  " + usedCommand + " " + name() + " controller <controller_name>");
    err.println("  or");
    err.println("  " + usedCommand + " " + name() + " c <controller_name>");
    err.println("      Creates new controller with name <controller_name>");
    return 1;
  }
}
