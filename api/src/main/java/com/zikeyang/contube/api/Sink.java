package com.zikeyang.contube.api;

import java.util.Map;

public interface Sink extends AutoCloseable {
  void open(Map<String, Object> config, Context context);

  void write(TubeRecord record);
}
