package org.apache.mahout.common;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.DefaultStringifier;
import org.apache.hadoop.util.GenericsUtil;
import org.apache.mahout.classifier.bayes.datastore.HBaseBayesDatastore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Parameters {

  private static final Logger log = LoggerFactory.getLogger(Parameters.class);

  private Map<String, String> params = new HashMap<String, String>();

  private Configuration conf = new Configuration();

  public Parameters() {

  }

  private Parameters(Map<String, String> params) {
    this.params = params;
  }

  public String get(String key) {
    return params.get(key);
  }

  public String get(String key, String defaultValue) {
    String ret = params.get(key);
    if (ret == null)
      return defaultValue;
    return ret;
  }

  public void set(String key, String value) {
    params.put(key, value);
  }

  @Override
  public String toString() {
    Configuration conf = new Configuration();
    conf.set("io.serializations",
        "org.apache.hadoop.io.serializer.JavaSerialization,org.apache.hadoop.io.serializer.WritableSerialization");
    DefaultStringifier<Map<String, String>> mapStringifier = new DefaultStringifier<Map<String, String>>(conf,
        GenericsUtil.getClass(params));
    try {
      return mapStringifier.toString(params);
    } catch (IOException e) {
      log.info("Encountered IOException while deserializing returning empty string", e);
      return "";
    }

  }

  public String print() {
    return params.toString();
  }

  public static Parameters fromString(String serializedString) throws IOException {
    Configuration conf = new Configuration();
    conf.set("io.serializations",
        "org.apache.hadoop.io.serializer.JavaSerialization,org.apache.hadoop.io.serializer.WritableSerialization");
    Map<String, String> params = new HashMap<String, String>();
    DefaultStringifier<Map<String, String>> mapStringifier = new DefaultStringifier<Map<String, String>>(conf,
        GenericsUtil.getClass(params));
    params = mapStringifier.fromString(serializedString);
    return new Parameters(params);
  }
}
