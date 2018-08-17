package kz.greetgo.cmd.client.launcher;

import kz.greetgo.cmd.core.AsdCore;

public class Launcher {
  public static void main(String[] args) {
    new Launcher().exec(args);
  }

  private void exec(String[] args) {
    System.out.println("Hello from CMD Client");
    System.out.println("CURRENT_WORKING_DIR = " + System.getenv("CURRENT_WORKING_DIR"));
    System.out.println("ARGS: " + String.join(" - ", args));
    System.out.println(AsdCore.asd("Hi"));
    String implementationVersion = getClass().getPackage().getImplementationVersion();
    System.out.println("implementationVersion = " + implementationVersion);
    String specificationVersion = getClass().getPackage().getSpecificationVersion();
    System.out.println("specificationVersion = " + specificationVersion);
  }
}
