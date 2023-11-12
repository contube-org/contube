package com.zikeyang.contube.runtime;

import com.zikeyang.contube.api.Context;
import java.util.function.Consumer;

public class ContextImpl implements Context {
  final Consumer<Throwable> failHandler;
  final Runnable stopHandler;

  public ContextImpl(Runnable stopHandler, Consumer<Throwable> failHandler) {
    this.stopHandler = stopHandler;
    this.failHandler = failHandler;
  }

  @Override
  public void stop() {
    stopHandler.run();
  }

  @Override
  public void fail(Throwable t) {
    failHandler.accept(t);
  }
}
