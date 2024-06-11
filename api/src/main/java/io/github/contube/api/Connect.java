package io.github.contube.api;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * The Connect interface provides a connection for connecting and exchanging data between tubes.
 * It enables a tube to dispatch records to other tubes and also to register a consumer to process incoming records.
 */
public interface Connect extends AutoCloseable {

  interface Sender {
    /**
     * Asynchronously sends a collection of records.
     *
     * @param records The collection of records to be sent.
     * @return A CompletableFuture that will be completed once the sending operation has been accomplished.
     */
    CompletableFuture<Void> send(Collection<TubeRecord> records);
  }

  /**
   * Get a Sender instance for a specified tube configuration to dispatch records asynchronously.
   *
   * @param tubeConfig The configuration of the destination tube.
   * @return A Sender instance that can be used to send records to the specified tube.
   */
  Sender getSender(TubeConfig tubeConfig);

  /**
   * Registers a consumer to process incoming records from a specified tube.
   * The registered consumer will be invoked when new records are available.
   *
   * @param tubeConfig     The configuration of the tube from which records are received.
   * @param recordConsumer The consumer that will process the incoming records.
   */
  void addReceiver(TubeConfig tubeConfig, Consumer<Collection<TubeRecord>> recordConsumer);
}

