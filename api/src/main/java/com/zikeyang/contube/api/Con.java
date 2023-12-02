package com.zikeyang.contube.api;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public interface Con extends AutoCloseable {
  CompletableFuture<Void> send(String tubeName, Collection<TubeRecord> records);

  void register(String tubeName, Consumer<Collection<TubeRecord>> recordConsumer);
}
