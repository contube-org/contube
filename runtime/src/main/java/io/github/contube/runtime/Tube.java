package io.github.contube.runtime;

import io.github.contube.api.Connect;
import io.github.contube.api.TubeConfig;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.extern.log4j.Log4j2;

@Log4j2
public abstract class Tube implements Runnable, AutoCloseable {
  final TubeConfig config;
  final Connect con;
  final AtomicBoolean closed = new AtomicBoolean(false);
  volatile Throwable deathException = null;
  ContextImpl context;

  public Tube(TubeConfig config, Connect con) {
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

  void init() throws Exception {
    log.info("Initializing tube {}", config.getName());
    context = createContext();
    log.info("Initialized tube {}", config.getName());
  }

  abstract void runTube();

  ContextImpl createContext() {
    Thread currentThread = Thread.currentThread();
    return new ContextImpl(
        config,
        () -> {
          if (!closed.compareAndSet(false, true)) {
            log.warn("Tube {} already closed", config.getName());
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
      while (!closed.get()) {
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
