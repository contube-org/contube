package com.zikeyang.contube.api;

import java.util.Collection;
import java.util.Map;

/**
 * Sink is the interface that must be implemented by a sink tube.
 * A sink tube receives records from other tubes and writes them to a downstream system.
 * <p>
 * The lifecycle of a Sink is described as follows:
 *
 * <ol>
 *     <li><b>Initialization:</b> Sink instances are first initialized using the {@link #open(Map, Context)} method.
 *     This method prepares the sink's configuration and context, and starts any services needed for processing.</li>
 *     <li><b>Record Processing:</b> After initialization, the Sink is ready to receive records for writing.
 *     Records are passed to the sink using the {@link #write(Collection)} method, which writes them to the downstream system.</li>
 *     <li><b>Shutdown:</b> When the sink needs to be shut down, the runtime environment will call the AutoCloseable#close method.
 *     The sink should release any resources it has acquired during its lifecycle in this method.</li>
 * </ol>
 */
public interface Sink extends AutoCloseable {

  /**
   * Prepares the sink for operation.
   * This method is called once, at the beginning of the sink's lifecycle.
   *
   * @param config  The configuration for the sink.
   * @param context The context in which the sink operates.
   */
  void open(Map<String, Object> config, Context context);

  /**
   * Writes a collection of records to the downstream system.
   * This method is called whenever there are new records available for writing.
   *
   * @param records The collection of records to be written.
   */
  void write(Collection<TubeRecord> records);
}
