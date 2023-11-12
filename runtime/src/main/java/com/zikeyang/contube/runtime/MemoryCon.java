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
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MemoryCon implements Con {
  final Retryer<Void> retryer = RetryerBuilder.<Void>newBuilder()
      .retryIfExceptionOfType(ConRetriableException.class)
      .withWaitStrategy(WaitStrategies.fibonacciWait())
      .withStopStrategy(StopStrategies.stopAfterAttempt(10))
      .build();

  Map<String, Consumer<TubeRecord>> tubeMap = new ConcurrentHashMap<>();

  @SneakyThrows
  @Override
  public void send(String tubeName, TubeRecord record) {
    Callable<Void> sendTask = () -> {
      internalSend(tubeName, record);
      return null;
    };
    retryer.call(sendTask);
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
  }

  @Override
  public void close() {
    log.info("Close MemoryCon");
  }
}
