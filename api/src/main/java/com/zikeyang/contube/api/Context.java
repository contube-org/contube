package com.zikeyang.contube.api;

public interface Context {
  String getName();

  void stop();

  void fail(Throwable t);
}
