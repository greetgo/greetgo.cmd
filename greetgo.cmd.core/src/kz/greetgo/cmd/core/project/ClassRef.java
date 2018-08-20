package kz.greetgo.cmd.core.project;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static kz.greetgo.cmd.core.util.PathUtil.toPoints;

public class ClassRef {
  private final String name;

  private final Imports imports;
  public final Content beforeClass;
  public final Content content;

  public ClassType type = ClassType.CLASS;

  private String parent = null;

  public final PackageRef inPackage;

  public ClassRef(PackageRef inPackage, String name) {
    this.inPackage = inPackage;
    this.name = name;
    imports = new Imports(inPackage.packagePath);
    content = new Content(imports);
    beforeClass = new Content(imports);
  }

  public void setParent(String parent) {
    this.parent = imports.name(parent);
  }

  public void setParent(Class<?> aClass) {
    this.parent = imports.name(aClass);
  }

  private List<String> implementList = new ArrayList<>();

  public ClassRef implement(String className) {
    implementList.add(imports.name(className));
    return this;
  }

  @SuppressWarnings("UnusedReturnValue")
  public ClassRef implement(Class<?> aClass) {
    return implement(aClass.getName());
  }

  public String fullName() {
    return toPoints(inPackage.packagePath) + "." + name;
  }

  public String text() {
    StringBuilder ret = new StringBuilder();

    ret.append("package ").append(toPoints(inPackage.packagePath)).append(";\n");
    ret.append("\n");
    imports.content().forEach(line -> ret.append(line).append("\n"));
    ret.append("\n");

    ret.append(beforeClass.text());
    ret.append("public ").append(type.name().toLowerCase()).append(" ").append(name);
    if (parent != null) {
      ret.append(" extends ").append(parent);
    }
    if (implementList.size() > 0) {
      ret.append(" implements ").append(String.join(", ", implementList)).append(" ");
    }
    ret.append(" {\n");

    ret.append(content.text());

    ret.append("}\n");
    return ret.toString();
  }

  public void save() {
    File file = inPackage.finishRoot().resolve(name + ".java").toFile();
    file.getParentFile().mkdirs();
    try {
      Files.write(file.toPath(), text().getBytes(UTF_8));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
