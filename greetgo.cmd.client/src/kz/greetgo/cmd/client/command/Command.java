package kz.greetgo.cmd.client.command;

import kz.greetgo.cmd.core.errors.SimpleExit;

import java.io.PrintStream;
import java.util.List;

public interface Command {
  String name();

  void printShortHelpTo(PrintStream out);

  void exec(List<String> argList) throws SimpleExit;
}
