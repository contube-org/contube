package com.zikeyang.contube.api;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface TubeRecord {
  public static final TombstoneRecord TOMBSTONE_RECORD = new TombstoneRecord();

  byte[] getValue();

  default Optional<byte[]> getSchemaData() {
    return Optional.empty();
  }

  default Optional<byte[]> getIndex() {
    return Optional.empty();
  }

  default void commit() {
  }

  default CompletableFuture<Void> waitForCommit() {
    return CompletableFuture.completedFuture(null);
  }
}
