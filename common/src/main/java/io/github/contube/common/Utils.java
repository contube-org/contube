package io.github.contube.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;

public class Utils {
  private static final ObjectMapper MAPPER = new ObjectMapper();

  public static <T> T loadConfig(Map<String, Object> map, Class<T> clazz) {
    return MAPPER.convertValue(map, clazz);
  }
}
