package kz.greetgo.cmd.core.copier;

import kz.greetgo.cmd.core.util.StrUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;
import static kz.greetgo.cmd.core.util.StrUtil.spaces;

public class FileCopier {

  public final FileCopier parent;
  public final File fromFile;
  public final int level;
  public String toName;

  public final List<FileCopier> children = new ArrayList<>();
  public Path base;
  public Map<String, String> variableMap = null;

  private FileCopier(FileCopier parent, File fromFile, int level) {
    this.parent = parent;
    this.fromFile = fromFile;
    toName = fromFile.getName();
    this.level = level;
  }

  public Map<String, String> getVariableMap() {
    if (variableMap != null) {
      return variableMap;
    }

    if (parent == null) {
      throw new RuntimeException("parent == null");
    }

    return variableMap = parent.getVariableMap();
  }

  public String modifierExt = null;

  public static FileCopier of(FileCopier parent, File fromFile, int level) {
    return new FileCopier(parent, fromFile, level);
  }

  public static FileCopier root(File fromFile) {
    return of(null, fromFile, 0);
  }

  String newLine = "\n";

  public void apply() {
    if (skip) {
      return;
    }

    applyOnlyMine();

    children.forEach(FileCopier::apply);
  }

  private void applyOnlyMine() {
    if (!fromFile.isFile()) {
      return;
    }

    try {

      if (isBinary()) {

        byte[] bytes = Files.readAllBytes(fromFile.toPath());

        writeToDestination(bytes);

      } else {

        List<String> lines = StrUtil.splitOnLines(new String(Files.readAllBytes(fromFile.toPath()), UTF_8));

        lines = modify(lines);

        byte[] bytes = lines.stream().collect(Collectors.joining(newLine)).getBytes(UTF_8);

        writeToDestination(bytes);

      }

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private List<String> modify(List<String> lines) {
    List<String> ret = new ArrayList<>();

    for (int i = 0, size = lines.size(); i < size; i++) {

      String line = lines.get(i);

      if (line.startsWith("///MODIFY ")
        || line.startsWith("###MODIFY ")
      ) {
        if (i == size - 1) {
          throw new RuntimeException("Cannot modify: line " + (i + 1) + " in " + fromFile);
        }

        ret.add(modifyLine(line.substring("###MODIFY ".length()).trim(), lines.get(i + 1), i + 1));

        i++;

        continue;
      }

      ret.add(line);
    }

    return ret;
  }

  private String modifyLine(String modifyLine, String nextLine, int line) {
    String[] split = modifyLine.split("\\s+");
    if (split.length == 3 && split[0].equals("replace")) {
      String regexp = split[1];
      String replacement = updateValue(split[2]);

      Pattern r = Pattern.compile(regexp);

      StringBuffer result = new StringBuffer();
      Matcher matcher = r.matcher(nextLine);
      while (matcher.find()) {
        matcher.appendReplacement(result, replacement);
      }
      matcher.appendTail(result);

      return result.toString();
    }

    throw new RuntimeException("Unknown modify command: `" + modifyLine + "' at line " + line + " in " + fromFile);
  }

  private void writeToDestination(byte[] bytes) throws IOException {
    Path destinationPath = destinationPath();
    destinationPath.toFile().getParentFile().mkdirs();
    Files.write(destinationPath, bytes);
  }

  BinStatus binStatus = BinStatus.AUTO;

  public Predicate<File> checkIsFileTextual = null;

  public Predicate<File> getCheckIsFileTextual() {
    {
      Predicate<File> local = checkIsFileTextual;
      if (local != null) {
        return local;
      }
    }

    return checkIsFileTextual = getLiveParent().getCheckIsFileTextual();
  }

  private boolean isBinary() {
    switch (binStatus) {
      case BIN:
        return true;
      case TXT:
        return false;
    }

    return !getCheckIsFileTextual().test(fromFile);
  }

  public String getModifierExt() {
    {
      String localConfExt = modifierExt;
      if (localConfExt != null) {
        return localConfExt;
      }
    }

    return modifierExt = getLiveParent().getModifierExt();
  }

  boolean modifierWasRead = false;

  public void readModifier() {
    if (modifierWasRead) {
      return;
    }
    modifierWasRead = true;

    doReadModifier();

    children.forEach(FileCopier::readModifier);
  }

  private boolean skip = false;

  private void doReadModifier() {
    File modifierFile = new File(fromFile.getPath() + getModifierExt());

    if (!modifierFile.exists()) {
      return;
    }

    try {
      List<String> lines = Files.readAllLines(modifierFile.toPath());

      for (String line : lines) {
        if (line.trim().isEmpty()) {
          continue;
        }
        if (line.trim().startsWith("#")) {
          continue;
        }

        int eqIdx = line.indexOf('=');
        if (eqIdx < 0) {
          continue;
        }

        String key = line.substring(0, eqIdx).trim();
        String value = line.substring(eqIdx + 1);


        if ("skip".equals(key)) {
          skip = StrUtil.toBool(value);
          continue;
        }

        if ("rename-to".equals(key)) {
          renameTo(value);
          continue;
        }

        throw new RuntimeException("Unknown modifier parameter: " + key + " = " + value);
      }

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private void renameTo(String newName) {
    toName = updateValue(newName.trim());
  }

  private String updateValue(String value) {
    for (Map.Entry<String, String> e : getVariableMap().entrySet()) {
      value = value.replaceAll(e.getKey(), e.getValue());
    }
    return value;
  }

  private FileCopier getLiveParent() {
    FileCopier localParent = parent;
    if (localParent == null) {
      throw new RuntimeException("parent is null");
    }
    return localParent;
  }

  public FileCopier fillChildren() {
    if (!fromFile.isDirectory()) {
      return this;
    }

    File[] files = fromFile.listFiles();

    if (files == null) {
      return this;
    }

    for (File file : files) {
      if (file.getName().endsWith(getModifierExt())) {
        continue;
      }

      children.add(FileCopier.of(this, file, level + 1).fillChildren());
    }

    return this;
  }

  @SuppressWarnings("unused")
  public void showYourself() {
    String end = fromFile.isDirectory() ? "/" : "";
    StringBuilder pr = new StringBuilder(spaces(level * 2) + fromFile.getName() + end);
    for (int i = pr.length(); i < 50; i++) {
      pr.append(i % 3 == 0 ? '.' : ' ');
    }
    System.out.println(pr + " " + destinationPath() + end);
    children.forEach(FileCopier::showYourself);
  }

  private Path destinationPath() {
    if (base != null) {
      return base.resolve(toName);
    }

    if (parent == null) {
      throw new RuntimeException("parent == null");
    }

    return parent.destinationPath().resolve(toName);
  }

}
