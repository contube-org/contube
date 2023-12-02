package com.zikeyang.contube.common;

import com.zikeyang.contube.api.TubeRecord;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import lombok.Builder;

public class RawRecord implements TubeRecord {
  private final byte[] value;
  @Builder.Default
  private final byte[] schemaData;
  @Builder.Default
  private final byte[] index;
  CompletableFuture<Void> commitFuture = new CompletableFuture<>();

  @Builder
  public RawRecord(byte[] value, byte[] schemaData, byte[] index) {
    this.value = value;
    this.schemaData = schemaData;
    this.index = index;
  }

  @Override
  public byte[] getValue() {
    return value;
  }

  @Override
  public Optional<byte[]> getSchemaData() {
    return Optional.ofNullable(schemaData);
  }

  @Override
  public Optional<byte[]> getIndex() {
    return Optional.ofNullable(index);
  }

  @Override
  public CompletableFuture<Void> waitForCommit() {
    return commitFuture;
  }

  @Override
  public void commit() {
    commitFuture.complete(null);
  }
}
