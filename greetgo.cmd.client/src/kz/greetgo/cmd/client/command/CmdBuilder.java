package kz.greetgo.cmd.client.command;

import java.util.ArrayList;
import java.util.List;

public class CmdBuilder {

  List<CommandAbstract> commandList = new ArrayList<>();

  {
    commandList.add(new CommandNew());
    commandList.add(new CommandVersion());
  }

  public static CmdBuilder newCmdBuilder() {
    return new CmdBuilder();
  }

  public CmdBuilder setUsedCommand(String usedCommand) {
    if (usedCommand != null && usedCommand.trim().length() > 0) {
      commandList.forEach(c -> c.usedCommand = usedCommand);
    }
    return this;
  }

  public List<Command> allCommands() {
    List<Command> ret = new ArrayList<>();
    ret.add(new CommandVersion());
    ret.add(new CommandNew());
    return ret;
  }
}