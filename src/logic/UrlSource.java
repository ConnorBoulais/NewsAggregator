package logic;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UrlSource implements DataSource {

  private final String url;
  private final Logger logger;

  /**
   * Creates a UrlSource given a String for the url
   * and a java.util.logging.Logger to log errors.
   *
   * @param url - String representing url to read from
   * @param logger - java.util.logging.Logger to log errors
   */
  public UrlSource(String url, Logger logger) {
    this.url = url;
    this.logger = logger;
  }

  /**
   * Converts the UrlSource to an InputStream.
   *
   * @return InputStream representing the UrlSource
   */
  public InputStream openStream() {
    try {
      return new URL(url).openStream();
    } catch (IOException e) {
      String message = "Invalid URL: " + e.getMessage();
      logger.log(Level.WARNING, message);
      return InputStream.nullInputStream();
    }
  }
}
