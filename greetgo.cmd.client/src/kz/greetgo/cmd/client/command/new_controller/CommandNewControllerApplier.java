package kz.greetgo.cmd.client.command.new_controller;

import kz.greetgo.cmd.core.project.ClassRef;
import kz.greetgo.cmd.core.project.Project;
import kz.greetgo.depinject.core.Bean;
import kz.greetgo.depinject.core.BeanGetter;
import kz.greetgo.mvc.annotations.AsIs;
import kz.greetgo.mvc.annotations.Par;
import kz.greetgo.mvc.annotations.on_methods.ControllerPrefix;
import kz.greetgo.mvc.annotations.on_methods.OnGet;

public class CommandNewControllerApplier {
  private final Project project;
  private final String name;

  public CommandNewControllerApplier(Project project, String name) {
    this.project = project;
    this.name = name;
  }

  public int execute() {

    ClassRef controllerRef = project.getControllerPackageRef().createClassRef(name + "Controller");
    controllerRef.beforeClass.pr("@").cl(Bean.class).prn();
    controllerRef.beforeClass.pr("@").cl(ControllerPrefix.class).prn("(\"/" + name.toLowerCase() + "\")");

    {
      String controllerMarkerInterface = project.getControllerMarkerInterface();
      if (controllerMarkerInterface != null) {
        controllerRef.implement(controllerMarkerInterface);
      }
    }

    ClassRef registerInterfaceRef = project.getRegisterInterfacePackageRef().createClassRef(name + "Register");

    String registerVar = name.toLowerCase() + "Register";

    controllerRef.content.prn(1);
    controllerRef.content.pr(1, "public ").cl(BeanGetter.class)
      .pr("<").cl(registerInterfaceRef.fullName()).pr("> ")
      .pr(registerVar).prn(";")
    ;

    controllerRef.content.prn(1);

    controllerRef.content.pr(1, "@").cl(AsIs.class).prn();
    controllerRef.content.pr(1, "@").cl(OnGet.class).prn("(\"/get_by_id\")");
    controllerRef.content.pr(1, "public String getById(@").cl(Par.class).pr("(\"id\") String id) {").prn();
    controllerRef.content.pr(2, "return " + registerVar + ".get().getById(id);").prn();
    controllerRef.content.prn(1, "}");

    controllerRef.content.prn(1);

    registerInterfaceRef.content.prn(1);

    registerInterfaceRef.content.prn(1, "@Override");
    registerInterfaceRef.content.pr(1, "String getById(String id);").prn();

    registerInterfaceRef.content.prn(1);


    controllerRef.save();
    registerInterfaceRef.save();

    return 0;
  }
}
