package be.vinci.pae.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * This class provides methods for loading and retrieving properties from a properties file. It uses
 * the java.util.Properties class for handling properties.
 */
public class Config {

  // The Properties object that holds the loaded properties
  private static Properties props;

  /**
   * Loads properties from a file. The properties are stored in a static Properties object.
   *
   * @param file the path to the properties file
   */
  public static void load(String file) {
    props = new Properties();
    try (InputStream input = new FileInputStream(file)) {
      props.load(input);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Retrieves a property as a string.
   *
   * @param key the key of the property
   * @return the value of the property
   */
  public static String getProperty(String key) {
    return props.getProperty(key);
  }

  /**
   * Retrieves a property as an integer.
   *
   * @param key the key of the property
   * @return the value of the property
   */
  public static Integer getIntProperty(String key) {
    return Integer.parseInt(props.getProperty(key));
  }

  /**
   * Retrieves a property as a boolean.
   *
   * @param key the key of the property
   * @return the value of the property
   */
  public static boolean getBoolProperty(String key) {
    return Boolean.parseBoolean(props.getProperty(key));
  }

}