package com.zikeyang.contube.example.file;

import com.zikeyang.contube.api.Context;
import com.zikeyang.contube.api.Source;
import com.zikeyang.contube.api.StringRecord;
import com.zikeyang.contube.api.TubeRecord;
import com.zikeyang.contube.common.Utils;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Scanner;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileSource implements Source {
  FileTubeConfig config;
  Scanner scanner;
  Context context;

  @Override
  public void open(Map<String, Object> config, Context context) {
    log.info("FileSource open");
    this.config = Utils.loadConfig(config, FileTubeConfig.class);
    this.context = context;
    File file = new File(this.config.getPath());
    try {
      scanner = new Scanner(file);
    } catch (FileNotFoundException e) {
      log.error("File not found", e);
      context.fail(e);
    }
  }

  @Override
  public TubeRecord read() {
    if (!scanner.hasNextLine()) {
      log.info("Read finished");
      return TubeRecord.TOMBSTONE_RECORD;
    }
    return StringRecord.builder().value(scanner.nextLine()).build();
  }

  @Override
  public void close() {
    log.info("FileSource close");
    scanner.close();
  }
}
