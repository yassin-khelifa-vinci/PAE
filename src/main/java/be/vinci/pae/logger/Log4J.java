package be.vinci.pae.logger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The Log4J class provides a Logger instance for logging messages. It uses the Apache Log4j logging
 * library for Java. The LOGGER constant is a static member that holds the Logger instance, and it
 * is initialized with the LogManager.getLogger method.
 */
public class Log4J {

  /**
   * Logger instance for logging messages.
   */
  public static final Logger LOGGER = LogManager.getLogger(Log4J.class);

}