package com.zikeyang.contube.api;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public interface Con extends AutoCloseable {
  CompletableFuture<Void> send(String tubeName, TubeRecord record);

  void register(String tubeName, Consumer<TubeRecord> recordConsumer);
}
