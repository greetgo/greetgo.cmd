package kz.greetgo.cmd.core.local_params;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static java.nio.charset.StandardCharsets.UTF_8;

public class DateAcceptorInFile implements DateAcceptor {

  private final Path file;
  private final String dateFormat;

  public DateAcceptorInFile(Path file, String dateFormat) {
    this.file = file;
    this.dateFormat = dateFormat;
  }

  @Override
  public Date get() {
    if (!Files.exists(file)) {
      return null;
    }

    try {
      String content = new String(Files.readAllBytes(file), UTF_8).trim();
      SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

      try {
        return sdf.parse(content);
      } catch (ParseException e) {
        return null;
      }

    } catch (IOException e) {
      throw new RuntimeException(e);
    }

  }

  @Override
  public void set(Date date) {
    try {

      if (date == null) {
        Files.delete(file);
        return;
      }

      SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
      String content = sdf.format(date);

      file.toFile().getParentFile().mkdirs();
      Files.write(file, content.getBytes(UTF_8));

    } catch (IOException e) {
      throw new RuntimeException(e);
    }


  }
}
