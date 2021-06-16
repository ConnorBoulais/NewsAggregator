package logic;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NewsProcessor implements Processor {

  private final DataSource dataSource;
  private final ArticleParser parser;
  private final Logger logger;

  /**
   * Constructs a NewsProcessor.
   *
   * @param dataSource - DataSource with JSON data for articles
   * @param parser - ArticleParser to be used to parse the JSON data
   * @param logger - java.util.logging.Logger to log errors
   */
  public NewsProcessor(DataSource dataSource, ArticleParser parser, Logger logger) {
    this.dataSource = dataSource;
    this.parser = parser;
    this.logger = logger;
  }

  /**
   * Processes JSON from an InputStream into a list of
   * Articles, using the provided parser.
   *
   * @return A list of articles, constructed from the JSON
   */
  public List<Article> extract() {
    try (InputStream inputStream = dataSource.openStream()) {
      return parser.parseArticles(dataSource.openStream(), logger);
    } catch (IOException e) {
      String message = "Error opening input stream: " + e.getMessage();
      logger.log(Level.WARNING, message);
      return Collections.emptyList();
    }
  }

  /**
   * TESTING METHOD - gets the DataSource of the processor.
   *
   * @return DataSource of processor
   */
  public DataSource getDataSource() {
    return this.dataSource;
  }

  /**
   * TESTING METHOD - gets the Parser of the processor.
   *
   * @return Parser of the processor
   */
  public ArticleParser getParser() {
    return parser;
  }
}
