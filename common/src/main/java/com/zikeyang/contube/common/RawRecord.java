package com.zikeyang.contube.common;

import com.zikeyang.contube.api.AbstractTubeRecord;
import java.util.Optional;
import lombok.Builder;

@Builder
public class RawRecord extends AbstractTubeRecord {
  private final byte[] value;
  private final Optional<byte[]> schemaData;
  private final Optional<byte[]> index;
  @Override
  public byte[] getValue() {
    return value;
  }

  @Override
  public Optional<byte[]> getSchemaData() {
    return schemaData;
  }

  @Override
  public Optional<byte[]> getIndex() {
    return index;
  }
}
