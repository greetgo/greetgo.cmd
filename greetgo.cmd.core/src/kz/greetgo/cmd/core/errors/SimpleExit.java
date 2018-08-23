package kz.greetgo.cmd.core.errors;

public class SimpleExit extends RuntimeException {
  public final int exitCode;

  public SimpleExit(int exitCode) {
    super("exitCode = " + exitCode);
    this.exitCode = exitCode;
  }

  public static SimpleExit extract(Throwable e) {
    while (e != null) {
      if (e instanceof SimpleExit) {
        return (SimpleExit) e;
      }

      e = e.getCause();
    }

    return null;
  }
}
