package com.zikeyang.contube.api;

import java.util.Collection;
import java.util.Map;

/**
 * Source is the interface that must be implemented by a source tube.
 * A source tube reads records from an upstream system and provides them for further processing.
 * <p>
 * The lifecycle of a Source is described as follows:
 *
 * <ol>
 *     <li><b>Initialization:</b> Source instances are first initialized using the {@link #open(Map, Context)} method.
 *     This method prepares the source's configuration and context, and starts any services needed for reading.</li>
 *     <li><b>Record Processing:</b> After initialization, the Source is ready to provide records for processing.
 *     Records are fetched from the source using the {@link #read()} method, which returns them for further processing.</li>
 *     <li><b>Shutdown:</b> When the source needs to be shut down, the runtime environment will call the AutoCloseable#close method.
 *     The source should release any resources it has acquired during its lifecycle in this method.</li>
 * </ol>
 */
public interface Source extends AutoCloseable {

  /**
   * Prepares the source for operation.
   * This method is called once, at the beginning of the source's lifecycle.
   *
   * @param config  The configuration for the source.
   * @param context The context in which the source operates.
   */
  void open(Map<String, Object> config, Context context);

  /**
   * Reads a collection of records from the upstream system.
   * This method is called whenever records are needed for processing.
   *
   * @return The collection of records read from the upstream system.
   */
  Collection<TubeRecord> read();
}
