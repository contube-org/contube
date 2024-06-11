package io.github.contube.runtime;

import io.github.contube.api.Connect;
import io.github.contube.api.Source;
import io.github.contube.api.TubeConfig;
import io.github.contube.api.TubeRecord;
import io.github.contube.common.TombstoneRecord;
import java.util.Collection;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class SourceTube extends Tube {
  Source source;
  Connect.Sender sender;

  public SourceTube(TubeConfig config, Connect con) {
    super(config, con);
  }

  @Override
  void init() throws Exception {
    super.init();
    source = createTube(config.getClazz(), Source.class);
    source.open(config.getConfig(), createContext());
    sender = con.getSender(config);
  }

  @SneakyThrows
  void runTube() {
    Collection<TubeRecord> records = source.read();
    sender.send(records).exceptionally(e -> {
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
