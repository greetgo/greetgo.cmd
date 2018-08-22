package kz.greetgo.cmd.client.command.new_sub;

import java.util.List;

public abstract class NewSubCommand {

  public String cmdPrefix;

  public abstract int exec(List<String> argList);

  public abstract boolean accept(String strSubCommand);

  public abstract void printUsage();
}
