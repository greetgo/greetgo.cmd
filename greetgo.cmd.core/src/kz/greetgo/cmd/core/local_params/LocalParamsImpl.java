package kz.greetgo.cmd.core.local_params;

import java.nio.file.Path;

public class LocalParamsImpl implements LocalParams {

  private final Path lastUpdateCheckedAtFile;

  public LocalParamsImpl(Path lastUpdateCheckedAtFile) {
    this.lastUpdateCheckedAtFile = lastUpdateCheckedAtFile;
  }

  @Override
  public DateAcceptor lastUpdateCheckedAt() {
    return new DateAcceptorInFile(lastUpdateCheckedAtFile, "yyyy-MM-dd HH:mm:ss");
  }
}
