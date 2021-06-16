package logic;

import java.util.List;

public interface Processor {

  /**
   * Extracts a List of Articles from the Processor,
   * performing any included computation during the
   * process.
   *
   * @return A list of articles, constructed from the JSON
   */
  List<Article> extract();

  /**
   * TESTING METHOD - gets the DataSource of the processor.
   *
   * @return DataSource of processor
   */
  public DataSource getDataSource();

  /**
   * TESTING METHOD - gets the Parser of the processor.
   *
   * @return Parser of the processor
   */
  public ArticleParser getParser();
}
