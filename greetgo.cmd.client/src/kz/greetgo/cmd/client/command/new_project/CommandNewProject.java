package kz.greetgo.cmd.client.command.new_project;

import kz.greetgo.cmd.client.command.new_sub.NewSubCommand;
import kz.greetgo.cmd.core.copier.TemplateCopier;
import kz.greetgo.cmd.core.errors.SimpleExit;
import kz.greetgo.cmd.core.git.Git;
import kz.greetgo.cmd.core.util.AppUtil;
import kz.greetgo.cmd.core.util.Locations;
import kz.greetgo.cmd.core.util.StrUtil;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class CommandNewProject extends NewSubCommand {

  private final LinkedHashMap<String, ProjectTemplate> templateGroupMap = new LinkedHashMap<>();

  {
//    addTemplate("depinject-vue-vuex", "https://github.com/greetgo/greetgo.templates.git", "Server based on depinject.\nClient - one page, used VueJS with vuex (vuex-typex)");
//    addTemplate("depinject-angular", "https://github.com/greetgo/greetgo.templates.git", "Server based on depinject.\nClient - one page, used angular");
    addTemplateGroup("greetgo.templates",
        "https://github.com/greetgo/greetgo.templates.git",
        "Common greetgo project templates");
  }

  private String projectName = "a-project-name";

  private String templateGroup = null;
  private String templateName1 = null;

  @SuppressWarnings("SameParameterValue")
  private void addTemplateGroup(String name, String gitUrl, String description) {
    ProjectTemplate t = ProjectTemplate.of(name, gitUrl, description);
    templateGroupMap.put(t.name, t);
  }

  @Override
  public void exec(List<String> subList) {

    if (subList.size() >= 1) {
      projectName = subList.get(0);
    }

    if (subList.size() < 2) {
      System.err.println("Incomplete command. Usage: \n");
      usage();
      throw new SimpleExit(1);
    }

    templateGroup = subList.get(1);

    if (!templateGroupMap.containsKey(templateGroup)) {
      unknownTemplateGroup();
      throw new SimpleExit(1);
    }

    if (subList.size() == 2) {
      listTemplatesInGroup();
      throw new SimpleExit(1);
    }

    templateName1 = subList.get(2);

    if (subList.size() == 3) {
      executeCommand();
      return;
    }

    System.err.println("Too many command arguments. Usage:\n");

    usage();
    throw new SimpleExit(1);
  }

  private void unknownTemplateGroup() {
    System.err.println("Unknown template group `" + templateGroup + "'. Usage:\n");
    usage();
  }

  private void usage() {
    printUsage();
  }

  @Override
  public boolean accept(String strSubCommand) {
    return "project".equals(strSubCommand) || "p".equals(strSubCommand);
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
    System.err.println("  " + cmdPrefix + " [project | p] <ProjectName> <TemplateGroup> <TemplateName>");
    System.err.println("      ");
    System.err.println("      Creates new project with name <ProjectName> based on template <TemplateName>");
    System.err.println("      ");
    System.err.println("      You can use the following template groups:");

    String prefixSpace = StrUtil.spaces(8);

    int maxNameLength = templateGroupMap.values().stream().mapToInt(s -> s.name.length()).max().orElse(1);
    String nameHeader = "<TemplateGroup>";
    if (maxNameLength < nameHeader.length()) {
      maxNameLength = nameHeader.length();
    }

    {
      System.err.println(prefixSpace);
      String name = prefixSpace + toLenRight(nameHeader, maxNameLength) + " - ";
      System.err.println(name + "<Description>");
    }

    for (ProjectTemplate pt : templateGroupMap.values()) {

      String name = prefixSpace + toLenRight(pt.name, maxNameLength) + " - ";
      String space = StrUtil.spaces(name.length());
      String description = Arrays.stream(pt.description.split("\n")).map(String::trim).collect(Collectors.joining("\n" + space));

      System.err.println(prefixSpace);
      System.err.println(name + description);
    }
    System.err.println(prefixSpace);

    System.err.println("      Use the following commands:");
    System.err.println();
    for (ProjectTemplate pt : templateGroupMap.values()) {
      System.err.println(//"        " +
          cmdPrefix + " project " + projectName + " " + pt.name);
    }
    System.err.println();
  }

  private void executeCommand() {
    Path gitPath = prepareTemplateGroup();

    Set<String> templateNameSet = Git.listRemoteBranches(gitPath)
        .stream()
        .map(this::extractTemplateName)
        .filter(Objects::nonNull)
        .collect(Collectors.toSet());

    if (!templateNameSet.contains(templateName1)) {
      System.err.println("Unknown variant `" + templateName1 + "'.");
      listTemplatesInGroup();
      throw new SimpleExit(1);
    }

    Git.checkout(gitPath, templateBranchName());

    File projectDir = AppUtil.currentWorkingDir().resolve(projectName).toFile();

    if (projectDir.exists()) {
      System.err.println("Directory `" + projectName + "' already exists");
      throw new SimpleExit(1);
    }

    if (!projectDir.mkdir()) {
      System.err.println("Cannot create directory `" + projectName + "'");
      throw new SimpleExit(10);
    }

    TemplateCopier.of()
        .from(gitPath)
        .to(projectDir.toPath())
        .setVariable("PROJECT_NAME", projectName)
        .setVariable("PROJECT_CC_NAME", StrUtil.toCamelCase(projectName))
        .setVariable("RND_SALT", StrUtil.generateSalt())
        .copy();

    Git.init(projectDir.toPath());
    Git.addAll(projectDir.toPath());
    Git.commit(projectDir.toPath(), "Created with greetgo-cli by " + System.getProperty("user.name"));
  }

  private static Path templatesDir() {
    return Locations.localNewProject().resolve("templates-002");
  }

  private Path gitRepoPath(String templateName) {
    return templatesDir().resolve(templateName).resolve("git-repo");
  }

  private String extractTemplateName(String branchName) {
    if (!branchName.startsWith("t-")) {
      return null;
    }
    return branchName.substring(2);
  }

  private String templateBranchName() {
    return "t-" + templateName1;
  }

  private void listTemplatesInGroup() {
    System.err.println("You an use following templates:\n");
    String cmd = cmdPrefix + " project " + projectName + ' ' + templateGroup + ' ';
    System.err.println("list templates in group: " + templateGroup);
    System.err.println();

    Path gitPath = prepareTemplateGroup();

    Git.listRemoteBranches(gitPath)
        .stream()
        .map(this::extractTemplateName)
        .filter(Objects::nonNull)
        .forEachOrdered(tName -> System.err.println(cmd + tName));

    System.err.println();
  }

  private Path prepareTemplateGroup() {
    ProjectTemplate projectTemplate = templateGroupMap.get(templateGroup);
    if (projectTemplate == null) {
      unknownTemplateGroup();
      throw new SimpleExit(1);
    }

    Path gitPath = gitRepoPath(templateGroup);

    if (gitPath.toFile().isDirectory()) {
      Git.fetchAllBranches(gitPath);
    } else {
      File parentFile = gitPath.toFile().getParentFile();
      parentFile.mkdirs();
      Git.gitClone(parentFile.toPath(), projectTemplate.gitUrl, gitPath.toFile().getName());
    }

    return gitPath;
  }
}
