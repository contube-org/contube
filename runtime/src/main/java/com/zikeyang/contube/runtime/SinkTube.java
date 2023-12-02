package com.zikeyang.contube.runtime;

import com.zikeyang.contube.api.Con;
import com.zikeyang.contube.api.Sink;
import com.zikeyang.contube.api.TubeRecord;
import com.zikeyang.contube.common.TombstoneRecord;
import java.util.Collection;
import java.util.concurrent.LinkedBlockingQueue;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class SinkTube extends Tube {
  final LinkedBlockingQueue<Collection<TubeRecord>> recordQueue = new LinkedBlockingQueue<>();
  Sink sink;

  public SinkTube(TubeConfig config, Con con) {
    super(config, con);
  }

  void write(Collection<TubeRecord> records) {
    try {
      recordQueue.put(records);
    } catch (InterruptedException e) {
      context.fail(e);
    }
  }

  @Override
  void init() throws Exception {
    super.init();
    sink = createTube(config.getClazz(), Sink.class);
    sink.open(config.getConfig(), context);
    con.register(config.getName(), this::write);
  }

  @SneakyThrows
  @Override
  void runTube() {
    Collection<TubeRecord> records = recordQueue.take();
    Collection<TubeRecord> sinkRecords =
        records.stream().filter(r -> !(r instanceof TombstoneRecord)).toList();
    if (!sinkRecords.isEmpty()) {
      sink.write(sinkRecords);
    }
    if (records.size() != sinkRecords.size()) {
      log.trace("Got tombstone record");
      context.stop();
    }
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
