package kz.greetgo.cmd.client.util;

import kz.greetgo.cmd.client.command.CommandVersion;
import org.testng.annotations.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class CommandUtilTest {
  @Test
  public void calcName() {
    assertThat(CommandUtil.calcName(CommandVersion.class)).isEqualTo("version");
  }
}