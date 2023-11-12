package com.zikeyang.contube.runtime;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;

@Getter
public class ConTubeConfig {
  private List<TubeType> tubeType;
  @JsonProperty(required = true)
  private String conType;

  @Getter
  public static class TubeType {
    @JsonProperty(required = true)
    private String name;
    @JsonProperty(required = true, value = "class")
    private String tubeClass;
  }
}
