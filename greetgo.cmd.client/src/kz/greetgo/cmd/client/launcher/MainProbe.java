package kz.greetgo.cmd.client.launcher;

import kz.greetgo.cmd.core.AsdCore;

import java.util.Arrays;
import java.util.stream.Collectors;

public class MainProbe {
  public static void main(String[] args) {
    new MainProbe().execute(args);
  }

  private void execute(String[] args) {
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
