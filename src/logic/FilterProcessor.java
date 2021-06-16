package logic;

import java.util.List;
import java.util.stream.Collectors;

public class FilterProcessor implements Processor {

  private final Processor processor;
  private final FilterExpression filterExpression;

  /**
   * Constructs a FilterProcessor.
   *
   * @param processor - base processor to extract articles from
   * @param filterExpression - FilterExpression to use to filter articles
   */
  public FilterProcessor(Processor processor, FilterExpression filterExpression) {
    this.processor = processor;
    this.filterExpression = filterExpression;
  }

  /**
   * Extracts articles from the processor, while filtering
   * using the FilterExpression.
   *
   * @return A list of filtered articles
   */
  public List<Article> extract() {
    return processor.extract().stream()
        .filter(filterExpression::evaluate)
        .collect(Collectors.toList());
  }

  /**
   * TESTING METHOD - gets the DataSource of the processor.
   *
   * @return DataSource of processor
   */
  public DataSource getDataSource() {
    return this.processor.getDataSource();
  }

  /**
   * TESTING METHOD - gets the Parser of the processor.
   *
   * @return Parser of the processor
   */
  public ArticleParser getParser() {
    return processor.getParser();
  }
}
