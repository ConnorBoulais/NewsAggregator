package logic;

import java.io.InputStream;

public interface DataSource {
  /**
   * Converts the DataSource to an InputStream.
   *
   * @return InputStream representing the DataSource
   */
  InputStream openStream();
}

