package kz.greetgo.cmd.client.launcher;

import kz.greetgo.cmd.client.beans.BeanConfigClient;
import kz.greetgo.cmd.client.beans.Commander;
import kz.greetgo.cmd.core.config.BeanConfigShareConfig;
import kz.greetgo.depinject.core.BeanContainer;
import kz.greetgo.depinject.core.Include;

@Include({BeanConfigClient.class, BeanConfigShareConfig.class})
public interface ClientBeanContainer extends BeanContainer {
  Commander commander();
}
