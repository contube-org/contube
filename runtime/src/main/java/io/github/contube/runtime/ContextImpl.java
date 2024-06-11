package io.github.contube.runtime;

import io.github.contube.api.Context;
import io.github.contube.api.TubeConfig;
import java.util.function.Consumer;

public class ContextImpl implements Context {
  final Consumer<Throwable> failHandler;
  final Runnable stopHandler;
  final TubeConfig tubeConfig;

  public ContextImpl(TubeConfig tubeConfig, Runnable stopHandler, Consumer<Throwable> failHandler) {
    this.stopHandler = stopHandler;
    this.failHandler = failHandler;
    this.tubeConfig = tubeConfig;
  }

  @Override
  public void stop() {
    stopHandler.run();
  }

  @Override
  public void fail(Throwable t) {
    failHandler.accept(t);
  }

  @Override
  public String getName() {
    return tubeConfig.getName();
  }
}
