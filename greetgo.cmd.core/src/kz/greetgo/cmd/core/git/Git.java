package kz.greetgo.cmd.core.git;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import static kz.greetgo.cmd.core.util.CmdUtil.executeCommand;
import static kz.greetgo.cmd.core.util.CmdUtil.runCommand;

public class Git {
  public static void gitClone(Path localRepoPath, String gitUrl, String repoDirName) {
    executeCommand(localRepoPath, "git", "clone", gitUrl, repoDirName);
  }

  public static void fetchAllBranches(Path gitPath) {
    runCommand(gitPath, "git fetch --prune");
  }

  public static List<String> listRemoteBranches(Path gitPath) {
    return runCommand(gitPath, "git branch --list --no-column --no-color --all --remotes").ok().stdOut
      .stream()
      .map(b -> b.split("/"))
      .filter(pp -> pp.length == 2)
      .map(pp -> pp[1])
      .sorted()
      .collect(Collectors.toList())
      ;
  }

  public static void checkout(Path gitPath, String branchName) {
    executeCommand(gitPath, "git", "checkout", "--force", branchName).ok();
    executeCommand(gitPath, "git", "pull").ok();
  }
}
