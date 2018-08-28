package kz.greetgo.cmd.core.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

public class CmdUtil {

  public static void outStd(CmdResult ok) {
    System.out.println(String.join("\n", ok.stdOut));
    System.err.println(String.join("\n", ok.stdErr));
  }

  private enum ErrorType {
    OK, ERR_CODE, EXCEPTION;
  }

  public static CmdResult executeCommand(Path currentDir, String... cmd) {

    Date startedAtDate = new Date();
    final long startedAt = System.nanoTime();

    final File stdOutTmpFile, stdErrTmpFile;

    try {
      stdOutTmpFile = File.createTempFile("command-std-err", "tmp");
      stdErrTmpFile = File.createTempFile("command-std-out", "tmp");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    final List<Exception> errors = new ArrayList<>();

    Integer exitCode = null;

    ErrorType errorType = ErrorType.OK;

    try {

      currentDir.toFile().mkdirs();

      exitCode = new ProcessBuilder()
        .command(cmd)
        .directory(currentDir.toFile())
        .redirectInput(ProcessBuilder.Redirect.INHERIT)
        .redirectOutput(stdOutTmpFile)
        .redirectError(stdErrTmpFile)
        .start()
        .waitFor();

      if (exitCode != 0) {
        errorType = ErrorType.ERR_CODE;
      }

    } catch (RuntimeException | InterruptedException | IOException e) {
      errors.add(e);
      errorType = ErrorType.EXCEPTION;
    }

    final long delayNanos = System.nanoTime() - startedAt;

    List<String> stdOut = new ArrayList<>();
    try {
      stdOut = Files.readAllLines(stdOutTmpFile.toPath());
    } catch (IOException e) {
      errors.add(e);
    }

    List<String> stdErr = new ArrayList<>();
    try {
      stdErr = Files.readAllLines(stdErrTmpFile.toPath());
    } catch (IOException e) {
      errors.add(e);
    }

    logCmdExecute(String.join(" ", cmd), exitCode,
      startedAtDate, delayNanos,
      errorType, errors,
      stdOut, stdErr);

    stdOutTmpFile.delete();
    stdErrTmpFile.delete();

    if (errorType != ErrorType.EXCEPTION) {
      return CmdResult.of(String.join(" ", cmd), exitCode, stdOut, stdErr);
    }

    {
      Exception firstError = errors.get(0);

      if (firstError instanceof RuntimeException) {
        throw (RuntimeException) firstError;
      }

      throw new RuntimeException(firstError);
    }
  }

  private static void logCmdExecute(String cmd, Integer exitCode,
                                    Date startedAt, long delayNanos, ErrorType errorType,
                                    List<Exception> errors,
                                    List<String> stdOut, List<String> stdErr) {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    StringBuilder sb = new StringBuilder();
    sb.append("        COMMAND : ").append(cmd).append('\n');
    sb.append("             AT : ").append(sdf.format(startedAt)).append('\n');
    sb.append("          DELAY : ").append(nanosToStrSec(delayNanos)).append('\n');
    sb.append("           TYPE : ").append(errorType).append('\n');
    sb.append("      EXIT_CODE : ").append(exitCode == null ? "<Unknown>" : "" + exitCode).append('\n');

    for (Exception error : errors) {
      StringWriter sw = new StringWriter();
      PrintWriter pw = new PrintWriter(sw);
      error.printStackTrace(pw);
      pw.flush();
      sb.append("----------------------------- EXCEPTION\n").append(sw.toString());
    }

    if (stdOut.size() > 0) {
      sb.append("----------------------------- STD_OUT\n").append(String.join("\n", stdOut)).append('\n');
    }
    if (stdErr.size() > 0) {
      sb.append("----------------------------- STD_ERR\n").append(String.join("\n", stdErr)).append('\n');
    }

    sb.append("###############################################################################\n\n");

    File logFile = Locations.logs().resolve("command-executes.log").toFile();

    if (!logFile.exists()) {
      logFile.getParentFile().mkdirs();
    }

    try (FileOutputStream outputStream = new FileOutputStream(logFile, true)) {
      outputStream.write(sb.toString().getBytes(UTF_8));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static final long GIG = 1_000_000_000L;

  public static String nanosToStrSec(long delayNanos) {
    DecimalFormat format = new DecimalFormat("0.######");
    DecimalFormatSymbols sym = new DecimalFormatSymbols();
    sym.setDecimalSeparator('.');
    sym.setGroupingSeparator(' ');
    format.setDecimalFormatSymbols(sym);
    return format.format((double) delayNanos / (double) GIG) + "s";
  }

  public static CmdResult runCommand(Path currentDir, String command) {
    return executeCommand(currentDir, command.split("\\s+"));
  }
}
