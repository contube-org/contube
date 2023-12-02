package com.zikeyang.contube.runtime;

import com.zikeyang.contube.api.Con;
import com.zikeyang.contube.api.Source;
import com.zikeyang.contube.api.TubeRecord;
import com.zikeyang.contube.common.TombstoneRecord;
import java.util.Collection;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class SourceTube extends Tube {
  final String sinkTubeName;
  Source source;

  public SourceTube(TubeConfig config, Con con) {
    super(config, con);
    sinkTubeName = config.getSinkTubeName();
    if (sinkTubeName == null) {
      throw new IllegalArgumentException("Sink tube name is null");
    }
  }

  @Override
  void init() throws Exception {
    super.init();
    source = createTube(config.getClazz(), Source.class);
    source.open(config.getConfig(), createContext());
  }

  @SneakyThrows
  void runTube() {
    Collection<TubeRecord> records = source.read();
    con.send(sinkTubeName, records).exceptionally(e -> {
      log.error("Send record failed", e);
      return null;
    });
    records.stream().filter(r -> r instanceof TombstoneRecord).findFirst().ifPresent(r -> {
      log.trace("Got tombstone record");
      context.stop();
    });
  }

  @Override
  public void close() {
    try {
      if (source != null) {
        source.close();
      }
    } catch (Exception e) {
      log.error("Closing tube failed", e);
    }
  }
}
