package kz.greetgo.cmd.client.command.new_controller;

import java.io.PrintStream;
import java.util.List;
import java.util.function.Supplier;

public class CommandNewController {

  public Supplier<Integer> usage = null;

  public int exec(List<String> argList) {
    if (argList.size() == 0) {
      System.err.println("Not specified controller name. Usage:");
      System.err.println();
      return usage.get();
    }

    String controllerName = argList.get(0);

    System.out.println("Creating controller " + controllerName);
    return 1;
  }
}
