package kz.greetgo.cmd.core.copier;

import java.nio.file.Path;
import java.util.Arrays;
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
    root.binExtList = Arrays.asList(("" +
      " png jpg jpeg jp2 bmp tiff gif psd raw" +
      " jar war zip bz2 tar so" +
      " mp3 mp4 mpeg4 wave wav" +
      " psd".trim()).split("\\s+"));

    root.base = to.toFile().getParentFile().toPath();
    root.toName = to.toFile().getName();
    root.variableMap = variableMap;

    root.fillChildren();

    root.readModifier();

    //    root.showYourself();

    root.apply();
  }

  private final Map<String, String> variableMap = new HashMap<>();

  public TemplateCopier setVariable(String varName, String varValue) {
    variableMap.put(varName, varValue);
    return this;
  }
}
