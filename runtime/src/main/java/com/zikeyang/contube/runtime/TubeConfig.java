package com.zikeyang.contube.runtime;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

@Getter
public class TubeConfig {
  static final String DEFAULT_SOURCE_TUBE_NAME = "__default_source";
  static final String DEFAULT_SINK_TUBE_NAME = "__default_sink";
  @JsonProperty(required = true)
  private TubeType type;

  @JsonProperty(required = true, value = "class")
  private String clazz;

  private String name;

  private String sinkTubeName;

  private Map<String, Object> config;

  public Map<String, Object> getConfig() {
    return config == null ? new HashMap<>(0) : config;
  }

  public String getName() {
    if (name != null) {
      return name;
    }
    return switch (type) {
      case SOURCE -> DEFAULT_SOURCE_TUBE_NAME;
      case SINK -> DEFAULT_SINK_TUBE_NAME;
    };
  }

  public String getSinkTubeName() {
    if (sinkTubeName != null) {
      return sinkTubeName;
    }
    return switch (type) {
      case SOURCE -> DEFAULT_SINK_TUBE_NAME;
      case SINK -> "";
    };
  }
}
