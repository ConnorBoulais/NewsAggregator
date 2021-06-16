package logic;

import java.io.InputStream;
import java.util.List;
import java.util.logging.Logger;

public interface ArticleParser {
  /**
   * Parses JSON data to construct a List of Article from the JSON.
   *
   * @param inputStream - InputStream to read from
   * @param logger - java.util.logging.Logger to log errors
   * @return List of Article generated from the JSON data
   */
  List<Article> parseArticles(InputStream inputStream, Logger logger);
}