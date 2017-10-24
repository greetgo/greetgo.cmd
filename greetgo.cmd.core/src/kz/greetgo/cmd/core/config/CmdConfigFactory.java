package kz.greetgo.cmd.core.config;

import kz.greetgo.conf.hot.HotConfigFactory;
import kz.greetgo.depinject.core.Bean;

@Bean
public class CmdConfigFactory extends HotConfigFactory {
  @Override
  protected String getBaseDir() {
    return System.getProperty("user.home") + "/.config/greetgo";
  }

  @Bean
  public CmdConfig createCmdConfig() {
    return createConfig(CmdConfig.class);
  }
}
