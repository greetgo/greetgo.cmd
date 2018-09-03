package kz.greetgo.cmd.client.command.new_controller;

import kz.greetgo.cmd.core.project.ClassRef;
import kz.greetgo.cmd.core.project.ClassType;
import kz.greetgo.cmd.core.project.Project;
import kz.greetgo.cmd.core.util.Name;
import kz.greetgo.depinject.core.Bean;
import kz.greetgo.depinject.core.BeanGetter;
import kz.greetgo.mvc.annotations.AsIs;
import kz.greetgo.mvc.annotations.Par;
import kz.greetgo.mvc.annotations.on_methods.ControllerPrefix;
import kz.greetgo.mvc.annotations.on_methods.OnGet;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static kz.greetgo.cmd.core.util.StrUtil.firstLower;

public class CommandNewControllerApplier {

  private final Project project;
  private final Name name;

  public CommandNewControllerApplier(Project project, String name) {
    this.project = project;
    this.name = Name.parse(name);
  }

  private ClassRef controllerRef;
  private ClassRef registerRef;
  private ClassRef registerImplRef;
  private ClassRef mybatisDaoRef;
  private final List<ClassRef> mybatisDaoDbRefList = new ArrayList<>();
  private ClassRef registerTestRef;

  private ClassRef mybatisTestDaoRef;
  private final List<ClassRef> mybatisTestDaoDbRefList = new ArrayList<>();


  public void execute() {
    controllerRef = project.getControllerPackageRef().createClassRef(name.appendToName( "Controller"));
    registerRef = project.getRegisterInterfacePackageRef().createClassRef(name.appendToName("Register"));
    registerImplRef = project.getRegisterImplPackageRef().createClassRef(name.appendToName("RegisterImpl"));
    mybatisDaoRef = project.getMybatisDaoPackageRef().createClassRef(name.appendToName("Dao"));
    mybatisTestDaoRef = project.getMybatisTestDaoPackageRef().createClassRef(name.appendToName("TestDao"));

    initDaoList(mybatisDaoDbRefList, mybatisDaoRef, "Dao");
    initDaoList(mybatisTestDaoDbRefList, mybatisTestDaoRef, "TestDao");

    registerTestRef = project.getRegisterImplTestPackageRef().createClassRef(name.appendToName("RegisterImplTest"));

    generateController();
    generateRegisterInterface();
    generateRegisterImpl();
    generateDao();
    generateTestDao();
    generateRegisterImplTest();

    controllerRef.save();
    registerRef.save();
    registerImplRef.save();
    mybatisDaoRef.save();
    mybatisDaoDbRefList.forEach(ClassRef::save);
    registerTestRef.save();
    mybatisTestDaoRef.save();
    mybatisTestDaoDbRefList.forEach(ClassRef::save);

  }

  private void initDaoList(List<ClassRef> daoList, ClassRef topRef, String suffix) {
    for (String database : project.getMybatisDaoDatabases()) {
      daoList.add(
        topRef.inPackage.subPackage(database.toLowerCase())
          .createClassRef(name.killSubPackage().renameTo(name.simpleName() + suffix + database))
      );
    }
  }

  private void generateController() {
    controllerRef.beforeClass.pr("@").cl(Bean.class).prn();
    controllerRef.beforeClass.pr("@").cl(ControllerPrefix.class)
      .prn("(\"/" + name.slashesPackageAndNameWithMinuses() + "\")");

    {
      String controllerMarkerInterface = project.getControllerMarkerInterface();
      if (controllerMarkerInterface != null) {
        controllerRef.implement(controllerMarkerInterface);
      }
    }

    String registerVar = firstLower(name.simpleName()) + "Register";

    controllerRef.content.prn(1);
    controllerRef.content.pr(1, "public ").cl(BeanGetter.class)
      .pr("<").cl(registerRef.fullName()).pr("> ")
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
    registerRef.type = ClassType.INTERFACE;
    registerRef.content.prn(1);
    registerRef.content.pr(1, "String getById(String id);").prn();
    registerRef.content.prn(1);
  }

  private void generateRegisterImpl() {
    registerImplRef.beforeClass.pr("@").cl(Bean.class).prn();

    registerImplRef.content.prn(1);

    String daoVar = firstLower(name.simpleName()) + "Dao";

    registerImplRef.content
      .pr(1, "public ").cl(BeanGetter.class).pr("<").cl(mybatisDaoRef.fullName()).prn("> " + daoVar + ";");

    registerImplRef.content.prn(1);

    registerImplRef.content.prn(1, "@Override");
    registerImplRef.content.prn(1, "public String getById(String id) {");
    registerImplRef.content.prn(2, "return " + daoVar + ".get().getById(id);");
    registerImplRef.content.prn(1, "}");

    registerImplRef.content.prn(1);

    registerImplRef.implement(registerRef.fullName());
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

  private void generateTestDao() {
    mybatisTestDaoRef.type = ClassType.INTERFACE;

    for (ClassRef ref : mybatisTestDaoDbRefList) {
      ref.beforeClass.pr("@").cl(Bean.class).prn();
      ref.setParent(mybatisTestDaoRef.fullName());
      ref.type = ClassType.INTERFACE;
    }
  }

  private void generateRegisterImplTest() {

    for (String beforeClass : project.getRegisterImplTestBeforeClass()) {
      registerTestRef.beforeClass.pri(beforeClass).prn();
    }

    String extendsClass = project.getRegisterImplTestExtends();
    if (extendsClass != null) {
      registerTestRef.setParent(extendsClass);
    }

    registerTestRef.content.prn();

    registerTestRef.content.prn();

    String registerVar = firstLower(name.simpleName()) + "Register";

    registerTestRef.content
      .pr(1, "public ").cl(BeanGetter.class).pr("<").cl(registerRef.fullName()).prn("> " + registerVar + ";");

    registerTestRef.content.prn();

    String daoVar = firstLower(name.simpleName()) + "TestDao";

    registerTestRef.content
      .pr(1, "public ").cl(BeanGetter.class).pr("<").cl(mybatisTestDaoRef.fullName()).prn("> " + daoVar + ";");

    registerTestRef.content.prn();

    registerTestRef.importStatic("org.fest.assertions.api.Assertions.assertThat");

    registerTestRef.content.pr(1, "@").cl(Test.class).prn();
    registerTestRef.content.prn(1, "public void test() {");
    registerTestRef.content.prn(2);
    registerTestRef.content.prn(2, "assertThat(" + registerVar + ").isNotNull();");
    registerTestRef.content.prn(2);
    registerTestRef.content.prn(2, "assertThat(" + daoVar + ").isNotNull();");
    registerTestRef.content.prn(2);
    registerTestRef.content.prn(1, "}");

    registerTestRef.content.prn();

  }
}
