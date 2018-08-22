package kz.greetgo.cmd.client.command;

import kz.greetgo.cmd.client.command.new_controller.CommandNewController;
import kz.greetgo.cmd.client.command.new_project.CommandNewProject;
import kz.greetgo.cmd.client.command.new_sub.NewSubCommand;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class CommandNew extends CommandAbstract {
  @Override
  public void printShortHelpTo(PrintStream out) {
    out.println("  " + usedCommand + " " + name());
    out.println("      Creates new something. To see what type '" + usedCommand + ' ' + name() + "'");
  }

  private final List<NewSubCommand> subCommandList = new ArrayList<>();

  {
    subCommandList.add(new CommandNewProject());
    subCommandList.add(new CommandNewController());
  }

  @Override
  public int exec(List<String> argList) {
    for (NewSubCommand subCommand : subCommandList) {
      subCommand.cmdPrefix = usedCommand + " " + name();
    }

    if (argList.size() == 0) {
      return usage();
    }

    String strSubCommand = argList.get(0);

    for (NewSubCommand command : subCommandList) {
      if (command.accept(strSubCommand)) {
        return command.exec(argList.subList(1, argList.size()));
      }
    }

    System.err.println("Unknown sub command " + strSubCommand);
    System.err.println();
    return usage();
  }

  private int usage() {
    System.err.println("Usage:");
    System.err.println();
    for (NewSubCommand subCommand : subCommandList) {
      subCommand.printUsage();
      System.err.println();
    }
    return 1;
  }
}
