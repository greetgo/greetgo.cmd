package kz.greetgo.cmd.core.project;

public class Content {

  private final Imports imports;

  public Content(Imports imports) {
    this.imports = imports;
  }

  public Content pr(String text) {
    return pr(0, text);
  }

  public Content prn(int ofs, String text) {
    return pr(ofs, text).prn();
  }

  public Content prn() {
    return pr("\n");
  }

  public Content prn(int ofs) {
    return pr(ofs, "\n");
  }

  public Content prn(String text) {
    return prn(0, text);
  }

  private final StringBuilder content = new StringBuilder();

  public Content pr(int ofs, String text) {
    for (int i = 0; i < ofs; i++) {
      content.append("  ");
    }
    content.append(text);
    return this;
  }

  public Content cl(String fullClassName) {
    return pr(imports.name(fullClassName));
  }

  public Content cl(Class<?> aClass) {
    return cl(aClass.getName());
  }

  public String text() {
    return content.toString();
  }

  public Content pri(String tildaText) {
    boolean throughImport = false;
    for (String part : tildaText.split("~")) {
      if (throughImport) {
        cl(part);
      } else {
        pr(part);
      }
      throughImport = !throughImport;
    }
    return this;
  }
}
