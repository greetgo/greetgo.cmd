package kz.greetgo.cmd.client.command.new_project;

import kz.greetgo.cmd.client.command.new_sub.NewSubCommand;
import kz.greetgo.cmd.core.util.Locations;
import kz.greetgo.cmd.core.git.Git;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

public class CommandNewProject extends NewSubCommand {

  private final LinkedHashMap<String, ProjectTemplate> templateMap = new LinkedHashMap<>();

  {
    addTemplate("depinject-vue-vuex",
      "https://github.com/greetgo/greetgo.sandbox.git",
      "Server based on depinject.\nClient - one page, used VueJS with vuex (vuex-typex)");
    addTemplate("depinject-angular",
      "https://github.com/greetgo/greetgo.sandbox.git",
      "Server based on depinject.\nClient - one page, used angular");
  }

  private String projectName = "a-project-name";
  private String templateName = null;
  private String templateVariant = null;

  @SuppressWarnings("SameParameterValue")
  private void addTemplate(String name, String gitUrl, String description) {
    ProjectTemplate t = ProjectTemplate.of(name, gitUrl, description);
    templateMap.put(t.name, t);
  }

  @Override
  public int exec(List<String> subList) {

    if (subList.size() >= 1) {
      projectName = subList.get(0);
    }

    if (subList.size() < 2) {
      System.err.println("Incomplete command. Usage: \n");
      return usage();
    }

    templateName = subList.get(1);

    if (!templateMap.containsKey(templateName)) {
      return unknownTemplate();
    }

    if (subList.size() == 2) {
      return listVariants();
    }

    templateVariant = subList.get(2);

    if (subList.size() == 3) {
      return executeCommand();
    }

    System.err.println("Too many command arguments. Usage:\n");

    return usage();
  }

  private int unknownTemplate() {
    System.err.println("Unknown template `" + templateName + "'. Usage:\n");
    return usage();
  }

  private int usage() {
    printUsage();
    return 1;
  }

  @Override
  public boolean accept(String strSubCommand) {
    return "project".equals(strSubCommand) || "p".equals(strSubCommand);
  }

  private static String spaces(int len) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < len; i++) {
      sb.append(' ');
    }
    return sb.toString();
  }

  private static String toLenRight(String name, int length) {
    StringBuilder sb = new StringBuilder();
    sb.append(name);
    while (sb.length() < length) {
      sb.insert(0, ' ');
    }
    return sb.toString();
  }


  @Override
  public void printUsage() {
    System.err.println("  " + cmdPrefix + " [project | p] <ProjectName> <TemplateName> <Variant>");
    System.err.println("      ");
    System.err.println("      Creates new project with name <ProjectName> based on template <TemplateName> with variant <Variant>");
    System.err.println("      ");
    System.err.println("      You can use following templates:");

    String prefixSpace = spaces(8);

    int maxNameLength = templateMap.values().stream().mapToInt(s -> s.name.length()).max().orElse(1);
    String nameHeader = "<TemplateName>";
    if (maxNameLength < nameHeader.length()) {
      maxNameLength = nameHeader.length();
    }

    {
      System.err.println(prefixSpace);
      String name = prefixSpace + toLenRight(nameHeader, maxNameLength) + " - ";
      System.err.println(name + "<Description>");
    }

    for (ProjectTemplate pt : templateMap.values()) {

      String name = prefixSpace + toLenRight(pt.name, maxNameLength) + " - ";
      String space = spaces(name.length());
      String description = Arrays.stream(pt.description.split("\n"))
        .map(String::trim)
        .collect(Collectors.joining("\n" + space));

      System.err.println(prefixSpace);
      System.err.println(name + description);
    }
    System.err.println(prefixSpace);

    System.err.println("      Note to view list of variants type: '" + cmdPrefix + " project <AnyName> <TemplateName>'");
    for (ProjectTemplate pt : templateMap.values()) {
      System.err.println(//"        " +
        cmdPrefix + " project " + projectName + " " + pt.name);
    }
  }

  private int executeCommand() {
    System.err.println("Unknown variant `" + templateVariant + "' of template `" + templateName + "'.\n");
    return listVariants();
  }

  private static Path templatesDir() {
    return Locations.localNewProject().resolve("templates");
  }

  private Path gitRepoPath(String templateName) {
    return templatesDir().resolve(templateName).resolve("git-repo");
  }

  private int listVariants() {
    System.err.println("You an use following variants:\n");
    String cmd = cmdPrefix + " project " + projectName + ' ' + templateName + ' ';
    System.err.println("list variants of: " + cmd);

    ProjectTemplate projectTemplate = templateMap.get(templateName);
    if (projectTemplate == null) {
      return unknownTemplate();
    }

    Path gitPath = gitRepoPath(templateName);

    if (gitPath.toFile().isDirectory()) {
      Git.fetchAllBranches(gitPath);
    } else {
      File parentFile = gitPath.toFile().getParentFile();
      parentFile.mkdirs();
      Git.gitClone(parentFile.toPath(), projectTemplate.gitUrl, gitPath.toFile().getName());
    }

    for (String branch : Git.listRemoteBranches(gitPath)) {
      if (branch.startsWith("t-" + templateName + "-")) {
        String variant = branch.substring(templateName.length() + 3);
        if (variant.length() > 0) {
          System.err.println(cmd + variant);
        }
      }
    }

    return 1;
  }
}
