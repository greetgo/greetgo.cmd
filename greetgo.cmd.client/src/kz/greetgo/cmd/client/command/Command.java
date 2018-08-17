package kz.greetgo.cmd.client.command;

import java.io.PrintStream;
import java.util.List;

public interface Command {
  String name();

  void printShortHelpTo(PrintStream out);

  int exec(List<String> argList);
}
