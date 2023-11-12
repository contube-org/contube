package com.zikeyang.contube.runtime;

import com.zikeyang.contube.api.Con;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class Tube implements Runnable, AutoCloseable {
  final TubeConfig config;
  final Con con;
  final AtomicBoolean closed = new AtomicBoolean(false);
  volatile Throwable deathException = null;

  public Tube(TubeConfig config, Con con) {
    this.config = config;
    this.con = con;
  }

  <T> T createTube(String className, Class<T> tubeType)
      throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException,
      InstantiationException,
      IllegalAccessException {
    Class<?> clazz = Class.forName(className);
    if (!tubeType.isAssignableFrom(clazz)) {
      throw new IllegalArgumentException(className + " is not a valid type");
    }
    return tubeType.cast(clazz.getConstructor().newInstance());
  }

  abstract void init() throws Exception;

  abstract void runTube();

  ContextImpl createContext() {
    Thread currentThread = Thread.currentThread();
    return new ContextImpl(
        () -> {
          if (!closed.compareAndSet(false, true)) {
            deathException = new IllegalStateException("Tube already closed");
            return;
          }
          currentThread.interrupt();
        },
        (t) -> {
          deathException = t;
          currentThread.interrupt();
        }
    );
  }

  @Override
  public void run() {
    try {
      init();
      while (true) {
        runTube();
      }
    } catch (Throwable t) {
      if (deathException != null) {
        log.error("Fatal error in tube", deathException);
      } else if (!closed.get()) {
        log.error("Uncaught exception in tube", t);
      }
    } finally {
      log.info("Closing tube {}", config.getName());
      try {
        close();
      } catch (Exception e) {
        log.error("Closing tube failed", e);
      }
    }
  }
}
