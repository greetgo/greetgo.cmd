package kz.greetgo.cmd.core.util;

import org.testng.annotations.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class CmdUtilTest {

  @Test
  public void nanosToStrSec_big() {
    //
    //
    String str = CmdUtil.nanosToStrSec(123 * CmdUtil.GIG);
    //
    //

    assertThat(str).isEqualTo("123s");
  }

  @Test
  public void nanosToStrSec_small() {
    //
    //
    String str = CmdUtil.nanosToStrSec(123_000);
    //
    //

    assertThat(str).isEqualTo("0.000123s");
  }

  @Test
  public void nanosToStrSec_verySmall() {
    //
    //
    String str = CmdUtil.nanosToStrSec(123_000_000);
    //
    //

    assertThat(str).isEqualTo("0.123s");
  }
}