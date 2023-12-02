package com.zikeyang.contube.api;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * The Con interface provides a conduit for connecting and exchanging data between tubes.
 * It enables a tube to dispatch records to other tubes and also to register a consumer to process incoming records.
 */
public interface Con extends AutoCloseable {

  /**
   * Dispatches a collection of records to a specified tube asynchronously.
   *
   * @param tubeName The name of the destination tube.
   * @param records  The collection of records to be dispatched.
   * @return A CompletableFuture that will be completed once the sending operation has been accomplished.
   */
  CompletableFuture<Void> send(String tubeName, Collection<TubeRecord> records);

  /**
   * Registers a consumer to process incoming records from a specified tube.
   * The registered consumer will be invoked when new records are available.
   *
   * @param tubeName       The name of the tube from which records are received.
   * @param recordConsumer The consumer that will process the incoming records.
   */
  void register(String tubeName, Consumer<Collection<TubeRecord>> recordConsumer);
}

