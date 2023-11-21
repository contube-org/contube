package com.zikeyang.contube.runtime;

import com.github.rholder.retry.Retryer;
import com.github.rholder.retry.RetryerBuilder;
import com.github.rholder.retry.StopStrategies;
import com.github.rholder.retry.WaitStrategies;
import com.zikeyang.contube.api.Con;
import com.zikeyang.contube.api.ConRetriableException;
import com.zikeyang.contube.api.TubeRecord;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class MemoryCon implements Con {
  final Retryer<Void> retryer = RetryerBuilder.<Void>newBuilder()
      .retryIfExceptionOfType(ConRetriableException.class)
      .withWaitStrategy(WaitStrategies.fixedWait(3, TimeUnit.SECONDS))
      .withStopStrategy(StopStrategies.stopAfterAttempt(10))
      .build();

  ExecutorService executorService = Executors.newSingleThreadExecutor();

  Map<String, Consumer<TubeRecord>> tubeMap = new ConcurrentHashMap<>();

  @SneakyThrows
  @Override
  public CompletableFuture<Void> send(String tubeName, TubeRecord record) {
    CompletableFuture<Void> future = new CompletableFuture<>();
    executorService.submit(() -> {
      Callable<Void> sendTask = () -> {
        internalSend(tubeName, record);
        return null;
      };
      try {
        retryer.call(sendTask);
        record.waitForCommit().thenRun(() -> future.complete(null));
      } catch (Exception e) {
        future.completeExceptionally(e);
      }
    });
    return future;
  }

  void internalSend(String tubeName, TubeRecord record) throws ConRetriableException {
    if (!tubeMap.containsKey(tubeName)) {
      throw new ConRetriableException(new Exception(String.format("Tube %s not found", tubeName)));
    }
    Consumer<TubeRecord> recordConsumer = tubeMap.get(tubeName);
    recordConsumer.accept(record);
  }

  @Override
  public void register(String tubeName, Consumer<TubeRecord> recordConsumer) {
    if (tubeMap.containsKey(tubeName)) {
      throw new IllegalStateException(String.format("Tube %s already registered", tubeName));
    }
    tubeMap.put(tubeName, recordConsumer);
    log.info("Register tube {} to memory con", tubeName);
  }

  @Override
  public void close() {
    log.info("Close MemoryCon");
    executorService.shutdown();
  }
}
