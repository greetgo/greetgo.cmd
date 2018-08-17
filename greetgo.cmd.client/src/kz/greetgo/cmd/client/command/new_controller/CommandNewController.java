package kz.greetgo.cmd.client.command.new_controller;

import kz.greetgo.cmd.core.project.PackageRef;
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

    String controllerName = argList.get(0);

    Project project = Project.openProject(PathUtil.findRoot());

    PackageRef controllerPackageRef = project.getControllerPackageRef();

    controllerPackageRef.createClassRef(controllerName);

    System.out.println("Creating controller " + controllerName);

    return 0;
  }
}
