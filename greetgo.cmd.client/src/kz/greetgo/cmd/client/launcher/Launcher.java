package kz.greetgo.cmd.client.launcher;

import kz.greetgo.cmd.client.command.CmdBuilder;
import kz.greetgo.cmd.client.command.Command;
import kz.greetgo.cmd.core.errors.SimpleExit;
import kz.greetgo.cmd.core.util.AppUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Launcher {
  public static void main(String[] args) {
    try {
      new Launcher().exec(args);
    } catch (RuntimeException e) {

      SimpleExit simpleExit = SimpleExit.extract(e);

      if (simpleExit != null) {
        System.exit(simpleExit.exitCode);
        return;
      }

      e.printStackTrace();
      System.exit(255);
    }
  }

  private void exec(String[] args) {
    String cmd = AppUtil.usedCommand();
    CmdBuilder cmdBuilder = CmdBuilder.newCmdBuilder().setUsedCommand(cmd);

    if (args.length == 0) {
      usage(cmd, cmdBuilder);
      throw new SimpleExit(1);
    }

    String commandName = args[0];
    List<String> argList = new ArrayList<>(Arrays.asList(args).subList(1, args.length));

    for (Command command : cmdBuilder.allCommands()) {
      if (command.name().equals(commandName)) {
        command.exec(argList);
        return;
      }
    }

    System.err.println("Unknown command " + commandName);
    System.err.println();

    usage(cmd, cmdBuilder);
    throw new SimpleExit(1);
  }

  private void usage(String cmd, CmdBuilder cmdBuilder) {
    System.err.println("Usage: " + cmd + " <command>");
    System.err.println();
    for (Command command : cmdBuilder.allCommands()) {
      command.printShortHelpTo(System.err);
      System.err.println();
    }
  }
}
