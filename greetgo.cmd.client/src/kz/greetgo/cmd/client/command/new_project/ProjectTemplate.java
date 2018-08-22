package kz.greetgo.cmd.client.command.new_project;

public class ProjectTemplate {
  public final String name;
  public final String gitUrl;
  public final String description;

  private ProjectTemplate(String name, String gitUrl, String description) {
    this.name = name;
    this.gitUrl = gitUrl;
    this.description = description;
  }

  public static ProjectTemplate of(String name, String gitUrl, String description) {
    return new ProjectTemplate(name, gitUrl, description);
  }
}
