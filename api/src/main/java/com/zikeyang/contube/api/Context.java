package com.zikeyang.contube.api;

public interface Context {
  void stop();

  void fail(Throwable t);

  String getName();
}
