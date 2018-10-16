package kz.greetgo.cmd.core.util;

import java.util.Optional;

import static kz.greetgo.cmd.core.util.StrUtil.toUnderscore;

public class Name {

  private final String subPackageName;
  private final String simpleName;

  private Name(String subPackageName, String simpleName) {
    this.subPackageName = subPackageName;
    this.simpleName = simpleName;
  }

  public static Name parse(String nameStr) {
    nameStr = nameStr.trim().replace('/', '.').replace('\\', '.');

    while (nameStr.startsWith(".")) {
      nameStr = nameStr.substring(1);
    }
    while (nameStr.endsWith(".")) {
      nameStr = nameStr.substring(0, nameStr.length() - 1);
    }

    nameStr = nameStr.replaceAll("\\.+", ".");

    int i = nameStr.lastIndexOf('.');

    if (i < 0) {
      return new Name(null, nameStr);
    } else {
      return new Name(nameStr.substring(0, i), nameStr.substring(i + 1));
    }
  }

  public String slashesPackageAndNameWithMinuses() {
    String name = toUnderscore(simpleName).replace('_', '-');

    if (subPackageName == null) {
      return name;
    }

    return subPackageName.replace('.', '/') + '/' + name;
  }

  public String nameWithSlashes() {
    if (subPackageName == null) {
      return simpleName;
    }
    return (subPackageName + "." + simpleName).replace('.', '/');
  }

  public String simpleName() {
    return simpleName;
  }

  public Name renameTo(String nameCanContainsSubPackage) {
    Name newName = Name.parse(nameCanContainsSubPackage);
    return new Name(resolvePackages(subPackageName, newName.subPackageName), newName.simpleName);
  }

  private static String resolvePackages(String packageName1, String packageName2) {
    if (packageName1 == null && packageName2 == null) {
      return null;
    }
    if (packageName1 == null) {
      return packageName2;
    }
    if (packageName2 == null) {
      return packageName1;
    }
    return packageName1 + '.' + packageName2;
  }

  public Optional<String> subPackageName() {
    return Optional.ofNullable(subPackageName);
  }

  public Name killSubPackage() {
    if (subPackageName == null) {
      return this;
    }
    return new Name(null, simpleName);
  }

  public Name appendToName(String suffix) {
    if (suffix == null) {
      return this;
    }
    return new Name(subPackageName, simpleName + suffix);
  }
}
