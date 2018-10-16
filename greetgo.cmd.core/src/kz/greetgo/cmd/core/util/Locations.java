package kz.greetgo.cmd.core.util;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Locations {

  public static Path local() {
    return Paths.get(System.getProperty("user.home") + "/.local/greetgo/cmd");
  }

  public static Path localNewProject() {
    return local().resolve("new_project");
  }

  public static Path logs() {
    return local().resolve("logs");
  }

  public static Path lastUpdateCheckedAtFile() {
    return local().resolve("params").resolve("last_update_checked_at");
  }
}
