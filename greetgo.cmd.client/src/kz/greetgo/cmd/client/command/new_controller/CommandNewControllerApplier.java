package kz.greetgo.cmd.client.command.new_controller;

import kz.greetgo.cmd.core.project.ClassRef;
import kz.greetgo.cmd.core.project.ClassType;
import kz.greetgo.cmd.core.project.Project;
import kz.greetgo.depinject.core.Bean;
import kz.greetgo.depinject.core.BeanGetter;
import kz.greetgo.mvc.annotations.AsIs;
import kz.greetgo.mvc.annotations.Par;
import kz.greetgo.mvc.annotations.on_methods.ControllerPrefix;
import kz.greetgo.mvc.annotations.on_methods.OnGet;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;
import java.util.List;

public class CommandNewControllerApplier {
  private final Project project;
  private final String name;

  public CommandNewControllerApplier(Project project, String name) {
    this.project = project;
    this.name = name;
  }

  private ClassRef controllerRef;
  private ClassRef registerInterfaceRef;
  private ClassRef registerImplRef;
  private ClassRef mybatisDaoRef;
  private final List<ClassRef> mybatisDaoDbRefList = new ArrayList<>();

  public int execute() {

    controllerRef = project.getControllerPackageRef().createClassRef(name + "Controller");
    registerInterfaceRef = project.getRegisterInterfacePackageRef().createClassRef(name + "Register");
    registerImplRef = project.getRegisterImplPackageRef().createClassRef(name + "RegisterImpl");
    mybatisDaoRef = project.getMybatisDaoPackageRef().createClassRef(name + "Dao");
    for (String database : project.getMybatisDaoDatabases()) {
      mybatisDaoDbRefList.add(mybatisDaoRef.inPackage
        .subPackage(database.toLowerCase())
        .createClassRef(name + "Dao" + database)
      );
    }

    generateController();
    generateRegisterInterface();
    generateRegisterImpl();
    generateDao();

    controllerRef.save();
    registerInterfaceRef.save();
    registerImplRef.save();
    mybatisDaoRef.save();
    mybatisDaoDbRefList.forEach(ClassRef::save);

    return 0;
  }

  private void generateController() {
    controllerRef.beforeClass.pr("@").cl(Bean.class).prn();
    controllerRef.beforeClass.pr("@").cl(ControllerPrefix.class).prn("(\"/" + name.toLowerCase() + "\")");

    {
      String controllerMarkerInterface = project.getControllerMarkerInterface();
      if (controllerMarkerInterface != null) {
        controllerRef.implement(controllerMarkerInterface);
      }
    }

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
  }

  private void generateRegisterInterface() {
    registerInterfaceRef.type = ClassType.INTERFACE;
    registerInterfaceRef.content.prn(1);
    registerInterfaceRef.content.pr(1, "String getById(String id);").prn();
    registerInterfaceRef.content.prn(1);
  }

  private void generateRegisterImpl() {
    registerImplRef.beforeClass.pr("@").cl(Bean.class).prn();

    registerImplRef.content.prn(1);

    String daoVar = name.toLowerCase() + "Dao";

    registerImplRef.content
      .pr(1, "public ").cl(BeanGetter.class).pr("<").cl(mybatisDaoRef.fullName()).prn("> " + daoVar + ";");

    registerImplRef.content.prn(1);

    registerImplRef.content.prn(1, "@Override");
    registerImplRef.content.prn(1, "public String getById(String id) {");
    registerImplRef.content.prn(2, "return " + daoVar + ".get().getById(id);");
    registerImplRef.content.prn(1, "}");

    registerImplRef.content.prn(1);

    registerImplRef.implement(registerInterfaceRef.fullName());
  }

  private void generateDao() {
    mybatisDaoRef.content.prn();

    mybatisDaoRef.content.pr(1, "@").cl(Select.class).prn("(\"select #{id} as id from dual\")");
    mybatisDaoRef.content.pr(1, "String getById(@").cl(Param.class).pr("(\"id\") String id);").prn();

    mybatisDaoRef.content.prn();

    mybatisDaoRef.type = ClassType.INTERFACE;

    for (ClassRef ref : mybatisDaoDbRefList) {
      ref.beforeClass.pr("@").cl(Bean.class).prn();
      ref.setParent(mybatisDaoRef.fullName());
      ref.type = ClassType.INTERFACE;
    }
  }
}
