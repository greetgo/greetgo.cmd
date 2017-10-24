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
    System.out.println("CWD = " + System.getenv("CWD"));
    System.out.println("ARGS: " + Arrays.stream(args).collect(Collectors.joining(" - ")));
    System.out.println(AsdCore.asd("Hi"));
    String implementationVersion = getClass().getPackage().getImplementationVersion();
    System.out.println("implementationVersion = " + implementationVersion);
    String specificationVersion = getClass().getPackage().getSpecificationVersion();
    System.out.println("specificationVersion = " + specificationVersion);
  }
}
