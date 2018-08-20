package kz.greetgo.cmd.client.command.new_controller;

import kz.greetgo.cmd.core.project.Project;
import kz.greetgo.cmd.core.util.PathUtil;

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
}
