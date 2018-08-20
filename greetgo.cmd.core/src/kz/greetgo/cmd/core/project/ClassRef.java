package kz.greetgo.cmd.core.project;

import kz.greetgo.cmd.core.util.PathUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

public class ClassRef {
  private final Path sourceDir;
  private final String packagePath;
  private final String name;

  private final Imports imports;
  public final Content beforeClass;
  public final Content content;

  public ClassType type = ClassType.CLASS;

  private String parent = null;

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

  public ClassRef implement(Class<?> aClass) {
    return implement(aClass.getName());
  }

  public ClassRef(Path sourceDir, String packagePath, String name) {
    this.sourceDir = sourceDir;
    this.packagePath = packagePath;
    this.name = name;
    imports = new Imports(packagePath);
    content = new Content(imports);
    beforeClass = new Content(imports);
  }

  public String fullName() {
    return PathUtil.toPoints(packagePath) + "." + name;
  }

  public String text() {
    StringBuilder ret = new StringBuilder();

    ret.append("package ").append(PathUtil.toPoints(packagePath)).append(";\n");
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
    File file = sourceDir.resolve(packagePath).resolve(name + ".java").toFile();
    file.getParentFile().mkdirs();
    try {
      Files.write(file.toPath(), text().getBytes(UTF_8));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
