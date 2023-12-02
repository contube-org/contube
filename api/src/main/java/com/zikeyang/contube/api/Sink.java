package com.zikeyang.contube.api;

import java.util.Collection;
import java.util.Map;

public interface Sink extends AutoCloseable {
  void open(Map<String, Object> config, Context context);

  void write(Collection<TubeRecord> records);
}
