package logic;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class LogHandler extends Handler {

  private final List<String> logs = new LinkedList<>();

  /**
   * Publish a log to the logger.
   *
   * @param record - LogRecord holding the log info
   */
  @Override
  public void publish(LogRecord record) {
    logs.add(record.getMessage());
  }

  /**
   * Flush the logger.
   */
  @Override
  public void flush() {
    //not needed
  }

  /**
   * Close the logger.
   */
  @Override
  public void close() {
    //not needed
  }

  /**
   * Get a copy of the logs from the logger.
   *
   * @return List of Strings, one for each log
   */
  public List<String> getLogs() {
    return new LinkedList<>(logs);
  }

  /**
   * Clear the logger.
   */
  public void clear() {
    logs.clear();
  }
}
