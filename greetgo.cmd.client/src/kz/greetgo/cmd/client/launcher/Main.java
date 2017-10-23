package kz.greetgo.cmd.client.launcher;

import kz.greetgo.depinject.Depinject;

public class Main {
  public static void main(String[] args) {
    Depinject.newInstance(ClientBeanContainer.class).commander().execute(args);
  }
}
