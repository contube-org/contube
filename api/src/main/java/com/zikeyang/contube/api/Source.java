package com.zikeyang.contube.api;

import java.util.Map;

public interface Source extends AutoCloseable {
  void open(Map<String, Object> config, Context context);

  TubeRecord read();
}
