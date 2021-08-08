package com.innoscripta.common.properties;

import java.util.Properties;

public enum KafkaProducerPropertyEnum {
  
  GROUP_UP("group.id", null, Integer.class),

  BOOTSTRAP_SERVERS("bootstrap.servers", null, String.class),

  METADATA_BROKER_LIST("metadata.broker.list", null, String.class),

  INTERNAL_TERMINTAION_SIGNAL(
    "internal.termintaion.signal",
    null,
    Boolean.class
  ),

  ENABLE_IDEMPOTENCE("enable.idempotence", false, Boolean.class),

  TRANSACTIONAL_ID("transactional.id", null, String.class),

  TRANSACTIONAL_TIMEOUT_MS("transaction.timeout.ms", null, String.class);

  /**
   *
   */
  private final String key;

  /**
   *
   */
  private final Class<?> clss;

  /**
   *
   */
  private final Object defaultValue;

  /**
   *
   */
  <T> KafkaProducerPropertyEnum(String key, T defaultValue, Class<T> clss) {
    this.key = key;
    this.defaultValue = defaultValue;
    this.clss = clss;
  }

  public String getKey() {
    return key;
  }

  public Class<?> getClss() {
    return clss;
  }

  public Object getDefaultValue() {
    return defaultValue;
  }

  
  /**
   *
   * @param properties
   * @return
   */
  public String producerPropertyValue(Properties properties) {
    String producerProperty = properties.getProperty(key);
    return producerProperty == null
      ? defaultValue == null ? null : defaultValue.toString()
      : producerProperty;
  }
}
