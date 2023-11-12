package com.zikeyang.contube.api;

public interface TubeRecord {
  public static final TombstoneRecord TOMBSTONE_RECORD = new TombstoneRecord();

  byte[] getValue();
}
