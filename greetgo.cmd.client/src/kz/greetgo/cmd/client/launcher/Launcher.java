package kz.greetgo.cmd.client.launcher;

import kz.greetgo.cmd.client.command.CmdBuilder;
import kz.greetgo.cmd.client.command.Command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Launcher {
  public static void main(String[] args) {
    System.exit(new Launcher().exec(args));
  }

  private int exec(String[] args) {
    String cmd = System.getenv("USED_COMMAND");
    CmdBuilder cmdBuilder = CmdBuilder.newCmdBuilder()
      .setUsedCommand(cmd);

    if (args.length == 0) {
      return usage(cmd, cmdBuilder);
    }

    String commandName = args[0];
    List<String> argList = new ArrayList<>(Arrays.asList(args).subList(1, args.length));

    for (Command command : cmdBuilder.allCommands()) {
      if (command.name().equals(commandName)) {
        return command.exec(argList);
      }
    }

    System.err.println("Unknown command " + commandName);
    System.err.println();

    return usage(cmd, cmdBuilder);
  }

  private int usage(String cmd, CmdBuilder cmdBuilder) {
    System.err.println("Usage: " + cmd + " <command>");
    System.err.println();
    for (Command command : cmdBuilder.allCommands()) {
      command.printShortHelpTo(System.err);
      System.err.println();
    }
    return 1;
  }
}
