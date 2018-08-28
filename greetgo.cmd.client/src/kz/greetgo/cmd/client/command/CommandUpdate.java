package kz.greetgo.cmd.client.command;

import kz.greetgo.cmd.core.errors.SimpleExit;
import kz.greetgo.cmd.core.git.Git;

import java.io.PrintStream;
import java.nio.file.Paths;
import java.util.List;

import static kz.greetgo.cmd.core.util.CmdUtil.executeCommand;
import static kz.greetgo.cmd.core.util.CmdUtil.outStd;

public class CommandUpdate extends CommandAbstract {
  @Override
  public void printShortHelpTo(PrintStream out) {
    out.println("  " + usedCommand + " " + name());
    out.println("      Updates " + usedCommand + " (greetgo-cli) in current branch to last state");
  }

  @Override
  public void exec(List<String> argList) throws SimpleExit {
    Git.pull(Paths.get("."));
    outStd(executeCommand(Paths.get("."), "./gradlew", "clean").ok());
  }
}
