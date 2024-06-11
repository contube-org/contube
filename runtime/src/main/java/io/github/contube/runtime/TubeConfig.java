package io.github.contube.runtime;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

@Getter
public class TubeConfig {
  static final String DEFAULT_SOURCE_TUBE_NAME = "__default_source";
  static final String DEFAULT_SINK_TUBE_NAME = "__default_sink";
  static final String DEFAULT_SOURCE_TYPE = "source";
  static final String DEFAULT_SINK_TYPE = "sink";
  @JsonProperty(required = true)
  private String type;

  @JsonProperty(required = true, value = "class")
  private String clazz;

  @JsonProperty(required = true)
  private String name;

  private String sinkTubeName;

  private Map<String, Object> config;

  public Map<String, Object> getConfig() {
    return config == null ? new HashMap<>(0) : config;
  }
}
