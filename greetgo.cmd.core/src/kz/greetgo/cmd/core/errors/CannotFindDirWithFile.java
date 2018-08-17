package kz.greetgo.cmd.core.errors;

import java.nio.file.Path;

public class CannotFindDirWithFile extends RuntimeException {
  public final String simpleFileName;
  public final Path fromDir;

  public CannotFindDirWithFile(String simpleFileName, Path fromDir) {
    super("FileName = '" + simpleFileName + "', found from dir = '" + fromDir + "'");
    this.simpleFileName = simpleFileName;
    this.fromDir = fromDir;
  }
}
