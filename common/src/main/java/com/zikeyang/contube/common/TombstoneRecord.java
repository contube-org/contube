package com.zikeyang.contube.common;

import com.zikeyang.contube.api.TubeRecord;

public class TombstoneRecord implements TubeRecord {
  public static TombstoneRecord instance = new TombstoneRecord();
  @Override
  public byte[] getValue() {
    return null;
  }
}
