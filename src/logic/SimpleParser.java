package logic;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SimpleParser implements ArticleParser {

  /**
   * Converts a simple JSON string from the NewsAPI into a NewsAPIResponse.
   *
   * @param inputStream InputStream with JSON from the NewsAPI
   * @param logger java.util.logging.Logger to log warnings to
   * @return NewsAPIResponse object
   */
  public List<Article> parseArticles(InputStream inputStream, Logger logger) {
    Article article = null;
    try {
      ObjectMapper mapper = new ObjectMapper();
      SimpleModule module = new SimpleModule();
      module.addDeserializer(Article.class, new ArticleDeserializer(Article.class, logger));
      mapper.registerModule(module);
      article = mapper.readValue(inputStream, Article.class);
    } catch (IOException e) {
      logger.log(Level.WARNING, "Invalid JSON file:\n {0}", e.getMessage());
    }
    if (article != null && validArticle(article)) {
      return Collections.singletonList(article);
    } else {
      return new LinkedList<>();
    }
  }

  private boolean validArticle(Article article) {
    return article.getTitle() != null
      && article.getUrl() != null
      && article.getDescription() != null
      && article.getPublishedAt() != null;
  }
}
