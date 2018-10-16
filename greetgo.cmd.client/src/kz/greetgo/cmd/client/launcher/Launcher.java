package kz.greetgo.cmd.client.launcher;

import kz.greetgo.cmd.client.command.CmdBuilder;
import kz.greetgo.cmd.client.command.Command;
import kz.greetgo.cmd.client.command.CommandUpdate;
import kz.greetgo.cmd.core.errors.SimpleExit;
import kz.greetgo.cmd.core.local_params.LocalParams;
import kz.greetgo.cmd.core.local_params.LocalParamsImpl;
import kz.greetgo.cmd.core.util.AppUtil;
import kz.greetgo.cmd.core.util.Locations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static kz.greetgo.cmd.client.command.CmdBuilder.newCmdBuilder;

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
    LocalParams localParams = getLocalParams();
    String cmd = AppUtil.usedCommand();
    CmdBuilder cmdBuilder = newCmdBuilder()
        .setUsedCommand(cmd)
        .setLocalParams(localParams);

    cmdBuilder.commandOf(CommandUpdate.class).checkNeedUpdate();

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

  private LocalParams getLocalParams() {
    return new LocalParamsImpl(Locations.lastUpdateCheckedAtFile());
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
