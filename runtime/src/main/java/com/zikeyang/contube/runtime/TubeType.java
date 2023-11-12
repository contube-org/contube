package com.zikeyang.contube.runtime;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Arrays;
import java.util.stream.Collectors;

public enum TubeType {
  SOURCE("source"),
  SINK("sink");

  private final String value;

  TubeType(String value) {
    this.value = value;
  }

  @JsonCreator
  public static TubeType forValue(String value) {
    for (TubeType type : TubeType.values()) {
      if (type.value.equals(value)) {
        return type;
      }
    }
    String supportedTypes = Arrays.stream(TubeType.values())
        .map(TubeType::toString)
        .collect(Collectors.joining(", "));
    throw new IllegalArgumentException(
        "Invalid tube type: " + value + ". Supported types: " + supportedTypes);
  }

  @Override
  public String toString() {
    return value;
  }
}
