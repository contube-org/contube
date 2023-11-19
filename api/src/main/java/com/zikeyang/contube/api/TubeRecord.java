package com.zikeyang.contube.api;

import java.util.Optional;

public interface TubeRecord {
  public static final TombstoneRecord TOMBSTONE_RECORD = new TombstoneRecord();

  byte[] getValue();

  default Optional<byte[]> getSchemaData() {
    return Optional.empty();
  }
}
