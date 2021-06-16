package logic;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileSource implements DataSource {

  private final String filePath;
  private final Logger logger;

  /**
   * Creates a FileSource given a String for the file path
   * and a java.util.logging.Logger to log errors.
   *
   * @param filePath - String representing file path
   * @param logger - java.util.logging.Logger to log errors
   */
  public FileSource(String filePath, Logger logger) {
    this.filePath = filePath;
    this.logger = logger;
  }

  /**
   * Converts the FileSource to an InputStream.
   *
   * @return InputStream representing the DataSource
   */
  public InputStream openStream() {
    try {
      return new FileInputStream(new File(filePath));
    } catch (IOException e) {
      String message = "Invalid File Path: " + e.getMessage();
      logger.log(Level.WARNING, message);
      return InputStream.nullInputStream();
    }
  }
}
