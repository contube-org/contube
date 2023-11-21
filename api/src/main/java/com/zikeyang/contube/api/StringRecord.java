package com.zikeyang.contube.api;

import lombok.Builder;
import lombok.Getter;

@Builder
public class StringRecord implements TubeRecord {
  @Getter
  String stringValue;

  @Override
  public byte[] getValue() {
    return stringValue.getBytes();
  }
}
