package com.zikeyang.contube.api;

import java.util.function.Consumer;

public interface Con extends AutoCloseable {
  void send(String tubeName, TubeRecord record);
  void register(String tubeName, Consumer<TubeRecord> recordConsumer);
}
