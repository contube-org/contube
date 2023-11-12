package com.zikeyang.contube.example.file;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class FileTubeConfig {
  @JsonProperty(required = true)
  private String path;
}
