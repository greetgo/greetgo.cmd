package kz.greetgo.cmd.core.local_params;

import kz.greetgo.util.RND;
import org.testng.annotations.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.fest.assertions.api.Assertions.assertThat;

public class DateAcceptorInFileTest {
  @Test
  public void get_set() {
    Path file = Paths.get("build", "DateAcceptorInFileTest", "get_set_" + RND.intStr(10) + ".txt");
    String dateFormat = "yyyy-MM-dd HH:mm:ss.SSS";

    DateAcceptorInFile acceptor = new DateAcceptorInFile(file, dateFormat);

    //
    //
    Date date1 = acceptor.get();
    //
    //

    assertThat(date1).isNull();

    Date dateExpected = RND.dateYears(-100, 0);

    //
    //
    acceptor.set(dateExpected);
    //
    //

    assertThat(Files.exists(file)).isTrue();

    //
    //
    Date dateActual = acceptor.get();
    //
    //

    SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

    assertThat(sdf.format(dateActual)).isEqualTo(sdf.format(dateExpected));
  }
}