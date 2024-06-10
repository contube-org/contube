package io.github.contube.runtime;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Collection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class ConTubeConfig {
  private Collection<TubeType> tubeType;
  @JsonProperty(required = true)
  private String conType;

  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class TubeType {
    @JsonProperty(required = true)
    private String name;
    @JsonProperty(required = true, value = "class")
    private String tubeClass;
  }
}
