package kz.greetgo.cmd.client.command;

import kz.greetgo.cmd.core.local_params.LocalParams;

import java.util.ArrayList;
import java.util.List;

public class CmdBuilder {

  final List<CommandAbstract> commandList = new ArrayList<>();

  {
    commandList.add(new CommandUpdate());
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

  public List<? extends Command> allCommands() {
    return commandList;
  }

  public <T extends Command> T commandOf(Class<T> aClass) {
    //noinspection unchecked
    return (T) commandList.stream()
        .filter(aClass::isInstance)
        .findAny()
        .orElseThrow(() -> new IllegalArgumentException("No command for " + aClass));
  }

  public CmdBuilder setLocalParams(LocalParams localParams) {
    commandList.forEach(c -> c.localParams = localParams);
    return this;
  }
}
