package com.zikeyang.contube.runtime;

import com.zikeyang.contube.api.Con;
import com.zikeyang.contube.api.Sink;
import com.zikeyang.contube.api.TombstoneRecord;
import com.zikeyang.contube.api.TubeRecord;
import java.util.concurrent.LinkedBlockingQueue;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SinkTube extends Tube {
  final LinkedBlockingQueue<TubeRecord> recordQueue = new LinkedBlockingQueue<>();
  Sink sink;
  ContextImpl context;

  public SinkTube(TubeConfig config, Con con) {
    super(config, con);
  }

  void write(TubeRecord record) {
    try {
      recordQueue.put(record);
    } catch (InterruptedException e) {
      context.fail(e);
    }
  }

  @Override
  void init() throws Exception {
    sink = createTube(config.getClazz(), Sink.class);
    context = createContext();
    sink.open(config.getConfig(), context);
    con.register(config.getName(), this::write);
  }

  @SneakyThrows
  @Override
  void runTube() {
    TubeRecord record = recordQueue.take();
    if (record instanceof TombstoneRecord) {
      context.stop();
      return;
    }
    sink.write(record);
  }

  @Override
  public void close() {
    try {
      if (sink != null) {
        sink.close();
      }
    } catch (Exception e) {
      log.error("Closing tube failed", e);
    }
  }
}
