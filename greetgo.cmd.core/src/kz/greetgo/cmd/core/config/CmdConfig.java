package kz.greetgo.cmd.core.config;

import kz.greetgo.conf.hot.DefaultIntValue;
import kz.greetgo.conf.hot.Description;

@Description("Основная конфигурация")
public interface CmdConfig {
  @Description("Порт, по которому нужно общаться с сервером")
  @DefaultIntValue(34857)
  int serverPort();
}
