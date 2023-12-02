package com.zikeyang.contube.runtime;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.zikeyang.contube.api.Con;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class Runtime {
  public static void main(String[] args) {
    if (args.length < 1 || Arrays.asList(args).contains("--help")) {
      log.info("runtime contube.yaml [tube1.yaml tube2.yaml ...]");
      System.exit(1);
    }

    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    Map<String, Class<? extends Tube>> tubeTypeMap = new HashMap<>();
    Con con = null;

    try {
      ConTubeConfig conTubeConfig = mapper.readValue(new File(args[0]), ConTubeConfig.class);
      log.info("Starting runtime with config: {}",
          mapper.writer().writeValueAsString(conTubeConfig));
      List<ConTubeConfig.TubeType> tubeTypes = conTubeConfig.getTubeType();
      if (tubeTypes == null) {
        tubeTypes = Arrays.asList(
            ConTubeConfig.TubeType.builder().name("sink").tubeClass(SinkTube.class.getName())
                .build(),
            ConTubeConfig.TubeType.builder().name("source").tubeClass(SourceTube.class.getName())
                .build());
      }
      for (ConTubeConfig.TubeType tubeType : tubeTypes) {
        Class<?> clazz = Class.forName(tubeType.getTubeClass());
        if (!Tube.class.isAssignableFrom(clazz)) {
          log.error("{} is not a valid tube type", tubeType.getName());
          System.exit(1);
        }
        tubeTypeMap.put(tubeType.getName(), clazz.asSubclass(Tube.class));
      }
      con = Class.forName(conTubeConfig.getConType()).asSubclass(Con.class)
          .getDeclaredConstructor().newInstance();
    } catch (Exception e) {
      log.error("Starting runtime failed", e);
      System.exit(1);
    }

    List<Thread> tubes = new ArrayList<>();

    for (String arg : Arrays.asList(args).subList(1, args.length)) {
      try {
        TubeConfig config = mapper.readValue(new File(arg), TubeConfig.class);
        log.info("Starting tube with config: {}", mapper.writer().writeValueAsString(config));
        Tube tube = tubeTypeMap.get(config.getType())
            .getDeclaredConstructor(TubeConfig.class, Con.class)
            .newInstance(config, con);
        Thread thread = new Thread(tube);
        thread.setName(String.format("tube-%s-%s", config.getType(), config.getName()));
        tubes.add(thread);
        thread.start();
      } catch (Exception e) {
        log.error("Staring tube failed", e);
        System.exit(1);
      }
    }
    for (Thread tube : tubes) {
      try {
        tube.join();
        log.info("Closed tube thread: {}", tube.getName());
      } catch (InterruptedException e) {
        log.error("Waiting for tube to stop failed", e);
        System.exit(1);
      }
    }
    try {
      con.close();
    } catch (Exception e) {
      log.error("Closing con failed", e);
      System.exit(1);
    }
  }
}
