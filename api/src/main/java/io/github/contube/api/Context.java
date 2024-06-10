package io.github.contube.api;

/**
 * The Context interface provides an interaction medium for a tube with its runtime environment.
 * It allows the tube to access its name, and control its execution state.
 */
public interface Context {

  /**
   * Retrieves the name of the tube.
   *
   * @return A string representing the name of the tube.
   */
  String getName();

  /**
   * Terminates the execution of the tube.
   */
  void stop();

  /**
   * Flags the tube as failed and associates the failure with a specific exception.
   *
   * @param t The exception that caused the tube to fail.
   * This throwable will be propagated to the runtime environment for further handling.
   */
  void fail(Throwable t);
}
