package kz.greetgo.cmd.core.util;

import java.util.Objects;

public class ReadResult<ValueType> {

  private enum ResultType {
    NO_FILE, NO_VALUE, OK;
  }

  private final ResultType type;
  private final ValueType value;

  private ReadResult(ResultType type, ValueType value) {
    this.type = type;
    this.value = value;
  }

  @SuppressWarnings("unchecked")
  private static final ReadResult NO_FILE = new ReadResult(ResultType.NO_FILE, null);
  @SuppressWarnings("unchecked")
  private static final ReadResult NO_VALUE = new ReadResult(ResultType.NO_VALUE, null);

  public static <ValueType> ReadResult<ValueType> ofFileAbsent() {
    //noinspection unchecked
    return NO_FILE;
  }

  public static <ValueType> ReadResult<ValueType> ofValueAbsent() {
    //noinspection unchecked
    return NO_VALUE;
  }

  public static <ValueType> ReadResult<ValueType> of(ValueType value) {
    return new ReadResult<>(ResultType.OK, Objects.requireNonNull(value));
  }

  public boolean isFileAbsent() {
    return type == ResultType.NO_FILE;
  }

  public boolean isValueAbsent() {
    return !isPresent();
  }

  @SuppressWarnings("BooleanMethodIsAlwaysInverted")
  public boolean isPresent() {
    return type == ResultType.OK;
  }

  public ValueType get() {
    if (isValueAbsent()) throw new RuntimeException(type == ResultType.NO_FILE ? "No file" : "No value");
    return value;
  }

  public ValueType getOr(ValueType defaultValue) {
    if (isValueAbsent()) return defaultValue;
    return value;
  }

  public ValueType getOrThrow(RuntimeException e) {
    if (isValueAbsent()) throw e;
    return value;
  }

  public ValueType getOrThrow(Exception e) throws Exception {
    if (isValueAbsent()) throw e;
    return value;
  }

  public <AnotherType> ReadResult<AnotherType> cast() {
    if (isPresent()) {
      throw new RuntimeException("Cannot cast value");
    }

    //noinspection unchecked
    return (ReadResult<AnotherType>) this;
  }
}
