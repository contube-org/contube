package com.zikeyang.contube.example.file;

import com.zikeyang.contube.api.Context;
import com.zikeyang.contube.api.Sink;
import com.zikeyang.contube.api.TubeRecord;
import com.zikeyang.contube.common.Utils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileSink implements Sink {
  FileTubeConfig config;
  OutputStream writer;
  Context context;

  @Override
  public void open(Map<String, Object> config, Context context) {
    log.info("FileSink open");
    this.config = Utils.loadConfig(config, FileTubeConfig.class);
    this.context = context;
    File file = new File(this.config.getPath());
    try {
      writer = new FileOutputStream(this.config.getPath());
    } catch (IOException e) {
      context.fail(e);
    }
  }

  @Override
  public void write(TubeRecord record) {
    try {
      writer.write(record.getValue());
    } catch (IOException e) {
      context.fail(e);
    }
  }

  @Override
  public void close() throws IOException {
    writer.close();
  }
}
