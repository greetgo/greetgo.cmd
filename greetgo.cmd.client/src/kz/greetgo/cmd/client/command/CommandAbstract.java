package kz.greetgo.cmd.client.command;

import kz.greetgo.cmd.client.util.CommandUtil;

public abstract class CommandAbstract implements Command {
  private final String name = CommandUtil.calcName(getClass());

  String usedCommand = "greetgo";

  @Override
  public String name() {
    return name;
  }
}
