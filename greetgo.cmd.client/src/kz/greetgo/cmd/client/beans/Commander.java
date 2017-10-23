package kz.greetgo.cmd.client.beans;

import kz.greetgo.depinject.core.Bean;

@Bean
public class Commander {
  public void execute(String[] args) {
    System.out.println("Hello world");
  }
}
