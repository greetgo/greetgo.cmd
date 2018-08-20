package kz.greetgo.cmd.core.project;

import kz.greetgo.cmd.core.util.PathUtil;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Imports {
  private final String currentPackagePath;

  public Imports(String currentPackagePath) {
    this.currentPackagePath = PathUtil.toPoints(currentPackagePath);
  }

  private final Map<String, String> names = new HashMap<>();
  private final Set<String> packageNames = new HashSet<>();

  public String name(String fullName) {
    if (fullName == null) return "";
    int index = fullName.lastIndexOf('.');
    if (index < 0) return fullName;
    String shortName = fullName.substring(index + 1);
    if (fullName.substring(0, index).equals(currentPackagePath)) {
      packageNames.add(shortName);
      return shortName;
    }
    if (packageNames.contains(shortName)) {
      return fullName;
    }
    String currentShortName = names.get(fullName);
    if (shortName.equals(currentShortName)) return shortName;
    if (currentShortName == null) {
      names.put(shortName, fullName);
      return shortName;
    }
    return fullName;
  }

  public String name(Class<?> aClass) {
    return name(aClass.getName());
  }

  public List<String> content() {
    return names.values().stream().sorted().map(s -> "import " + s + ";").collect(Collectors.toList());
  }
}
