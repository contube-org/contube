package io.github.contube.api;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * A tube record encapsulates the data transferred between tubes in the system.
 * <p>
 * The TubeRecord interface provides methods to access the value of the record,
 * its schema data, its index, and to handle its commit state.
 * <p>
 * Note that the getSchemaData, getIndex, commit, and waitForCommit methods
 * provide default implementations, which can be overridden by implementing classes if needed.
 */
public interface TubeRecord {

  /**
   * Returns the value of the record.
   *
   * @return The value of the record.
   */
  byte[] getValue();

  /**
   * Returns the schema data of the record, if available.
   * By default, this method returns an empty Optional, indicating that no schema data is available.
   *
   * @return An Optional containing the schema data of the record, or an empty Optional if no schema data is available.
   */
  default Optional<byte[]> getSchemaData() {
    return Optional.empty();
  }

  /**
   * Returns the index of the record, if available.
   * By default, this method returns an empty Optional, indicating that no index is available.
   *
   * @return An Optional containing the index of the record, or an empty Optional if no index is available.
   */
  default Optional<byte[]> getIndex() {
    return Optional.empty();
  }

  /**
   * Commits the record.
   * By default, this method does nothing.
   */
  default void commit() {
  }

  /**
   * Waits for the record to be committed.
   * By default, this method immediately returns a completed CompletableFuture.
   *
   * @return A CompletableFuture that completes when the record is committed.
   */
  default CompletableFuture<Void> waitForCommit() {
    return CompletableFuture.completedFuture(null);
  }
}
