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

  private final LinkedHashMap<String, ProjectTemplate> templateMap = new LinkedHashMap<>();

  {
    addTemplate("depinject-vue-vuex", "https://github.com/greetgo/greetgo.templates.git", "Server based on depinject.\nClient - one page, used VueJS with vuex (vuex-typex)");
    addTemplate("depinject-angular", "https://github.com/greetgo/greetgo.templates.git", "Server based on depinject.\nClient - one page, used angular");
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
  public void exec(List<String> subList) {

    if (subList.size() >= 1) {
      projectName = subList.get(0);
    }

    if (subList.size() < 2) {
      System.err.println("Incomplete command. Usage: \n");
      usage();
      throw new SimpleExit(1);
    }

    templateName = subList.get(1);

    if (!templateMap.containsKey(templateName)) {
      unknownTemplate();
      throw new SimpleExit(1);
    }

    if (subList.size() == 2) {
      listVariants();
      throw new SimpleExit(1);
    }

    templateVariant = subList.get(2);

    if (subList.size() == 3) {
      executeCommand();
      return;
    }

    System.err.println("Too many command arguments. Usage:\n");

    usage();
    throw new SimpleExit(1);
  }

  private void unknownTemplate() {
    System.err.println("Unknown template `" + templateName + "'. Usage:\n");
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
    System.err.println("  " + cmdPrefix + " [project | p] <ProjectName> <TemplateName> <Variant>");
    System.err.println("      ");
    System.err.println("      Creates new project with name <ProjectName> based on template <TemplateName> with variant <Variant>");
    System.err.println("      ");
    System.err.println("      You can use following templates:");

    String prefixSpace = StrUtil.spaces(8);

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
      String space = StrUtil.spaces(name.length());
      String description = Arrays.stream(pt.description.split("\n")).map(String::trim).collect(Collectors.joining("\n" + space));

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

  private void executeCommand() {
    Path gitPath = prepareTemplate();

    Set<String> variantSet = Git.listRemoteBranches(gitPath)
        .stream()
        .map(this::extractVariant)
        .filter(Objects::nonNull)
        .collect(Collectors.toSet());

    if (!variantSet.contains(templateVariant)) {
      System.err.println("Unknown variant `" + templateVariant + "'.");
      listVariants();
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
    return Locations.localNewProject().resolve("templates-001");
  }

  private Path gitRepoPath(String templateName) {
    return templatesDir().resolve(templateName).resolve("git-repo");
  }

  private String extractVariant(String branchName) {
    if (!branchName.startsWith("t-" + templateName + "-")) { return null; }
    return branchName.substring(templateName.length() + 3);
  }

  private String templateBranchName() {
    return "t-" + templateName + "-" + templateVariant;
  }

  private void listVariants() {
    System.err.println("You an use following variants:\n");
    String cmd = cmdPrefix + " project " + projectName + ' ' + templateName + ' ';
    System.err.println("list variants of: " + cmd);

    Path gitPath = prepareTemplate();

    Git.listRemoteBranches(gitPath)
        .stream()
        .map(this::extractVariant)
        .filter(Objects::nonNull)
        .forEachOrdered(variant -> System.err.println(cmd + variant));

  }

  private Path prepareTemplate() {
    ProjectTemplate projectTemplate = templateMap.get(templateName);
    if (projectTemplate == null) {
      unknownTemplate();
      throw new SimpleExit(1);
    }

    Path gitPath = gitRepoPath(templateName);

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
