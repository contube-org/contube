package com.zikeyang.contube.runtime;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Arrays;
import java.util.stream.Collectors;
import lombok.Getter;

public enum TubeType {
  SOURCE("source", SourceTube.class),
  SINK("sink", SinkTube.class);

  private final String value;
  @Getter
  private final Class<? extends Tube> tubeClass;

  TubeType(String value, Class<? extends Tube> tubeClass) {
    this.value = value;
    this.tubeClass = tubeClass;
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
