package com.zikeyang.contube.runtime;

import com.zikeyang.contube.api.Con;
import com.zikeyang.contube.api.TubeRecord;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MemoryCon implements Con {
  Map<String, SinkTube> tubeMap = new ConcurrentHashMap<>();

  public void addSinkTube(String tubeName, SinkTube sinkTube) {
    tubeMap.put(tubeName, sinkTube);
  }

  @Override
  public void send(String tubeName, TubeRecord record) {
    if (!tubeMap.containsKey(tubeName)) {
      throw new RuntimeException("tube not found");
    }
    tubeMap.get(tubeName).write(record);
  }

  @Override
  public void close() {
    log.info("Close MemoryCon");
  }
}
