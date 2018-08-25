package kz.greetgo.cmd.core.copier;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class TemplateCopier {
  private Path from;
  private Path to;

  private TemplateCopier() {}

  public static TemplateCopier of() {
    return new TemplateCopier();
  }

  public TemplateCopier from(Path from) {
    this.from = from;
    return this;
  }

  public TemplateCopier to(Path to) {
    this.to = to;
    return this;
  }

  public void copy() {

    FileCopier root = FileCopier.root(from.toFile());
    root.modifierExt = ".modifier.txt";
    root.checkIsFileTextual = this::isFileTextual;

    root.base = to.toFile().getParentFile().toPath();
    root.toName = to.toFile().getName();
    root.variableMap = variableMap;

    root.fillChildren();

    root.readModifier();

    //    root.showYourself();

    root.apply();
  }

  private boolean isFileTextual(File file) {
    String name = file.getName().toLowerCase();

    //noinspection PointlessBooleanExpression
    return false
      || name.endsWith(".java")
      || name.endsWith(".js")
      || name.endsWith(".ts")
      || name.endsWith(".sh")
      || name.endsWith(".bash")
      || name.endsWith(".bat")

      || name.endsWith(".vue")

      || name.endsWith(".gradle")
      || name.endsWith(".txt")
      || name.endsWith(".xml")
      || name.endsWith(".md")
      || name.endsWith(".json")
      || name.endsWith(".html")

      || name.endsWith(".css")
      || name.endsWith(".scss")
      || name.endsWith(".sass")
      || name.endsWith(".less")

      || name.endsWith(".jsp")
      || name.endsWith(".jspx")
      || name.endsWith(".asp")
      || name.endsWith(".aspx")

      || name.endsWith(".svg")

      || name.startsWith(".env.")
      || name.equals(".gitignore")
      ;
  }

  private final Map<String, String> variableMap = new HashMap<>();

  public TemplateCopier setVariable(String varName, String varValue) {
    variableMap.put(varName, varValue);
    return this;
  }
}
