package io.github.contube.common;

import io.github.contube.api.TubeRecord;

public class TombstoneRecord implements TubeRecord {
  public static TombstoneRecord instance = new TombstoneRecord();
  @Override
  public byte[] getValue() {
    return null;
  }
}
