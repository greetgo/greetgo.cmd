package kz.greetgo.cmd.client.command.new_controller;

import java.io.PrintStream;
import java.util.List;
import java.util.function.Supplier;

public class CommandNewController {

  public Supplier<Integer> usage = null;

  public int exec(List<String> argList, PrintStream out, PrintStream err) {
    if (argList.size() == 0) {
      err.println("Not specified controller name. Usage:");
      return usage.get();
    }
    System.out.println("Creating controller " + argList.get(0));
    return 1;
  }
}
