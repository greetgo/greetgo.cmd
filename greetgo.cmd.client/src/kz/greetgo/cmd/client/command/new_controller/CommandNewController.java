package kz.greetgo.cmd.client.command.new_controller;

import kz.greetgo.cmd.client.command.new_sub.NewSubCommand;
import kz.greetgo.cmd.core.project.Project;
import kz.greetgo.cmd.core.util.PathUtil;

import java.util.List;

public class CommandNewController extends NewSubCommand {

  @Override
  public int exec(List<String> argList) {
    if (argList.size() == 0) {
      System.err.println("Not specified controller name. Usage:");
      System.err.println();
      printUsage();
      return 1;
    }

    String name = argList.get(0);


    try {
      Project project = Project.openProject(PathUtil.findRoot());

      CommandNewControllerApplier a = new CommandNewControllerApplier(project, name);

      a.execute();
      return 0;
    } catch (RuntimeException e) {
      e.printStackTrace();
      return 100;
    }
  }

  @Override
  public boolean accept(String strSubCommand) {
    return "controller".equals(strSubCommand) || "c".equals(strSubCommand);
  }

  @Override
  public void printUsage() {
    System.err.println("  " + cmdPrefix + " [controller | c] <Name>");
    System.err.println("      ");
    System.err.println("      Creates new controller with name <Name>Controller");
    System.err.println("      ");
    System.err.println("      Note <Name> MUST starts with BIG letter");
    System.err.println("      Note <Name> MUST NOT contains 'Controller' at the end");
    System.err.println("      ");
    System.err.println("      This command MUST be run in project");
  }
}
