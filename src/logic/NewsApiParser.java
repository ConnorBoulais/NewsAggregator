package logic;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NewsApiParser implements ArticleParser {

  /**
   * Converts a JSON string from the NewsAPI into a NewsAPIResponse.
   *
   * @param inputStream InputStream with JSON from the NewsAPI
   * @param logger java.util.logging.Logger to log warnings to
   * @return NewsAPIResponse object
   */
  public List<Article> parseArticles(InputStream inputStream, Logger logger) {
    NewsApiResponse newsApiResponse = null;
    try {
      ObjectMapper mapper = new ObjectMapper();
      SimpleModule module = new SimpleModule();
      module.addDeserializer(Article.class, new ArticleDeserializer(Article.class, logger));
      mapper.registerModule(module);
      newsApiResponse = mapper.readValue(inputStream, NewsApiResponse.class);
    } catch (IOException e) {
      logger.log(Level.WARNING, "Invalid JSON file:\n {0}", e.getMessage());
    }
    if (newsApiResponse != null && newsApiResponse.getArticles() != null) {
      return newsApiResponse.getArticles();
    } else {
      return new LinkedList<>();
    }
  }
}