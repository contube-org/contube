package com.zikeyang.contube.api;

import lombok.Builder;

@Builder
public class StringRecord implements TubeRecord {
  String value;

  @Override
  public byte[] getValue() {
    return value.getBytes();
  }
}
