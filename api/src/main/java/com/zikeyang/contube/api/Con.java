package com.zikeyang.contube.api;

public interface Con extends AutoCloseable {
  void send(String tubeName, TubeRecord record);
}
