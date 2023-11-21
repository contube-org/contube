package com.zikeyang.contube.api;

import java.util.concurrent.CompletableFuture;

public abstract class AbstractTubeRecord implements TubeRecord {
  CompletableFuture<Void> commitFuture = new CompletableFuture<>();

  @Override
  public CompletableFuture<Void> waitForCommit() {
    return commitFuture;
  }

  @Override
  public void commit() {
    commitFuture.complete(null);
  }
}
