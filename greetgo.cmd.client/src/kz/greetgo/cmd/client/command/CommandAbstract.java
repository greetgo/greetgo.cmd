package kz.greetgo.cmd.client.command;

import kz.greetgo.cmd.client.util.CommandUtil;
import kz.greetgo.cmd.core.local_params.LocalParams;

public abstract class CommandAbstract implements Command {
  private final String name = CommandUtil.calcName(getClass());

  String usedCommand = "greetgo";
  LocalParams localParams = null;

  @Override
  public String name() {
    return name;
  }
}
