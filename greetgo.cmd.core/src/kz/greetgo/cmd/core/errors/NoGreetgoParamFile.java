package kz.greetgo.cmd.core.errors;

public class NoGreetgoParamFile extends RuntimeException {
  public NoGreetgoParamFile(String message) {
    super(message);
  }
}
