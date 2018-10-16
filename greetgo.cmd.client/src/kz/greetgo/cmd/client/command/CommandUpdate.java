package kz.greetgo.cmd.client.command;

import kz.greetgo.cmd.core.errors.SimpleExit;
import kz.greetgo.cmd.core.git.Git;
import kz.greetgo.cmd.core.util.AppUtil;
import kz.greetgo.util.ServerUtil;

import java.io.InputStream;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Objects;

import static kz.greetgo.cmd.core.util.CmdUtil.executeCommand;
import static kz.greetgo.cmd.core.util.CmdUtil.outStd;

public class CommandUpdate extends CommandAbstract {
  @Override
  public void printShortHelpTo(PrintStream out) {
    out.println("  " + usedCommand + " " + name());
    out.println("      Updates " + usedCommand + " (greetgo-cli) in current branch to last state");
  }

  @Override
  public void exec(List<String> argList) throws SimpleExit {
    Git.pull(Paths.get("."));
    outStd(executeCommand(Paths.get("."), "./gradlew", "clean").ok());
  }

  public void checkNeedUpdate() {
    if (needToUpdate() || true) {
      checkToUpdate();
      localParams.lastUpdateCheckedAt().set(new Date());
    }
  }

  private boolean needToUpdate() {
    Date lastCheckedAt = localParams.lastUpdateCheckedAt().get();
    if (lastCheckedAt == null) {
      localParams.lastUpdateCheckedAt().set(new Date());
      return false;
    }
    {
      Calendar calendar = new GregorianCalendar();
      calendar.setTime(lastCheckedAt);
      calendar.add(Calendar.MINUTE, 15);
      return new Date().after(calendar.getTime());
    }
  }

  private static String getRemoteVersion() {
    try {

      URL url = new URL("https://raw.githubusercontent.com/greetgo/greetgo.cmd/master/version.txt");
      URLConnection connection = url.openConnection();
      try (InputStream inputStream = connection.getInputStream()) {
        return ServerUtil.streamToStr(inputStream);
      }

    } catch (RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private void checkToUpdate() {
    String remoteVersion = getRemoteVersion();
    String version = AppUtil.version();

    if (Objects.equals(remoteVersion, version)) {
      return;
    }

    System.err.println("*** There is new version " + remoteVersion + " ***");
    System.err.println("To upgrade enter command:");
    System.err.println(usedCommand + " " + name());
  }
}
