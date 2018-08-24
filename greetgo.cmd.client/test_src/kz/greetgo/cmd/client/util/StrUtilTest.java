package kz.greetgo.cmd.client.util;

import kz.greetgo.cmd.core.util.StrUtil;
import org.testng.annotations.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class StrUtilTest {
  @Test
  public void toUnderscore() {
    //
    //
    String result = StrUtil.toUnderscore("helloBigWorld");
    //
    //

    assertThat(result).isEqualTo("hello_big_world");
  }

  @Test
  public void toUnderscore_fromBig() {
    //
    //
    String result = StrUtil.toUnderscore("HelloSmallWorld");
    //
    //

    assertThat(result).isEqualTo("hello_small_world");
  }

  @Test
  public void toUnderscore_fromManyBigs() {
    //
    //
    String result = StrUtil.toUnderscore("FBIHelloMediumWorld");
    //
    //

    assertThat(result).isEqualTo("fbi_hello_medium_world");
  }

  @Test
  public void toUnderscore_middleManyBigs() {
    //
    //
    String result = StrUtil.toUnderscore("HelloCIAWorld");
    //
    //

    assertThat(result).isEqualTo("hello_cia_world");
  }


  @Test
  public void toUnderscore_endManyBigs() {
    //
    //
    String result = StrUtil.toUnderscore("HelloWorldFRS");
    //
    //

    assertThat(result).isEqualTo("hello_world_frs");
  }

  @Test
  public void toUnderscore_manyBigs() {
    //
    //
    String result = StrUtil.toUnderscore("CIAHelloCKKPSSWorldFBI");
    //
    //

    assertThat(result).isEqualTo("cia_hello_ckkpss_world_fbi");
  }
}